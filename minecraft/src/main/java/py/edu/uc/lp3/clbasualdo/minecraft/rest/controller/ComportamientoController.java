package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.edu.uc.lp3.clbasualdo.minecraft.constants.ApiPaths;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Entidad;
import py.edu.uc.lp3.clbasualdo.minecraft.service.ComportamientoService;

/**
 * Descripción:
 * Controlador que muestra cómo reacciona cada tipo de entidad del juego.
 *
 * Responsabilidad:
 * Se encarga de pedirle al servicio una entidad de cada tipo y devolver sus datos
 * y su reacción por la API.
 */
@RestController
@Tag(name = "Modelo")
public class ComportamientoController {

    /** Servicio que arma la lista de entidades de muestra. */
    private final ComportamientoService comportamientoService;

    /**
     * Descripción:
     * Crea el controlador con el servicio que va a usar.
     *
     * Parámetros:
     * comportamientoService - El servicio de comportamiento.
     */
    public ComportamientoController(ComportamientoService comportamientoService) {
        this.comportamientoService = comportamientoService;
    }

    /**
     * Descripción:
     * Responde a la ruta de comportamiento. Pide la lista de entidades al servicio
     * y por cada una arma un mapa con su tipo, su reacción, su vida y su posición.
     * Todas las entidades se tratan como Entidad, y cada una responde con su propia reacción.
     *
     * Retorno:
     * Una lista con los datos de cada entidad.
     */
    @Operation(summary = "Reacción de cada tipo de entidad", description = "Polimorfismo: cada entidad responde reaccionar() a su manera.")
    @GetMapping(ApiPaths.COMPORTAMIENTO)
    public List<Map<String, Object>> comportamiento() {
        return comportamientoService.entidadesDeMuestra().stream()
                .map(e -> Map.<String, Object>of(
                        "tipo", e.getClass().getSimpleName(),
                        "reaccion", e.reaccionar(),
                        "vida", e.getVida(),
                        "posicion", Map.of("x", e.getX(), "y", e.getY(), "z", e.getZ())
                ))
                .collect(Collectors.toList());
    }
}
