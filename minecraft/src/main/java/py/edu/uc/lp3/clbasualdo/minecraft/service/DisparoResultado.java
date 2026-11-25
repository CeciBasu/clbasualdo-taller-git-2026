package py.edu.uc.lp3.clbasualdo.minecraft.service;

/**
 * Descripción:
 * Record que guarda el resultado de pedirle al esqueleto los dos disparos de la
 * sobrecarga de disparar().
 *
 * Responsabilidad:
 * Llevar el resultado ya calculado desde la capa de servicio hacia el controller,
 * para que el controller no tenga que preguntar por las flechas ni por el alcance.
 */
public record DisparoResultado(
        /** Nombre del esqueleto que disparó. */
        String nombre,
        /** Flechas que tenía antes de los dos disparos. */
        int flechasIniciales,
        /** Flechas que le quedan después de los dos disparos. */
        int flechasRestantes,
        /** Distancia máxima a la que el esqueleto puede disparar. */
        double rango,
        /** Distancia que se le pidió para el segundo disparo. */
        double distanciaPedida,
        /** Texto que devolvió disparar(), la versión sin distancia. */
        String disparoSinDistancia,
        /** Texto que devolvió disparar(double), la versión con distancia. */
        String disparoConDistancia) {
}
