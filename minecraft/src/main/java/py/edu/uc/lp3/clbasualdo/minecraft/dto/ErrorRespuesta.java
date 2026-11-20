package py.edu.uc.lp3.clbasualdo.minecraft.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Descripción:
 * Cuerpo JSON de todas las respuestas de error de la API.
 *
 * Responsabilidad:
 * Darle a cualquier cliente (Insomnia o el servicio de un compañero) la misma forma para
 * todos los errores, con el nombre de la excepción en "tipo" para distinguir cada regla.
 *
 * Parámetros:
 * timestamp - Momento del error.
 * status - Código HTTP.
 * error - Frase estándar del código HTTP.
 * tipo - Nombre de la excepción.
 * mensaje - Explicación para una persona.
 * rechazadoPor - Solo en errores de un compañero: "A2" o "A3".
 * companero - Solo en errores de un compañero: su nombre.
 * estadoCompanero - Solo en errores de un compañero: el código HTTP que devolvió.
 * respuestaCompanero - Solo en errores de un compañero: el cuerpo que devolvió, tal cual.
 */
@Schema(description = "Forma común de todos los errores de la API.")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorRespuesta(
        @Schema(description = "Momento del error.", example = "2026-11-19T21:10:02.512")
        LocalDateTime timestamp,
        @Schema(description = "Código HTTP.", example = "409")
        int status,
        @Schema(description = "Frase estándar del código HTTP.", example = "Conflict")
        String error,
        @Schema(description = "Excepción que lo provocó: DatosInvalidosException (400), "
                + "RecursoNoEncontradoException (404), CreeperYaDetonadoException (409), "
                + "CreeperMuertoException (422), RechazoDeCompaneroException (422) o "
                + "CompaneroNoDisponibleException (503).", example = "CreeperYaDetonadoException")
        String tipo,
        @Schema(description = "Explicación del error.", example = "Creeper 1 ya detonó y no puede volver a explotar.")
        String mensaje,
        @Schema(description = "Solo si el error vino de un compañero: A2 o A3.", example = "A2")
        String rechazadoPor,
        @Schema(description = "Solo si el error vino de un compañero: su nombre.", example = "Irina Insfrán")
        String companero,
        @Schema(description = "Solo si el compañero respondió: su código HTTP.", example = "422")
        Integer estadoCompanero,
        @Schema(description = "Solo si el compañero respondió: su cuerpo, tal cual.")
        String respuestaCompanero) {
}
