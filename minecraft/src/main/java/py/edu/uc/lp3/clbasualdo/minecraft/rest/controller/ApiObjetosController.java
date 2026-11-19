package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.edu.uc.lp3.clbasualdo.minecraft.constants.ApiPaths;
import py.edu.uc.lp3.clbasualdo.minecraft.rest.dto.ObjetoApiDto;
import py.edu.uc.lp3.clbasualdo.minecraft.service.ApiObjetosService;

@RestController
@Tag(name = "Objetos externos", description = "Consulta a api.restful-api.dev mediante WebClient")
public class ApiObjetosController {

    private final ApiObjetosService apiObjetosService;

    public ApiObjetosController(ApiObjetosService apiObjetosService) {
        this.apiObjetosService = apiObjetosService;
    }

    @Operation(summary = "Lista los objetos de la API externa")
    @GetMapping(ApiPaths.API_OBJETOS_EXTERNOS)
    public List<ObjetoApiDto> listar() {
        return apiObjetosService.listarObjetos();
    }

    @Operation(summary = "Obtiene un objeto externo por su id")
    @GetMapping(ApiPaths.API_OBJETOS_EXTERNOS_ID)
    public ObjetoApiDto obtener(@PathVariable String id) {
        return apiObjetosService.obtenerObjeto(id);
    }
}
