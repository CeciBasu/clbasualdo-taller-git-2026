package py.edu.uc.lp3.clbasualdo.minecraft.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Descripcion:
 * Controlador de la pagina de inicio de la API.
 *
 * Responsabilidad:
 * Se encarga de responder con un mensaje de bienvenida cuando se entra a la direccion principal "/".
 */
@RestController
public class IndexController {

    /**
     * Descripcion:
     * Responde a la ruta "/" con un texto que dice de que trata la API.
     *
     * Retorno:
     * El texto de bienvenida.
     */
    @GetMapping("/")
    public String index() {
        return "API Minecraft - clbasualdo - Taller Git LP3 2026";
    }
}
