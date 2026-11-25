package py.edu.uc.lp3.clbasualdo.minecraft.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = """
        Estado de un Creeper para validar las dos reglas a la vez. "saludCreeper" y "yaDetonado" \
        son opcionales; cualquier otro campo del cuerpo se ignora y no se guarda nada.""")
public record ValidacionExplosionRequest(
        @Schema(description = "Vida del Creeper. Si falta, se considera vivo.", example = "20")
        Integer saludCreeper,
        @Schema(description = "Si ya detonó. Si falta, se considera false.", example = "false")
        Boolean yaDetonado) {
}
