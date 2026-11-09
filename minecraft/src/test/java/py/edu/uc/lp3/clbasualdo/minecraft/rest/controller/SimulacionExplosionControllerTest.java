package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.SimulacionExplosionJpaRepository;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Descripcion:
 * Pruebas HTTP de la simulacion de explosiones persistida.
 *
 * Responsabilidad:
 * Verificar el caso exitoso, el GET posterior que muestra lo guardado y que cada
 * excepcion de dominio tenga su codigo y su JSON a traves del handler.
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
     * Descripcion:
     * La simulacion exitosa responde 201 con id, el Creeper detonado, el Zombie
     * alcanzado y el Jugador fuera del radio. Despues, los GET muestran el mismo
     * resultado persistido y las entidades guardadas quedan con su estado final.
     */
    @Test
    void laSimulacionExitosaSePersisteYSePuedeConsultar() throws Exception {
        MvcResult respuesta = mockMvc.perform(post("/simulacion/explosion"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.simulacionId").isNumber())
                .andExpect(jsonPath("$.fecha").exists())
                .andExpect(jsonPath("$.radioExplosion").value(3.0))
                .andExpect(jsonPath("$.creeper.detonado").value(true))
                .andExpect(jsonPath("$.creeper.vivo").value(false))
                .andExpect(jsonPath("$.creeper.vidaInicial").value(20))
                .andExpect(jsonPath("$.creeper.vidaFinal").value(0))
                .andExpect(jsonPath("$.afectados.length()").value(1))
                .andExpect(jsonPath("$.afectados[0].tipo").value("Zombie"))
                .andExpect(jsonPath("$.afectados[0].vidaFinal").value(0))
                .andExpect(jsonPath("$.noAfectados.length()").value(1))
                .andExpect(jsonPath("$.noAfectados[0].tipo").value("Jugador"))
                .andExpect(jsonPath("$.noAfectados[0].vidaFinal").value(20))
                .andReturn();

        Number idLeido = JsonPath.read(respuesta.getResponse().getContentAsString(), "$.simulacionId");
        long id = idLeido.longValue();

        mockMvc.perform(get("/simulacion/explosion/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simulacionId").value(id))
                .andExpect(jsonPath("$.creeper.detonado").value(true));

        mockMvc.perform(get("/simulacion/explosion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simulacionId").value(id));

        mockMvc.perform(get("/simulacion/explosiones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].simulacionId").value(id));

        mockMvc.perform(get("/entidades/nombre/{nombre}", "Creeper Simulado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Creeper"))
                .andExpect(jsonPath("$.vida").value(0))
                .andExpect(jsonPath("$.detonado").value(true));

        mockMvc.perform(get("/entidades/nombre/{nombre}", "Zombie Simulado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vida").value(0));

        mockMvc.perform(get("/entidades/nombre/{nombre}", "Steve Simulado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vida").value(20));
    }

    /**
     * Descripcion:
     * Un Creeper muerto responde 422 con el tipo de excepcion y no agrega una
     * simulacion nueva a la base.
     */
    @Test
    void unCreeperMuertoDevuelve422() throws Exception {
        long antes = simulacionExplosionJpaRepository.count();

        mockMvc.perform(post("/simulacion/explosion").param("vidaCreeper", "0"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.tipo").value("CreeperMuertoException"))
                .andExpect(jsonPath("$.mensaje").value(containsString("muerto")));

        assertEquals(antes, simulacionExplosionJpaRepository.count());
    }

    /**
     * Descripcion:
     * Un segundo intento de detonacion responde 409 con el tipo de excepcion y no
     * agrega una simulacion nueva a la base.
     */
    @Test
    void unCreeperYaDetonadoDevuelve409() throws Exception {
        long antes = simulacionExplosionJpaRepository.count();

        mockMvc.perform(post("/simulacion/explosion").param("repetirDetonacion", "true"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.tipo").value("CreeperYaDetonadoException"))
                .andExpect(jsonPath("$.mensaje").value(containsString("ya detonó")));

        assertEquals(antes, simulacionExplosionJpaRepository.count());
    }
}
