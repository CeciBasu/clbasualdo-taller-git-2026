package py.edu.uc.lp3.clbasualdo.minecraft.exceptions;

/**
 * Descripcion:
 * Excepcion que se lanza cuando se intenta detonar un Creeper que ya exploto.
 *
 * Responsabilidad:
 * Avisar que se violo la regla de que un Creeper solo puede detonar una vez.
 */
public class CreeperYaDetonadoException extends MinecraftException {

    /**
     * Descripcion:
     * Crea el error con el mensaje del dominio.
     *
     * Parametros:
     * mensaje - Explicacion de lo que salio mal.
     */
    public CreeperYaDetonadoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Descripcion:
     * Crea el error con su mensaje y la causa original.
     *
     * Parametros:
     * mensaje - Explicacion de lo que salio mal.
     * causa - El error que lo provoco.
     */
    public CreeperYaDetonadoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
