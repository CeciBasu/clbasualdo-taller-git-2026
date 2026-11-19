package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import py.edu.uc.lp3.clbasualdo.minecraft.rest.dto.ObjetoApiDto;
import py.edu.uc.lp3.clbasualdo.minecraft.service.ApiObjetosService;

class ApiObjetosControllerTest {

    private final ApiObjetosService apiObjetosService = mock(ApiObjetosService.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new ApiObjetosController(apiObjetosService))
            .build();

    @Test
    void listaLosObjetosPorHttp() throws Exception {
        when(apiObjetosService.listarObjetos())
                .thenReturn(List.of(new ObjetoApiDto("1", "Google Pixel 6 Pro", Map.of("color", "Cloudy White"))));

        mockMvc.perform(get("/api/objetos-externos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Google Pixel 6 Pro"))
                .andExpect(jsonPath("$[0].data.color").value("Cloudy White"));
    }

    @Test
    void obtieneUnObjetoPorHttp() throws Exception {
        when(apiObjetosService.obtenerObjeto("7"))
                .thenReturn(new ObjetoApiDto("7", "Apple MacBook Pro 16", Map.of("year", 2019)));

        mockMvc.perform(get("/api/objetos-externos/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("7"))
                .andExpect(jsonPath("$.data.year").value(2019));
    }
}
