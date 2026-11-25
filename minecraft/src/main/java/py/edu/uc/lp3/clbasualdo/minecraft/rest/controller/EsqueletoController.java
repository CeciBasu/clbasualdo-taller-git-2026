package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.edu.uc.lp3.clbasualdo.minecraft.constants.ApiPaths;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Esqueleto;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.service.DisparoResultado;
import py.edu.uc.lp3.clbasualdo.minecraft.service.EsqueletoService;

/**
 * Descripción:
 * Controlador que permite crear un esqueleto desde la API y ver como funciona la
 * sobrecarga de disparar().
 *
 * Responsabilidad:
 * Se encarga de recibir los datos por la dirección, pedirle al servicio que cree
 * el esqueleto y devolver su información o un mensaje de error si los datos no sirven.
 * Las reglas del juego quedan en el dominio: el controller solo arma la respuesta.
 */
@RestController
@Tag(name = "Modelo", description = "Rutas de POO-06 que muestran el modelo de dominio sin guardar en H2.")
public class EsqueletoController {

    /** Servicio que arma el esqueleto con las reglas del dominio. */
    private final EsqueletoService esqueletoService;

    /**
     * Descripción:
     * Crea el controlador con el servicio que va a usar.
     *
     * Parámetros:
     * esqueletoService - El servicio del esqueleto.
     */
    public EsqueletoController(EsqueletoService esqueletoService) {
        this.esqueletoService = esqueletoService;
    }

    /**
     * Descripción:
     * Responde a la ruta del esqueleto. Le pasa los datos al servicio, que crea el
     * esqueleto y lo ubica en la posición pedida, y devuelve sus datos.
     * Si algún dato no es válido (por ejemplo una vida de 0), devuelve un error 400 con el mensaje.
     * Todos los parámetros son opcionales y tienen un valor por defecto.
     *
     * Parámetros:
     * nombre - Nombre del esqueleto (por defecto "Esqueleto").
     * vida - Vida inicial del esqueleto (por defecto 20).
     * x - Posición en X (por defecto 0).
     * y - Posición en Y (por defecto 64).
     * z - Posición en Z (por defecto 0).
     *
     * Retorno:
     * Una respuesta con el nombre, vida, si está vivo, flechas y posición del esqueleto,
     * o una respuesta de error con el mensaje si los datos no son válidos.
     */
    @Operation(summary = "Construye un Esqueleto desde la URL", description = "Con vida 0 o nombre vacío responde 400.")
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

    /**
     * Descripción:
     * Responde a la ruta de disparo. Le pasa los datos al servicio, que construye el
     * esqueleto con el constructor y después ejecuta las dos versiones de disparar():
     * una sin distancia y otra con la distancia que viene en la URL. Se devuelve el
     * texto de cada una, para que se vea en el JSON que es la misma acción
     * sobrecargada con distinto contexto.
     * Si algún dato no es válido (por ejemplo una vida de 0 o una distancia negativa),
     * devuelve un error 400 con el mensaje del dominio.
     * Todos los parámetros son opcionales y tienen un valor por defecto.
     *
     * Parámetros:
     * nombre - Nombre del esqueleto (por defecto "Bony").
     * vida - Vida inicial del esqueleto (por defecto 20).
     * x - Posición en X (por defecto 0).
     * y - Posición en Y (por defecto 64).
     * z - Posición en Z (por defecto 0).
     * distancia - Distancia al objetivo para disparar(double) (por defecto 5).
     *
     * Retorno:
     * Una respuesta con las dos variantes del disparo, las flechas antes y después, y
     * el rango del esqueleto; o una respuesta de error con el mensaje si los datos no
     * son válidos.
     */
    @Operation(summary = "Muestra la sobrecarga de disparar()", description = "Ejecuta disparar() y disparar(distancia). Una distancia negativa responde 400.")
    @GetMapping(ApiPaths.ESQUELETO_DISPARAR)
    public ResponseEntity<Object> disparar(
            @RequestParam(defaultValue = "Bony") String nombre,
            @RequestParam(defaultValue = "20") int vida,
            @RequestParam(defaultValue = "0") double x,
            @RequestParam(defaultValue = "64") double y,
            @RequestParam(defaultValue = "0") double z,
            @RequestParam(defaultValue = "5") double distancia) {

        try {
            DisparoResultado resultado = esqueletoService.disparar(nombre, vida, x, y, z, distancia);
            return ResponseEntity.ok(resultado);
        } catch (DatosInvalidosException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
