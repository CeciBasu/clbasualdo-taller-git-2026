package py.edu.uc.lp3.clbasualdo.minecraft.service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Descripción:
 * Resultado completo de una simulación de explosión.
 *
 * Responsabilidad:
 * Llevar al controller los datos del Creeper guardado que detonó, de las entidades
 * guardadas que alcanzó y de las que quedaron fuera del radio, junto con el id y la
 * fecha con los que quedó guardada la simulación en H2.
 *
 * Parámetros:
 * simulacionId - Id de la simulación persistida.
 * fecha - Fecha en que se guardó la simulación.
 * radioExplosion - Radio de la explosión del Creeper.
 * creeper - Estado del Creeper antes y después de detonar.
 * afectados - Entidades que estaban dentro del radio.
 * noAfectados - Entidades vivas que estaban fuera del radio.
 */
public record SimulacionExplosionResultado(
        Long simulacionId,
        LocalDateTime fecha,
        double radioExplosion,
        EstadoCreeper creeper,
        List<EstadoEntidad> afectados,
        List<EstadoEntidad> noAfectados) {

    /**
     * Descripción:
     * Estado del Creeper antes y después de la explosión.
     *
     * Parámetros:
     * id - Id del Creeper en la tabla "entidades".
     * nombre - Nombre del Creeper.
     * vidaInicial - Vida que tenía antes de detonar.
     * vidaFinal - Vida después de detonar (siempre 0).
     * detonado - Si quedó detonado (siempre true).
     * vivo - Si sigue vivo (siempre false).
     * x - Posición en X.
     * y - Posición en Y.
     * z - Posición en Z.
     */
    public record EstadoCreeper(
            Long id,
            String nombre,
            int vidaInicial,
            int vidaFinal,
            boolean detonado,
            boolean vivo,
            double x,
            double y,
            double z) {
    }

    /**
     * Descripción:
     * Estado de una entidad guardada que estaba viva al momento de la explosión.
     *
     * Parámetros:
     * id - Id de la entidad en la tabla "entidades".
     * tipo - Subtipo de la entidad.
     * nombre - Nombre de la entidad.
     * vidaInicial - Vida antes de la explosión.
     * vidaFinal - Vida después de la explosión.
     * distanciaAlCreeper - Distancia al Creeper, en bloques.
     * alcanzada - Si quedó dentro del radio.
     */
    public record EstadoEntidad(
            Long id,
            String tipo,
            String nombre,
            int vidaInicial,
            int vidaFinal,
            double distanciaAlCreeper,
            boolean alcanzada) {
    }
}
