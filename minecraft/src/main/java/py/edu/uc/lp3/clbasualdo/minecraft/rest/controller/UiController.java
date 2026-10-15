package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.clbasualdo.minecraft.constants.ApiPaths;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.UICapable;
import py.edu.uc.lp3.clbasualdo.minecraft.service.UiService;

/**
 * Descripcion:
 * Controlador que muestra los datos que la UI necesita de cada elemento
 * representable del dominio.
 *
 * Responsabilidad:
 * Se encarga de pedirle al servicio las entidades UICapable y devolver por la API
 * sus datos comunes (nombre, vida, estado y posicion), los de dibujo (imagen,
 * sonido y velocidad) y los propios de cada familia, siempre a traves del tipo de
 * la interfaz UICapable.
 */
@RestController
public class UiController {

    /** Servicio que arma la lista de entidades UICapable. */
    private final UiService uiService;

    /**
     * Descripcion:
     * Crea el controlador con el servicio que va a usar.
     *
     * Parametros:
     * uiService - El servicio de UI.
     */
    public UiController(UiService uiService) {
        this.uiService = uiService;
    }

    /**
     * Descripcion:
     * Responde a la ruta de UI. Pide la lista de entidades al servicio y por cada
     * una arma un mapa con todos sus datos representables. Todas se tratan como
     * UICapable, y cada una responde sus datos sin que el controller sepa de que
     * clase concreta se trata.
     *
     * Retorno:
     * Una lista con los datos de UI de cada elemento representable.
     */
    @GetMapping(ApiPaths.UI)
    public List<Map<String, Object>> ui() {
        return uiService.entidadesUi().stream()
                .map(this::aJson)
                .collect(Collectors.toList());
    }

    /**
     * Descripcion:
     * Arma el mapa con los datos de UI de una entidad. Primero pone los datos
     * comunes y de dibujo que expone la interfaz, y despues le suma los datos
     * propios que devuelve datosExtra().
     *
     * Parametros:
     * entidad - La entidad UICapable que se quiere representar.
     *
     * Retorno:
     * Un mapa ordenado con todos los datos de la entidad.
     */
    private Map<String, Object> aJson(UICapable entidad) {
        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("tipo", entidad.getClass().getSimpleName());
        datos.put("imagen", entidad.imagen());
        datos.put("posicion", entidad.posicion());
        datos.put("velocidad", entidad.velocidad());
        datos.put("sonido", entidad.sonido());
        return datos;
    }
}
