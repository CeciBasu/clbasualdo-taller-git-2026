package py.edu.uc.lp3.clbasualdo.minecraft.exceptions;

/**
 * Descripción:
 * Excepción que se lanza cuando se intenta detonar un Creeper que ya explotó.
 *
 * Responsabilidad:
 * Avisar que se violó la regla de que un Creeper solo puede detonar una vez.
 */
public class CreeperYaDetonadoException extends MinecraftException {

    /**
     * Descripción:
     * Crea el error con el mensaje del dominio.
     *
     * Parámetros:
     * mensaje - Explicación de lo que salió mal.
     */
    public CreeperYaDetonadoException(String mensaje) {
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
    public CreeperYaDetonadoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
