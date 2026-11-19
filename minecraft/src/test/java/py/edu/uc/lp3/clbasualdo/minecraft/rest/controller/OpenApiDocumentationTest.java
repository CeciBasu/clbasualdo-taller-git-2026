package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void elJsonOpenApiDocumentaTodosLosControladores() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("API Minecraft - LP3"))
                .andExpect(jsonPath("$.paths['/'].get").exists())
                .andExpect(jsonPath("$.paths['/entidades'].get").exists())
                .andExpect(jsonPath("$.paths['/esqueleto'].get").exists())
                .andExpect(jsonPath("$.paths['/comportamiento'].get").exists())
                .andExpect(jsonPath("$.paths['/ui'].get").exists())
                .andExpect(jsonPath("$.paths['/simulacion/explosion'].post").exists())
                .andExpect(jsonPath("$.paths['/api/objetos-externos'].get").exists());
    }
}
