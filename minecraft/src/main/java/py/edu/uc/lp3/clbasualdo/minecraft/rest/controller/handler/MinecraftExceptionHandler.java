package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller.handler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperMuertoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperYaDetonadoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.MinecraftException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RecursoNoEncontradoException;

/**
 * Descripción:
 * Handler HTTP de las excepciones del dominio.
 *
 * Responsabilidad:
 * Traducir cada tipo de excepción a un código HTTP distinto y a un JSON común.
 * No captura excepciones genéricas para no ocultar errores de programación.
 */
@RestControllerAdvice
public class MinecraftExceptionHandler {

    /**
     * Descripción:
     * Responde HTTP 404 cuando el recurso pedido no existe.
     *
     * Parámetros:
     * ex - La excepción de dominio.
     *
     * Retorno:
     * Respuesta JSON con el detalle del error.
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex);
    }

    /**
     * Descripción:
     * Responde HTTP 400 cuando los datos de entrada rompen un invariante.
     *
     * Parámetros:
     * ex - La excepción de dominio.
     *
     * Retorno:
     * Respuesta JSON con el detalle del error.
     */
    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<Map<String, Object>> manejarDatosInvalidos(DatosInvalidosException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex);
    }

    /**
     * Descripción:
     * Responde HTTP 422 cuando se intenta detonar un Creeper muerto.
     *
     * Parámetros:
     * ex - La excepción de dominio.
     *
     * Retorno:
     * Respuesta JSON con el detalle del error.
     */
    @ExceptionHandler(CreeperMuertoException.class)
    public ResponseEntity<Map<String, Object>> manejarCreeperMuerto(CreeperMuertoException ex) {
        return respuesta(HttpStatus.UNPROCESSABLE_ENTITY, ex);
    }

    /**
     * Descripción:
     * Responde HTTP 409 cuando se intenta detonar un Creeper que ya explotó.
     *
     * Parámetros:
     * ex - La excepción de dominio.
     *
     * Retorno:
     * Respuesta JSON con el detalle del error.
     */
    @ExceptionHandler(CreeperYaDetonadoException.class)
    public ResponseEntity<Map<String, Object>> manejarCreeperYaDetonado(CreeperYaDetonadoException ex) {
        return respuesta(HttpStatus.CONFLICT, ex);
    }

    /**
     * Descripción:
     * Un cuerpo JSON mal formado, o con un dato del tipo equivocado (por ejemplo "x": "abc"),
     * se responde igual que cualquier otro dato inválido: 400 con DatosInvalidosException,
     * en lugar del error genérico de Spring.
     *
     * Parámetros:
     * ex - Error de lectura del JSON.
     *
     * Retorno:
     * Respuesta 400 con el mismo formato que los demás errores.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> manejarJsonIlegible(HttpMessageNotReadableException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, new DatosInvalidosException(
                "El cuerpo JSON falta o no se puede leer: los campos tienen que tener el tipo correcto.", ex));
    }

    /**
     * Descripción:
     * Un parámetro de la URL con el tipo equivocado (por ejemplo /entidades/abc) es un dato
     * inválido: se responde 400 con DatosInvalidosException.
     *
     * Parámetros:
     * ex - Error de conversión del parámetro.
     *
     * Retorno:
     * Respuesta 400 con el mismo formato que los demás errores.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> manejarParametroInvalido(MethodArgumentTypeMismatchException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, new DatosInvalidosException(
                "El parámetro \"" + ex.getName() + "\" tiene un valor inválido: " + ex.getValue() + ".", ex));
    }

    /**
     * Descripción:
     * Arma el cuerpo JSON común de los errores de dominio.
     *
     * Parámetros:
     * status - Código HTTP que corresponde al error.
     * ex - La excepción de dominio.
     *
     * Retorno:
     * Respuesta con el status y los datos del error.
     */
    private ResponseEntity<Map<String, Object>> respuesta(HttpStatus status, MinecraftException ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("status", status.value());
        cuerpo.put("error", status.getReasonPhrase());
        cuerpo.put("tipo", ex.getClass().getSimpleName());
        cuerpo.put("mensaje", ex.getMessage());
        return ResponseEntity.status(status).body(cuerpo);
    }
}
