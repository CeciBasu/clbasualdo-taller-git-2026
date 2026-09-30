package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.clbasualdo.minecraft.constants.ApiPaths;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Esqueleto;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.service.EsqueletoService;

/**
 * Descripcion:
 * Controlador que permite crear un esqueleto desde la API.
 *
 * Responsabilidad:
 * Se encarga de recibir los datos por la direccion, pedirle al servicio que cree
 * el esqueleto y devolver su informacion o un mensaje de error si los datos no sirven.
 */
@RestController
public class EsqueletoController {

    /** Servicio que arma el esqueleto con las reglas del dominio. */
    private final EsqueletoService esqueletoService;

    /**
     * Descripcion:
     * Crea el controlador con el servicio que va a usar.
     *
     * Parametros:
     * esqueletoService - El servicio del esqueleto.
     */
    public EsqueletoController(EsqueletoService esqueletoService) {
        this.esqueletoService = esqueletoService;
    }

    /**
     * Descripcion:
     * Responde a la ruta del esqueleto. Le pasa los datos al servicio, que crea el
     * esqueleto y lo ubica en la posicion pedida, y devuelve sus datos.
     * Si algun dato no es valido (por ejemplo una vida de 0), devuelve un error 400 con el mensaje.
     * Todos los parametros son opcionales y tienen un valor por defecto.
     *
     * Parametros:
     * nombre - Nombre del esqueleto (por defecto "Esqueleto").
     * vida - Vida inicial del esqueleto (por defecto 20).
     * x - Posicion en X (por defecto 0).
     * y - Posicion en Y (por defecto 64).
     * z - Posicion en Z (por defecto 0).
     *
     * Retorno:
     * Una respuesta con el nombre, vida, si esta vivo, flechas y posicion del esqueleto,
     * o una respuesta de error con el mensaje si los datos no son validos.
     */
    @GetMapping(ApiPaths.ESQUELETO)
    public ResponseEntity<Map<String, Object>> crear(
            @RequestParam(defaultValue = "Esqueleto") String nombre,
            @RequestParam(defaultValue = "20") int vida,
            @RequestParam(defaultValue = "0") double x,
            @RequestParam(defaultValue = "64") double y,
            @RequestParam(defaultValue = "0") double z) {

        try {
            Esqueleto esqueleto = esqueletoService.crear(nombre, vida, x, y, z);

            return ResponseEntity.ok(Map.of(
                    "nombre", esqueleto.getNombre(),
                    "vida", esqueleto.getVida(),
                    "vivo", esqueleto.estaVivo(),
                    "flechas", esqueleto.getFlechas(),
                    "posicion", Map.of("x", esqueleto.getX(), "y", esqueleto.getY(), "z", esqueleto.getZ())
            ));
        } catch (DatosInvalidosException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
