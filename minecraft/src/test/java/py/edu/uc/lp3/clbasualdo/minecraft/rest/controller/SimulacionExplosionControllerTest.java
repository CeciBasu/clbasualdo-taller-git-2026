package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.SimulacionExplosionJpaRepository;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Descripción:
 * Pruebas HTTP de la simulación de explosiones sobre entidades creadas con el CRUD.
 *
 * Responsabilidad:
 * Verificar el recorrido completo: crear las entidades con POST /entidades, detonar el
 * Creeper guardado, leer el resultado con GET y comprobar que un segundo intento sobre el
 * mismo Creeper da 409 y que un Creeper muerto da 422, sin guardar nada nuevo.
 */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:simulaciontest;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class SimulacionExplosionControllerTest {

    /** Cliente HTTP de prueba que habla con los controllers sin levantar un puerto. */
    @Autowired
    private MockMvc mockMvc;

    /** Repositorio para comprobar que los errores no persisten simulaciones. */
    @Autowired
    private SimulacionExplosionJpaRepository simulacionExplosionJpaRepository;

    /**
     * Descripción:
     * Crea una entidad con POST /entidades y devuelve su id.
     *
     * Parámetros:
     * json - Cuerpo de la entidad.
     *
     * Retorno:
     * El id asignado por H2.
     */
    private long crear(String json) throws Exception {
        MvcResult creada = mockMvc.perform(post("/entidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        Number id = JsonPath.read(creada.getResponse().getContentAsString(), "$.id");
        return id.longValue();
    }

    /**
     * Descripción:
     * Detona el Creeper con el id dado.
     *
     * Parámetros:
     * creeperId - Id del Creeper guardado.
     *
     * Retorno:
     * El pedido listo para ejecutar.
     */
    private org.springframework.test.web.servlet.RequestBuilder detonar(long creeperId) {
        return post("/simulacion/explosion")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"creeperId\": " + creeperId + "}");
    }

    /**
     * Descripción:
     * El Creeper creado con el CRUD detona sobre el Zombie y el Jugador creados con el CRUD.
     * Los GET muestran el resultado guardado y el estado final de cada entidad, y un segundo
     * intento sobre el mismo Creeper responde 409.
     */
    @Test
    void laSimulacionUsaLasEntidadesDelCrudYNoRepiteLaDetonacion() throws Exception {
        long creeperId = crear("""
                {"tipo": "Creeper", "nombre": "Creeper Sim", "vida": 20, "x": 0, "y": 64, "z": 0}
                """);
        long zombieId = crear("""
                {"tipo": "Zombie", "nombre": "Zombie Sim", "vida": 20, "x": 2, "y": 64, "z": 0}
                """);
        long jugadorId = crear("""
                {"tipo": "Jugador", "nombre": "Steve Sim", "vida": 20, "x": 5, "y": 64, "z": 0, "nivel": 1}
                """);

        MvcResult respuesta = mockMvc.perform(detonar(creeperId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.simulacionId").isNumber())
                .andExpect(jsonPath("$.radioExplosion").value(3.0))
                .andExpect(jsonPath("$.creeper.id").value(creeperId))
                .andExpect(jsonPath("$.creeper.nombre").value("Creeper Sim"))
                .andExpect(jsonPath("$.creeper.detonado").value(true))
                .andExpect(jsonPath("$.creeper.vidaFinal").value(0))
                .andExpect(jsonPath("$.afectados[?(@.id == " + zombieId + ")].vidaFinal").value(0))
                .andExpect(jsonPath("$.noAfectados[?(@.id == " + jugadorId + ")].vidaFinal").value(20))
                .andReturn();

        Number idLeido = JsonPath.read(respuesta.getResponse().getContentAsString(), "$.simulacionId");
        long id = idLeido.longValue();

        mockMvc.perform(get("/simulacion/explosion/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simulacionId").value(id))
                .andExpect(jsonPath("$.creeper.id").value(creeperId));

        mockMvc.perform(get("/simulacion/explosion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simulacionId").value(id));

        mockMvc.perform(get("/simulacion/explosiones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].simulacionId").isNotEmpty());

        mockMvc.perform(get("/entidades/{id}", creeperId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vida").value(0))
                .andExpect(jsonPath("$.detonado").value(true));
        mockMvc.perform(get("/entidades/{id}", zombieId))
                .andExpect(jsonPath("$.vida").value(0));
        mockMvc.perform(get("/entidades/{id}", jugadorId))
                .andExpect(jsonPath("$.vida").value(20));

        long antes = simulacionExplosionJpaRepository.count();
        mockMvc.perform(detonar(creeperId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.tipo").value("CreeperYaDetonadoException"))
                .andExpect(jsonPath("$.mensaje").value(containsString("ya detonó")));
        assertEquals(antes, simulacionExplosionJpaRepository.count());
    }

    /**
     * Descripción:
     * Un Creeper que murió por daño (vida 0, sin detonar) no puede detonar: 422 y nada nuevo
     * en H2.
     */
    @Test
    void unCreeperMuertoDevuelve422() throws Exception {
        long creeperId = crear("""
                {"tipo": "Creeper", "nombre": "Creeper Herido", "vida": 20, "x": 0, "y": 64, "z": 0}
                """);
        mockMvc.perform(put("/entidades/{id}", creeperId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tipo": "Creeper", "nombre": "Creeper Herido", "vida": 0, "vidaMaxima": 20,
                                 "x": 0, "y": 64, "z": 0, "detonado": false}
                                """))
                .andExpect(status().isOk());
        long antes = simulacionExplosionJpaRepository.count();

        mockMvc.perform(detonar(creeperId))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.tipo").value("CreeperMuertoException"))
                .andExpect(jsonPath("$.mensaje").value(containsString("muerto")));

        assertEquals(antes, simulacionExplosionJpaRepository.count());
    }

    /**
     * Descripción:
     * Un id que no existe responde 404 y una entidad que no es Creeper responde 400.
     */
    @Test
    void soloDetonaUnCreeperExistente() throws Exception {
        mockMvc.perform(detonar(999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.tipo").value("RecursoNoEncontradoException"));

        long zombieId = crear("""
                {"tipo": "Zombie", "nombre": "Zombie No Explota", "vida": 20, "x": 0, "y": 64, "z": 0}
                """);
        mockMvc.perform(detonar(zombieId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("DatosInvalidosException"));
    }
}
