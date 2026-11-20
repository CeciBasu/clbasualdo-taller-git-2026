package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import com.jayway.jsonpath.JsonPath;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.EmboscadaJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.SimulacionExplosionJpaRepository;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Descripción:
 * Pruebas del escenario «emboscada del Creeper» con dos servidores HTTP de prueba que hacen
 * de A2 y de A3.
 *
 * Responsabilidad:
 * Verificar el camino feliz (las dos reglas aceptan y la jugada se guarda y se vuelve a leer),
 * el rechazo de A2 y el de A3 (la base queda igual y se ve de quién fue el rechazo), un
 * compañero caído (503 atribuido) y que mis propias reglas cortan antes de llamar a nadie.
 */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:emboscadatest;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class EmboscadaControllerTest {

    /** Servidor de prueba que hace de A2. */
    private static final CompaneroFalso A2 = new CompaneroFalso("/api/minecraft/reglas/mecha-corta");

    /** Servidor de prueba que hace de A3. */
    private static final CompaneroFalso A3 = new CompaneroFalso("/reglas/emboscada");

    /** Cliente HTTP de prueba. */
    @Autowired
    private MockMvc mockMvc;

    /** Repositorio de jugadas, para contar. */
    @Autowired
    private EmboscadaJpaRepository emboscadaJpaRepository;

    /** Repositorio de explosiones, para contar. */
    @Autowired
    private SimulacionExplosionJpaRepository simulacionExplosionJpaRepository;

    /**
     * Descripción:
     * Apunta los clientes de A2 y A3 a los servidores de prueba.
     *
     * Parámetros:
     * registro - Propiedades dinámicas de Spring.
     */
    @DynamicPropertySource
    static void companeros(DynamicPropertyRegistry registro) {
        registro.add("companeros.a2.base-url", A2::baseUrl);
        registro.add("companeros.a3.base-url", A3::baseUrl);
        registro.add("companeros.a3.nombre", () -> "Compañero Tres");
    }

    /**
     * Descripción:
     * Apaga los servidores de prueba.
     */
    @AfterAll
    static void apagar() {
        A2.apagar();
        A3.apagar();
    }

    /**
     * Descripción:
     * Por defecto los dos compañeros aceptan.
     */
    @BeforeEach
    void aceptanTodos() {
        A2.responder(200, "{\"regla\":\"mecha-corta\",\"aceptada\":true,\"objetivo\":\"Steve\"}");
        A3.responder(200, "{\"aceptada\":true}");
    }

    @Test
    void elCaminoFelizGuardaLaJugadaYSeVuelveALeer() throws Exception {
        long creeperId = crear("Creeper Feliz", "Creeper", 0);
        long jugadorId = crear("Steve Feliz", "Jugador", 2);

        MvcResult resultado = mockMvc.perform(emboscar(creeperId, jugadorId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.a2.companero").value("Irina Insfrán"))
                .andExpect(jsonPath("$.a2.respuesta").value(containsString("mecha-corta")))
                .andExpect(jsonPath("$.a3.companero").value("Compañero Tres"))
                .andExpect(jsonPath("$.simulacion.creeper.detonado").value(true))
                .andExpect(jsonPath("$.simulacion.afectados[?(@.id == " + jugadorId + ")].vidaFinal").value(0))
                .andReturn();

        assertTrue(A2.ultimoCuerpo().contains("\"posicionX\""), "A2 recibe el cuerpo de su OpenAPI");
        assertTrue(A2.ultimoCuerpo().contains("\"jugadores\""));
        assertTrue(A3.ultimoCuerpo().contains("\"jugador\""));

        Number id = JsonPath.read(resultado.getResponse().getContentAsString(), "$.id");
        mockMvc.perform(get("/escenarios/emboscadas/{id}", id.longValue()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creeperId").value(creeperId))
                .andExpect(jsonPath("$.jugadorId").value(jugadorId));
        mockMvc.perform(get("/entidades/{id}", creeperId))
                .andExpect(jsonPath("$.detonado").value(true));
    }

    @Test
    void siA2RechazaLaBaseQuedaIgualYSeAtribuyeAA2() throws Exception {
        long creeperId = crear("Creeper Lejos", "Creeper", 0);
        long jugadorId = crear("Steve Lejos", "Jugador", 10);
        A2.responder(422, "{\"error\":\"fuera_de_alcance\",\"codigo\":\"fuera_de_alcance\","
                + "\"mensaje\":\"Ningún jugador vivo a 3 bloques del creeper.\"}");
        int llamadasA3 = A3.llamadas();
        long emboscadas = emboscadaJpaRepository.count();
        long explosiones = simulacionExplosionJpaRepository.count();

        mockMvc.perform(emboscar(creeperId, jugadorId))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.tipo").value("RechazoDeCompaneroException"))
                .andExpect(jsonPath("$.rechazadoPor").value("A2"))
                .andExpect(jsonPath("$.companero").value("Irina Insfrán"))
                .andExpect(jsonPath("$.estadoCompanero").value(422))
                .andExpect(jsonPath("$.respuestaCompanero").value(containsString("fuera_de_alcance")));

        assertEquals(llamadasA3, A3.llamadas(), "si A2 rechaza, no se le pregunta a A3");
        assertEquals(emboscadas, emboscadaJpaRepository.count());
        assertEquals(explosiones, simulacionExplosionJpaRepository.count());
        mockMvc.perform(get("/entidades/{id}", creeperId))
                .andExpect(jsonPath("$.detonado").value(false))
                .andExpect(jsonPath("$.vida").value(20));
    }

    @Test
    void siA3RechazaLaBaseQuedaIgualYSeAtribuyeAA3() throws Exception {
        long creeperId = crear("Creeper Tres", "Creeper", 0);
        long jugadorId = crear("Steve Tres", "Jugador", 1);
        A3.responder(409, "{\"mensaje\":\"Regla de A3 incumplida.\"}");
        long emboscadas = emboscadaJpaRepository.count();
        long explosiones = simulacionExplosionJpaRepository.count();

        mockMvc.perform(emboscar(creeperId, jugadorId))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.rechazadoPor").value("A3"))
                .andExpect(jsonPath("$.companero").value("Compañero Tres"))
                .andExpect(jsonPath("$.estadoCompanero").value(409))
                .andExpect(jsonPath("$.mensaje").value(containsString("Rechazo de A3")));

        assertEquals(emboscadas, emboscadaJpaRepository.count());
        assertEquals(explosiones, simulacionExplosionJpaRepository.count());
        mockMvc.perform(get("/entidades/{id}", jugadorId))
                .andExpect(jsonPath("$.vida").value(20));
    }

    @Test
    void siLaApiDeA2FallaSeRespondeQueNoEstaDisponible() throws Exception {
        long creeperId = crear("Creeper Caos", "Creeper", 0);
        long jugadorId = crear("Steve Caos", "Jugador", 1);
        A2.responder(500, "{\"error\":\"interno\"}");

        mockMvc.perform(emboscar(creeperId, jugadorId))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.tipo").value("CompaneroNoDisponibleException"))
                .andExpect(jsonPath("$.rechazadoPor").value("A2"))
                .andExpect(jsonPath("$.estadoCompanero").value(500));
    }

    @Test
    void misReglasCortanAntesDeLlamarALosCompaneros() throws Exception {
        long creeperId = crear("Creeper Usado", "Creeper", 0);
        long jugadorId = crear("Steve Usado", "Jugador", 1);
        mockMvc.perform(emboscar(creeperId, jugadorId)).andExpect(status().isCreated());
        int llamadasA2 = A2.llamadas();

        mockMvc.perform(emboscar(creeperId, jugadorId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.tipo").value("CreeperYaDetonadoException"));

        assertEquals(llamadasA2, A2.llamadas(), "con el Creeper ya detonado no se llama a A2");
    }

    /**
     * Descripción:
     * Crea una entidad en mi H2 con 20 de vida, en (x, 64, 0).
     *
     * Parámetros:
     * nombre - Nombre único.
     * tipo - Creeper o Jugador.
     * x - Posición X.
     *
     * Retorno:
     * El id asignado.
     */
    private long crear(String nombre, String tipo, double x) throws Exception {
        String json = "{\"tipo\": \"" + tipo + "\", \"nombre\": \"" + nombre + "\", \"vida\": 20, "
                + "\"x\": " + x + ", \"y\": 64, \"z\": 0}";
        MvcResult creada = mockMvc.perform(post("/entidades").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andReturn();
        Number id = JsonPath.read(creada.getResponse().getContentAsString(), "$.id");
        return id.longValue();
    }

    /**
     * Descripción:
     * Arma el pedido del escenario.
     *
     * Parámetros:
     * creeperId - Id del Creeper.
     * jugadorId - Id del Jugador.
     *
     * Retorno:
     * El pedido.
     */
    private RequestBuilder emboscar(long creeperId, long jugadorId) {
        return post("/escenarios/emboscada")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"creeperId\": " + creeperId + ", \"jugadorId\": " + jugadorId + "}");
    }

    /**
     * Descripción:
     * Servidor HTTP mínimo que hace de compañero: responde lo que se le configure y recuerda
     * el último cuerpo recibido.
     */
    private static final class CompaneroFalso {

        /** Servidor del JDK. */
        private final HttpServer servidor;

        /** Código que va a responder. */
        private volatile int estado = 200;

        /** Cuerpo que va a responder. */
        private volatile String cuerpo = "{}";

        /** Último cuerpo recibido. */
        private volatile String ultimoCuerpo = "";

        /** Cantidad de llamadas recibidas. */
        private final AtomicInteger llamadas = new AtomicInteger();

        /**
         * Descripción:
         * Levanta el servidor en un puerto libre con la ruta de la regla.
         *
         * Parámetros:
         * ruta - Ruta de la regla.
         */
        CompaneroFalso(String ruta) {
            try {
                servidor = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
            servidor.createContext(ruta, intercambio -> {
                ultimoCuerpo = new String(intercambio.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                llamadas.incrementAndGet();
                byte[] bytes = cuerpo.getBytes(StandardCharsets.UTF_8);
                intercambio.getResponseHeaders().add("Content-Type", "application/json");
                intercambio.sendResponseHeaders(estado, bytes.length);
                try (OutputStream salida = intercambio.getResponseBody()) {
                    salida.write(bytes);
                }
            });
            servidor.start();
        }

        /** @return la URL base del servidor. */
        String baseUrl() {
            return "http://localhost:" + servidor.getAddress().getPort();
        }

        /**
         * Descripción:
         * Configura la próxima respuesta.
         *
         * Parámetros:
         * nuevoEstado - Código HTTP.
         * nuevoCuerpo - Cuerpo JSON.
         */
        void responder(int nuevoEstado, String nuevoCuerpo) {
            this.estado = nuevoEstado;
            this.cuerpo = nuevoCuerpo;
        }

        /** @return el último cuerpo recibido. */
        String ultimoCuerpo() {
            return ultimoCuerpo;
        }

        /** @return la cantidad de llamadas recibidas. */
        int llamadas() {
            return llamadas.get();
        }

        /**
         * Descripción:
         * Apaga el servidor.
         */
        void apagar() {
            servidor.stop(0);
        }
    }
}
