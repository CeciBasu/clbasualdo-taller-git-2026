package py.edu.uc.lp3.clbasualdo.minecraft.exceptions;

/**
 * Descripcion:
 * Excepcion que se lanza cuando se intenta detonar un Creeper que ya esta muerto.
 *
 * Responsabilidad:
 * Avisar que se violo la regla de que un Creeper solo puede detonar si sigue vivo.
 */
public class CreeperMuertoException extends MinecraftException {

    /**
     * Descripcion:
     * Crea el error con el mensaje del dominio.
     *
     * Parametros:
     * mensaje - Explicacion de lo que salio mal.
     */
    public CreeperMuertoException(String mensaje) {
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
    public CreeperMuertoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
