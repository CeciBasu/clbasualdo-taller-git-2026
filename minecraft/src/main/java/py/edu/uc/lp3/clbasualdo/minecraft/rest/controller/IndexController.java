package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.edu.uc.lp3.clbasualdo.minecraft.constants.ApiPaths;

/**
 * Descripción:
 * Controlador de la página de inicio de la API.
 *
 * Responsabilidad:
 * Se encarga de responder con un mensaje de bienvenida cuando se entra a la dirección principal.
 */
@RestController
@Tag(name = "Modelo")
public class IndexController {

    /**
     * Descripción:
     * Responde a la ruta de inicio con un texto que dice de que trata la API.
     *
     * Retorno:
     * El texto de bienvenida.
     */
    @Operation(summary = "Confirma que el servicio está vivo")
    @GetMapping(ApiPaths.INDEX)
    public String index() {
        return "API Minecraft - clbasualdo - Taller Git LP3 2026";
    }
}
