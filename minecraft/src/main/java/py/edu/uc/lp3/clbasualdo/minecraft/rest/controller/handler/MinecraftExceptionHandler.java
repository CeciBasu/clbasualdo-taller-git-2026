package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller.handler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperMuertoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperYaDetonadoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.MinecraftException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RecursoNoEncontradoException;

/**
 * Descripcion:
 * Handler HTTP de las excepciones del dominio.
 *
 * Responsabilidad:
 * Traducir cada tipo de excepcion a un codigo HTTP distinto y a un JSON comun.
 * No captura excepciones genericas para no ocultar errores de programacion.
 */
@RestControllerAdvice
public class MinecraftExceptionHandler {

    /**
     * Descripcion:
     * Responde HTTP 404 cuando el recurso pedido no existe.
     *
     * Parametros:
     * ex - La excepcion de dominio.
     *
     * Retorno:
     * Respuesta JSON con el detalle del error.
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex);
    }

    /**
     * Descripcion:
     * Responde HTTP 400 cuando los datos de entrada rompen un invariante.
     *
     * Parametros:
     * ex - La excepcion de dominio.
     *
     * Retorno:
     * Respuesta JSON con el detalle del error.
     */
    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<Map<String, Object>> manejarDatosInvalidos(DatosInvalidosException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex);
    }

    /**
     * Descripcion:
     * Responde HTTP 422 cuando se intenta detonar un Creeper muerto.
     *
     * Parametros:
     * ex - La excepcion de dominio.
     *
     * Retorno:
     * Respuesta JSON con el detalle del error.
     */
    @ExceptionHandler(CreeperMuertoException.class)
    public ResponseEntity<Map<String, Object>> manejarCreeperMuerto(CreeperMuertoException ex) {
        return respuesta(HttpStatus.UNPROCESSABLE_ENTITY, ex);
    }

    /**
     * Descripcion:
     * Responde HTTP 409 cuando se intenta detonar un Creeper que ya exploto.
     *
     * Parametros:
     * ex - La excepcion de dominio.
     *
     * Retorno:
     * Respuesta JSON con el detalle del error.
     */
    @ExceptionHandler(CreeperYaDetonadoException.class)
    public ResponseEntity<Map<String, Object>> manejarCreeperYaDetonado(CreeperYaDetonadoException ex) {
        return respuesta(HttpStatus.CONFLICT, ex);
    }

    /**
     * Descripcion:
     * Arma el cuerpo JSON comun de los errores de dominio.
     *
     * Parametros:
     * status - Codigo HTTP que corresponde al error.
     * ex - La excepcion de dominio.
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
