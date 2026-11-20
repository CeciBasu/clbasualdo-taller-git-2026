package py.edu.uc.lp3.clbasualdo.minecraft.dto;

import java.time.LocalDateTime;

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
 */
@Schema(description = "Forma común de todos los errores de la API.")
public record ErrorRespuesta(
        @Schema(description = "Momento del error.", example = "2026-11-19T21:10:02.512")
        LocalDateTime timestamp,
        @Schema(description = "Código HTTP.", example = "409")
        int status,
        @Schema(description = "Frase estándar del código HTTP.", example = "Conflict")
        String error,
        @Schema(description = "Excepción que lo provocó: DatosInvalidosException (400), "
                + "RecursoNoEncontradoException (404), CreeperYaDetonadoException (409), "
                + "CreeperMuertoException (422), RechazoDeCompaneroException (409) o "
                + "CompaneroNoDisponibleException (503).", example = "CreeperYaDetonadoException")
        String tipo,
        @Schema(description = "Explicación del error.", example = "Creeper 1 ya detonó y no puede volver a explotar.")
        String mensaje) {
}
