package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Descripción:
 * Pruebas de las rutas que publican las dos reglas y del documento OpenAPI.
 *
 * Responsabilidad:
 * Verificar que un compañero puede consultar cada regla (200 si acepta, 422 o 409 si
 * rechaza) y que el OpenAPI describe el CRUD, la simulación y las reglas.
 */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:reglastest;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class ReglasYOpenApiTest {

    /** Cliente HTTP de prueba. */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Descripción:
     * Regla 1: con vida acepta, sin vida rechaza con 422 y sin "vida" responde 400.
     */
    @Test
    void laReglaDelCreeperVivo() throws Exception {
        mockMvc.perform(post("/reglas/creeper-vivo").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"C\", \"vida\": 5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regla").value("creeper-vivo"))
                .andExpect(jsonPath("$.aceptada").value(true));
        mockMvc.perform(post("/reglas/creeper-vivo").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"C\", \"vida\": 0}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.tipo").value("CreeperMuertoException"));
        mockMvc.perform(post("/reglas/creeper-vivo").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"C\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("DatosInvalidosException"));
    }

    /**
     * Descripción:
     * Regla 2: sin detonar acepta y ya detonado rechaza con 409.
     */
    @Test
    void laReglaDeLaDetonacionUnica() throws Exception {
        mockMvc.perform(post("/reglas/detonacion-unica").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"detonado\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Creeper todavía no detonó: puede detonar."));
        mockMvc.perform(post("/reglas/detonacion-unica").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"detonado\": true}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.tipo").value("CreeperYaDetonadoException"));
    }

    /**
     * Descripción:
     * El documento OpenAPI está publicado y describe el CRUD, la simulación y las dos reglas
     * con sus códigos de error.
     */
    @Test
    void elOpenApiDescribeLaApi() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").exists())
                .andExpect(jsonPath("$.paths['/entidades'].post.responses['400']").exists())
                .andExpect(jsonPath("$.paths['/simulacion/explosion'].post.responses['409']").exists())
                .andExpect(jsonPath("$.paths['/simulacion/explosion'].post.responses['422']").exists())
                .andExpect(jsonPath("$.paths['/reglas/creeper-vivo'].post.responses['422']").exists())
                .andExpect(jsonPath("$.paths['/reglas/detonacion-unica'].post.responses['409']").exists())
                .andExpect(jsonPath("$.paths['/explosiones/validar'].post.responses['200']").exists())
                .andExpect(jsonPath("$.paths['/explosiones/validar'].post.responses['409']").exists())
                .andExpect(jsonPath("$.paths['/explosiones/validar'].post.responses['422']").exists())
                .andExpect(jsonPath("$.components.schemas.ErrorRespuesta").exists());
    }
}
