package py.edu.uc.lp3.clbasualdo.minecraft.dto;

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
public record EntidadRequest(
        String tipo,
        String nombre,
        Integer vida,
        Integer vidaMaxima,
        Double x,
        Double y,
        Double z,
        Boolean detonado,
        Boolean puertaRota,
        Integer nivel,
        Integer experiencia) {
}
