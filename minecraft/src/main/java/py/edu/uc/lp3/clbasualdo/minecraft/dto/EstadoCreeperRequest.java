package py.edu.uc.lp3.clbasualdo.minecraft.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Descripción:
 * Estado de un Creeper que un cliente quiere validar contra una de las dos reglas.
 *
 * Responsabilidad:
 * Traer solo los datos que miran las reglas. No se guarda nada: las rutas de /reglas
 * responden si el Creeper podría detonar, sin tocar H2.
 *
 * Parámetros:
 * nombre - Nombre del Creeper, para el mensaje. Si no viene, se usa "Creeper".
 * vida - Vida actual. Obligatoria en /reglas/creeper-vivo.
 * detonado - Si ya detonó. Obligatorio en /reglas/detonacion-unica.
 */
@Schema(description = "Estado del Creeper que se quiere validar. No se guarda.")
public record EstadoCreeperRequest(
        @Schema(description = "Nombre del Creeper, solo para el mensaje.", example = "Creeper del pozo")
        String nombre,
        @Schema(description = "Vida actual. Obligatoria en /reglas/creeper-vivo.", example = "20")
        Integer vida,
        @Schema(description = "Si ya detonó. Obligatorio en /reglas/detonacion-unica.", example = "false")
        Boolean detonado) {
}
