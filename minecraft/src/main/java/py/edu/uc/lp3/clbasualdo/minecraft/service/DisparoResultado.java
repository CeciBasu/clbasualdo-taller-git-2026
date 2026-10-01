package py.edu.uc.lp3.clbasualdo.minecraft.service;

/**
 * Descripcion:
 * Record que guarda el resultado de pedirle al esqueleto los dos disparos de la
 * sobrecarga de disparar().
 *
 * Responsabilidad:
 * Llevar el resultado ya calculado desde la capa de servicio hacia el controller,
 * para que el controller no tenga que preguntar por las flechas ni por el alcance.
 */
public record DisparoResultado(
        /** Nombre del esqueleto que disparo. */
        String nombre,
        /** Flechas que tenia antes de los dos disparos. */
        int flechasIniciales,
        /** Flechas que le quedan despues de los dos disparos. */
        int flechasRestantes,
        /** Distancia maxima a la que el esqueleto puede disparar. */
        double rango,
        /** Distancia que se le pidio para el segundo disparo. */
        double distanciaPedida,
        /** Texto que devolvio disparar(), la version sin distancia. */
        String disparoSinDistancia,
        /** Texto que devolvio disparar(double), la version con distancia. */
        String disparoConDistancia) {
}
