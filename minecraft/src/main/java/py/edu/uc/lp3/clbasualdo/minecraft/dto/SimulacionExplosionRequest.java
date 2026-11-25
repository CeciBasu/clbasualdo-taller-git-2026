package py.edu.uc.lp3.clbasualdo.minecraft.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Descripción:
 * Cuerpo JSON del pedido de simulación de explosión.
 *
 * Responsabilidad:
 * Indicar qué Creeper guardado en H2 va a detonar.
 *
 * Parámetros:
 * creeperId - Id del Creeper en la tabla "entidades".
 */
@Schema(description = "Creeper guardado que va a detonar.")
public record SimulacionExplosionRequest(
        @Schema(description = "Id del Creeper en /entidades.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long creeperId) {
}
