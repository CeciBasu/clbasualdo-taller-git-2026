package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import py.edu.uc.lp3.clbasualdo.minecraft.dto.ErrorRespuesta;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CompaneroException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CompaneroNoDisponibleException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperMuertoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RechazoDeCompaneroException;
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
    public ResponseEntity<ErrorRespuesta> manejarNoEncontrado(RecursoNoEncontradoException ex) {
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
    public ResponseEntity<ErrorRespuesta> manejarDatosInvalidos(DatosInvalidosException ex) {
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
    public ResponseEntity<ErrorRespuesta> manejarCreeperMuerto(CreeperMuertoException ex) {
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
    public ResponseEntity<ErrorRespuesta> manejarCreeperYaDetonado(CreeperYaDetonadoException ex) {
        return respuesta(HttpStatus.CONFLICT, ex);
    }

    /**
     * Descripción:
     * La regla de A2 o de A3 rechazó la jugada. Se responde 422 con el rol y el nombre del
     * compañero, y con su código y su cuerpo originales, para que se vea de quién fue el rechazo.
     *
     * Parámetros:
     * ex - Rechazo atribuido a un compañero.
     *
     * Retorno:
     * Respuesta 422.
     */
    @ExceptionHandler(RechazoDeCompaneroException.class)
    public ResponseEntity<ErrorRespuesta> manejarRechazoDeCompanero(RechazoDeCompaneroException ex) {
        return respuesta(HttpStatus.UNPROCESSABLE_ENTITY, ex);
    }

    /**
     * Descripción:
     * La API de A2 o de A3 no respondió o falló (5xx). Es distinto de un rechazo: la jugada
     * no se pudo validar. Se responde 503 con el rol del compañero que falló.
     *
     * Parámetros:
     * ex - Error atribuido a un compañero.
     *
     * Retorno:
     * Respuesta 503.
     */
    @ExceptionHandler(CompaneroNoDisponibleException.class)
    public ResponseEntity<ErrorRespuesta> manejarCompaneroNoDisponible(CompaneroNoDisponibleException ex) {
        return respuesta(HttpStatus.SERVICE_UNAVAILABLE, ex);
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
    public ResponseEntity<ErrorRespuesta> manejarJsonIlegible(HttpMessageNotReadableException ex) {
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
    public ResponseEntity<ErrorRespuesta> manejarParametroInvalido(MethodArgumentTypeMismatchException ex) {
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
    private ResponseEntity<ErrorRespuesta> respuesta(HttpStatus status, MinecraftException ex) {
        String rol = null;
        String companero = null;
        Integer estadoCompanero = null;
        String respuestaCompanero = null;
        if (ex instanceof CompaneroException deCompanero) {
            rol = deCompanero.getRol();
            companero = deCompanero.getCompanero();
            estadoCompanero = deCompanero.getEstadoCompanero();
            respuestaCompanero = deCompanero.getRespuestaCompanero();
        }
        ErrorRespuesta cuerpo = new ErrorRespuesta(LocalDateTime.now(), status.value(),
                status.getReasonPhrase(), ex.getClass().getSimpleName(), ex.getMessage(),
                rol, companero, estadoCompanero, respuestaCompanero);
        return ResponseEntity.status(status).body(cuerpo);
    }
}
