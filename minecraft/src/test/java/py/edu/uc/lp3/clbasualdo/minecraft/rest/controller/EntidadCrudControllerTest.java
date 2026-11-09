package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.EntidadJpaRepository;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Descripcion:
 * Prueba de integracion del CRUD de la familia de entidades sobre H2.
 *
 * Responsabilidad:
 * Verificar que las operaciones GET, POST, PUT y DELETE de /entidades sigan
 * funcionando con Spring Data JPA, que el JSON discrimine el subtipo y que un
 * POST ilegal no agrande la coleccion.
 */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:crudtest;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class EntidadCrudControllerTest {

    /** Cliente HTTP de prueba que habla con los controllers sin levantar un puerto. */
    @Autowired
    private MockMvc mockMvc;

    /** Repositorio para verificar que la base no crecio con datos invalidos. */
    @Autowired
    private EntidadJpaRepository entidadJpaRepository;

    /**
     * Descripcion:
     * Crea un Zombie, lo consulta por id y por nombre, lo actualiza y lo elimina.
     */
    @Test
    void elCrudCompletoSigueFuncionandoSobreH2() throws Exception {
        String nombre = "Zombie CRUD Test";
        String entidadCreada = """
                {
                  "tipo": "Zombie",
                  "nombre": "Zombie CRUD Test",
                  "vida": 20,
                  "vidaMaxima": 20,
                  "x": 1.0,
                  "y": 64.0,
                  "z": 2.0,
                  "puertaRota": false
                }
                """;

        MvcResult resultadoCreacion = mockMvc.perform(post("/entidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entidadCreada))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.tipo").value("Zombie"))
                .andExpect(jsonPath("$.nombre").value(nombre))
                .andReturn();

        Number idLeido = JsonPath.read(
                resultadoCreacion.getResponse().getContentAsString(),
                "$.id");
        long id = idLeido.longValue();

        mockMvc.perform(get("/entidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].tipo", hasItems("Zombie")));

        mockMvc.perform(get("/entidades/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value(nombre))
                .andExpect(jsonPath("$.vida").value(20));

        mockMvc.perform(get("/entidades/nombre/{nombre}", nombre))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        String entidadActualizada = """
                {
                  "tipo": "Zombie",
                  "nombre": "Zombie CRUD Test",
                  "vida": 15,
                  "vidaMaxima": 20,
                  "x": 3.0,
                  "y": 64.0,
                  "z": 4.0,
                  "puertaRota": true
                }
                """;

        mockMvc.perform(put("/entidades/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entidadActualizada))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vida").value(15))
                .andExpect(jsonPath("$.x").value(3.0))
                .andExpect(jsonPath("$.z").value(4.0))
                .andExpect(jsonPath("$.puertaRota").value(true));

        mockMvc.perform(delete("/entidades/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/entidades/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.tipo").value("RecursoNoEncontradoException"));
    }

    /**
     * Descripcion:
     * Crea un Creeper y un Jugador para comprobar que la misma tabla guarda
     * subtipos distintos y que el JSON los discrimina con "tipo".
     */
    @Test
    void creaSubtiposYLosListaConSuTipo() throws Exception {
        String creeper = """
                {
                  "tipo": "Creeper",
                  "nombre": "Creeper CRUD Test",
                  "vida": 20,
                  "vidaMaxima": 20,
                  "x": 0.0,
                  "y": 64.0,
                  "z": 0.0,
                  "detonado": false
                }
                """;

        String jugador = """
                {
                  "tipo": "Jugador",
                  "nombre": "Jugador CRUD Test",
                  "vida": 20,
                  "vidaMaxima": 20,
                  "x": 0.0,
                  "y": 64.0,
                  "z": 0.0,
                  "nivel": 3,
                  "experiencia": 10
                }
                """;

        mockMvc.perform(post("/entidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(creeper))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("Creeper"))
                .andExpect(jsonPath("$.detonado").value(false));

        mockMvc.perform(post("/entidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jugador))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("Jugador"))
                .andExpect(jsonPath("$.nivel").value(3));

        mockMvc.perform(get("/entidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].tipo", hasItems("Creeper", "Jugador")));
    }

    /**
     * Descripcion:
     * Un POST con vida negativa, con vida mayor a la maxima o con nombre repetido
     * responde 400 y deja la cantidad de filas igual que antes.
     */
    @Test
    void unPostIlegalNoAgrandaLaColeccion() throws Exception {
        long antes = entidadJpaRepository.count();

        String vidaNegativa = """
                {
                  "tipo": "Zombie",
                  "nombre": "Zombie Ilegal",
                  "vida": -5,
                  "vidaMaxima": 20,
                  "x": 0.0,
                  "y": 64.0,
                  "z": 0.0
                }
                """;

        mockMvc.perform(post("/entidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vidaNegativa))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("DatosInvalidosException"))
                .andExpect(jsonPath("$.mensaje").value(containsString("negativa")));

        String vidaMayorALaMaxima = """
                {
                  "tipo": "Zombie",
                  "nombre": "Zombie Ilegal Dos",
                  "vida": 30,
                  "vidaMaxima": 20,
                  "x": 0.0,
                  "y": 64.0,
                  "z": 0.0
                }
                """;

        mockMvc.perform(post("/entidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vidaMayorALaMaxima))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("DatosInvalidosException"))
                .andExpect(jsonPath("$.mensaje").value(containsString("superar")));

        String repetida = """
                {
                  "tipo": "Zombie",
                  "nombre": "Zombie Repetido",
                  "vida": 20,
                  "vidaMaxima": 20,
                  "x": 0.0,
                  "y": 64.0,
                  "z": 0.0
                }
                """;

        mockMvc.perform(post("/entidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(repetida))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/entidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(repetida))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("DatosInvalidosException"));

        assertEquals(antes + 1, entidadJpaRepository.count(),
                "solo la creacion valida debe agregar una fila");
    }

    /**
     * Descripcion:
     * Un id inexistente responde 404 tanto en GET como en DELETE, con el tipo de
     * excepcion propio del recurso no encontrado.
     */
    @Test
    void unIdInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/entidades/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.tipo").value("RecursoNoEncontradoException"));

        mockMvc.perform(delete("/entidades/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.tipo").value("RecursoNoEncontradoException"));
    }

    /**
     * Descripcion:
     * Un PUT no puede convertir una fila de un subtipo en otro.
     */
    @Test
    void actualizarNoPermiteCambiarElTipo() throws Exception {
        String zombie = """
                {
                  "tipo": "Zombie",
                  "nombre": "Zombie Tipo Test",
                  "vida": 20,
                  "vidaMaxima": 20,
                  "x": 0.0,
                  "y": 64.0,
                  "z": 0.0
                }
                """;

        MvcResult creada = mockMvc.perform(post("/entidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(zombie))
                .andExpect(status().isCreated())
                .andReturn();

        Number idLeido = JsonPath.read(creada.getResponse().getContentAsString(), "$.id");

        String creeper = """
                {
                  "tipo": "Creeper",
                  "nombre": "Zombie Tipo Test",
                  "vida": 20,
                  "vidaMaxima": 20,
                  "x": 0.0,
                  "y": 64.0,
                  "z": 0.0
                }
                """;

        mockMvc.perform(put("/entidades/{id}", idLeido.longValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(creeper))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("DatosInvalidosException"))
                .andExpect(jsonPath("$.mensaje").value(containsString("tipo")));
    }
}
