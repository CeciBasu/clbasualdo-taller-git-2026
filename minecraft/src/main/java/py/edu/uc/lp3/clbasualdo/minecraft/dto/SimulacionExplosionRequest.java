package py.edu.uc.lp3.clbasualdo.minecraft.dto;

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
public record SimulacionExplosionRequest(Long creeperId) {
}
