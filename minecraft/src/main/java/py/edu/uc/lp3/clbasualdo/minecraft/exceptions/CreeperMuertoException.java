package py.edu.uc.lp3.clbasualdo.minecraft.exceptions;

/**
 * Descripción:
 * Excepción que se lanza cuando se intenta detonar un Creeper que ya está muerto.
 *
 * Responsabilidad:
 * Avisar que se violó la regla de que un Creeper solo puede detonar si sigue vivo.
 */
public class CreeperMuertoException extends MinecraftException {

    /**
     * Descripción:
     * Crea el error con el mensaje del dominio.
     *
     * Parámetros:
     * mensaje - Explicación de lo que salió mal.
     */
    public CreeperMuertoException(String mensaje) {
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
    public CreeperMuertoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
