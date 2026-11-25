package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.EmboscadaJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.EntidadJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.SimulacionExplosionJpaRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:validaciontest;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class ValidacionExplosionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntidadJpaRepository entidadJpaRepository;

    @Autowired
    private SimulacionExplosionJpaRepository simulacionExplosionJpaRepository;

    @Autowired
    private EmboscadaJpaRepository emboscadaJpaRepository;

    @BeforeEach
    void preparar() throws Exception {
        mockMvc.perform(post("/entidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tipo": "Creeper", "nombre": "Creeper guardado %s", "vida": 20,
                                 "x": 0, "y": 64, "z": 0}
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isCreated());
    }

    @Test
    void validarConFormatoDeOtraApiDevuelve200SinTocarH2() throws Exception {
        long filasAntes = filasEnH2();

        mockMvc.perform(post("/explosiones/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "saludCreeper": 20,
                                  "yaDetonado": false,
                                  "tiempoExplosion": 3,
                                  "tiempoTranscurrido": 3,
                                  "radioExplosion": 5,
                                  "creeper": {"x": 0, "y": 64, "z": 0},
                                  "objetivos": [{"tipo": "jugador", "salud": 20,
                                                 "posicion": {"x": 2, "y": 64, "z": 0}}]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aceptada").value(true))
                .andExpect(jsonPath("$.mensaje").value("El Creeper está vivo y todavía no detonó."));

        assertEquals(filasAntes, filasEnH2());
    }

    @Test
    void saludCreeperCeroDevuelve422SinTocarH2() throws Exception {
        long filasAntes = filasEnH2();

        mockMvc.perform(post("/explosiones/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"saludCreeper\": 0, \"yaDetonado\": false}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.tipo").value("CreeperMuertoException"));

        assertEquals(filasAntes, filasEnH2());
    }

    @Test
    void yaDetonadoTrueDevuelve409SinTocarH2() throws Exception {
        long filasAntes = filasEnH2();

        mockMvc.perform(post("/explosiones/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"saludCreeper\": 20, \"yaDetonado\": true}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.tipo").value("CreeperYaDetonadoException"));

        assertEquals(filasAntes, filasEnH2());
    }

    @Test
    void camposAusentesValenVivoYNoDetonado() throws Exception {
        long filasAntes = filasEnH2();

        mockMvc.perform(post("/explosiones/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aceptada").value(true));

        assertEquals(filasAntes, filasEnH2());
    }

    private long filasEnH2() {
        return entidadJpaRepository.count()
                + simulacionExplosionJpaRepository.count()
                + emboscadaJpaRepository.count();
    }
}
