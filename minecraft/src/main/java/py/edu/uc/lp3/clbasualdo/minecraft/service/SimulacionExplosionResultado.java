package py.edu.uc.lp3.clbasualdo.minecraft.service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Descripcion:
 * Resultado completo de una simulacion de explosion.
 *
 * Responsabilidad:
 * Llevar al controller los datos del Creeper, de las entidades alcanzadas y de
 * las que quedaron fuera del radio, junto con el id y la fecha con los que quedo
 * guardada la simulacion en H2.
 */
public record SimulacionExplosionResultado(
        /** Id de la simulacion persistida. */
        Long simulacionId,
        /** Fecha en que se guardo la simulacion. */
        LocalDateTime fecha,
        /** Radio de la explosion del Creeper. */
        double radioExplosion,
        /** Estado final del Creeper que exploto. */
        EstadoCreeper creeper,
        /** Entidades que estaban dentro del radio de la explosion. */
        List<EstadoEntidad> afectados,
        /** Entidades que estaban fuera del radio de la explosion. */
        List<EstadoEntidad> noAfectados) {

    /**
     * Descripcion:
     * Estado del Creeper antes y despues de la explosion.
     */
    public record EstadoCreeper(
            /** Nombre del Creeper. */
            String nombre,
            /** Vida que tenia antes de detonar. */
            int vidaInicial,
            /** Vida que tiene despues de detonar. */
            int vidaFinal,
            /** Indica si el Creeper ya detono. */
            boolean detonado,
            /** Indica si el Creeper sigue vivo. */
            boolean vivo,
            /** Posicion en X. */
            double x,
            /** Posicion en Y. */
            double y,
            /** Posicion en Z. */
            double z) {
    }

    /**
     * Descripcion:
     * Estado de una entidad que participo de la simulacion.
     */
    public record EstadoEntidad(
            /** Tipo concreto de la entidad. */
            String tipo,
            /** Nombre de la entidad. */
            String nombre,
            /** Vida que tenia antes de la explosion. */
            int vidaInicial,
            /** Vida que tiene despues de la explosion. */
            int vidaFinal,
            /** Distancia que la separaba del Creeper. */
            double distanciaAlCreeper,
            /** Indica si estaba dentro del radio de la explosion. */
            boolean alcanzada) {
    }
}
