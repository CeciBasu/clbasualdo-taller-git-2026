package py.edu.uc.lp3.clbasualdo.minecraft.exceptions;

/**
 * Descripción:
 * Excepción base de la aplicación. Todas las excepciones propias heredan de acá.
 *
 * Responsabilidad:
 * Servir como tipo común para agrupar los errores del juego y poder atraparlos
 * todos juntos cuando haga falta.
 */
public class MinecraftException extends RuntimeException {

    /**
     * Descripción:
     * Crea el error con su mensaje.
     *
     * Parámetros:
     * mensaje - Explicación de lo que salió mal.
     */
    public MinecraftException(String mensaje) {
        super(mensaje);
    }

    /**
     * Descripción:
     * Crea el error con su mensaje y la causa original.
     *
     * Parámetros:
     * mensaje - Explicación de lo que salió mal.
     * causa - El error que lo provocó.
     */
    public MinecraftException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
