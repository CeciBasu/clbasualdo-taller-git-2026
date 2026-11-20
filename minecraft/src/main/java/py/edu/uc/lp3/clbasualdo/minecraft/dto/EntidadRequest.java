package py.edu.uc.lp3.clbasualdo.minecraft.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Descripción:
 * Cuerpo JSON que recibe el CRUD para crear o reemplazar una entidad.
 *
 * Responsabilidad:
 * Traer los datos tal como llegan del cliente, con tipos que admiten null para que el
 * service distinga «no vino» de «vino 0». El controller no arma la Entity directamente:
 * el service valida cada dato y la combinación antes de construirla.
 *
 * Parámetros:
 * tipo - Subtipo: "Creeper", "Zombie" o "Jugador". Obligatorio.
 * nombre - Nombre único. Obligatorio.
 * vida - Vida actual. Obligatoria.
 * vidaMaxima - Vida máxima. Si no viene, se toma igual a la vida.
 * x - Posición en X. Obligatoria.
 * y - Posición en Y. Obligatoria.
 * z - Posición en Z. Obligatoria.
 * detonado - Solo Creeper: si ya explotó. Por defecto false.
 * puertaRota - Solo Zombie: si ya rompió una puerta. Por defecto false.
 * nivel - Solo Jugador: nivel. Por defecto 0.
 * experiencia - Solo Jugador: experiencia acumulada (0 a 99). Por defecto 0.
 */
@Schema(description = "Datos para crear o reemplazar una entidad. El service rechaza combinaciones "
        + "imposibles: un Creeper detonado tiene vida 0 y no se crea ni se detona con este cuerpo.")
public record EntidadRequest(
        @Schema(description = "Subtipo.", allowableValues = {"Creeper", "Zombie", "Jugador"},
                example = "Creeper", requiredMode = Schema.RequiredMode.REQUIRED)
        String tipo,
        @Schema(description = "Nombre único.", example = "Creeper del pozo", requiredMode = Schema.RequiredMode.REQUIRED)
        String nombre,
        @Schema(description = "Vida actual. Mayor a 0 al crear; entre 0 y vidaMaxima.", example = "20",
                requiredMode = Schema.RequiredMode.REQUIRED)
        Integer vida,
        @Schema(description = "Vida máxima. Si no viene, es igual a vida.", example = "20")
        Integer vidaMaxima,
        @Schema(description = "Posición X.", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        Double x,
        @Schema(description = "Posición Y (altura).", example = "64", requiredMode = Schema.RequiredMode.REQUIRED)
        Double y,
        @Schema(description = "Posición Z.", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        Double z,
        @Schema(description = "Solo Creeper. Si ya detonó; solo lo cambia la simulación.", example = "false")
        Boolean detonado,
        @Schema(description = "Solo Zombie. Si ya rompió una puerta.", example = "false")
        Boolean puertaRota,
        @Schema(description = "Solo Jugador. Nivel, 0 o más.", example = "1")
        Integer nivel,
        @Schema(description = "Solo Jugador. Experiencia acumulada, de 0 a 99.", example = "0")
        Integer experiencia) {
}
