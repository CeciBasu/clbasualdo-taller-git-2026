package py.edu.uc.lp3.clbasualdo.minecraft.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado de la validación de las dos reglas de la explosión.")
public record ValidacionExplosionResponse(
        @Schema(description = "Siempre true en una respuesta 200.", example = "true")
        boolean aceptada,
        @Schema(description = "Explicación para la persona que consume la API.",
                example = "El Creeper está vivo y todavía no detonó.")
        String mensaje) {
}
