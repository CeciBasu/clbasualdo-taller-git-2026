package py.edu.uc.lp3.clbasualdo.minecraft.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Descripción:
 * Cuerpo JSON del escenario «emboscada del Creeper».
 *
 * Responsabilidad:
 * Indicar qué Creeper guardado embosca a qué Jugador guardado.
 *
 * Parámetros:
 * creeperId - Id del Creeper en /entidades.
 * jugadorId - Id del Jugador en /entidades.
 */
@Schema(description = "Creeper y Jugador guardados que participan de la emboscada.")
public record EmboscadaRequest(
        @Schema(description = "Id del Creeper en /entidades.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long creeperId,
        @Schema(description = "Id del Jugador en /entidades.", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        Long jugadorId,
        @Schema(description = "Segundos transcurridos desde que se activó la mecha; no negativo.",
                example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer tiempoTranscurrido) {
}
