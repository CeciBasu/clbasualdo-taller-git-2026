package py.edu.uc.lp3.clbasualdo.minecraft.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Descripción:
 * Respuesta 200 de una regla que aceptó el estado pedido.
 *
 * Responsabilidad:
 * Decirle al cliente qué regla se aplicó y por qué aceptó. Cuando la regla rechaza, la
 * respuesta es un ErrorRespuesta con su código HTTP.
 *
 * Parámetros:
 * regla - Identificador de la regla.
 * aceptada - Siempre true en una respuesta 200.
 * mensaje - Explicación para una persona.
 */
@Schema(description = "La regla aceptó el estado pedido.")
public record ValidacionReglaResponse(
        @Schema(description = "Identificador de la regla.", example = "creeper-vivo")
        String regla,
        @Schema(description = "Siempre true en una respuesta 200.", example = "true")
        boolean aceptada,
        @Schema(description = "Explicación.", example = "Creeper del pozo está vivo: puede detonar.")
        String mensaje) {
}
