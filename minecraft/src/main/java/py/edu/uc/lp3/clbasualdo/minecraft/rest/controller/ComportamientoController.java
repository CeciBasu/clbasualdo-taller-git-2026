package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.clbasualdo.minecraft.constants.ApiPaths;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Entidad;
import py.edu.uc.lp3.clbasualdo.minecraft.service.ComportamientoService;

/**
 * Descripcion:
 * Controlador que muestra como reacciona cada tipo de entidad del juego.
 *
 * Responsabilidad:
 * Se encarga de pedirle al servicio una entidad de cada tipo y devolver sus datos
 * y su reaccion por la API.
 */
@RestController
public class ComportamientoController {

    /** Servicio que arma la lista de entidades de muestra. */
    private final ComportamientoService comportamientoService;

    /**
     * Descripcion:
     * Crea el controlador con el servicio que va a usar.
     *
     * Parametros:
     * comportamientoService - El servicio de comportamiento.
     */
    public ComportamientoController(ComportamientoService comportamientoService) {
        this.comportamientoService = comportamientoService;
    }

    /**
     * Descripcion:
     * Responde a la ruta de comportamiento. Pide la lista de entidades al servicio
     * y por cada una arma un mapa con su tipo, su reaccion, su vida y su posicion.
     * Todas las entidades se tratan como Entidad, y cada una responde con su propia reaccion.
     *
     * Retorno:
     * Una lista con los datos de cada entidad.
     */
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
