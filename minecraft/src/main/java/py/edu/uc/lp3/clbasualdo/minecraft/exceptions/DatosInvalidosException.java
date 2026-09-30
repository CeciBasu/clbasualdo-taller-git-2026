package py.edu.uc.lp3.clbasualdo.minecraft.exceptions;

/**
 * Descripcion:
 * Excepcion que se lanza cuando los datos que llegan a la API no son validos
 * para el dominio (por ejemplo, una vida de 0).
 *
 * Responsabilidad:
 * Avisar que el problema viene de la entrada del usuario, para que el
 * controlador pueda responder un error 400.
 */
public class DatosInvalidosException extends MinecraftException {

    /**
     * Descripcion:
     * Crea el error con el mensaje del dominio.
     *
     * Parametros:
     * mensaje - Explicacion de lo que salio mal.
     */
    public DatosInvalidosException(String mensaje) {
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
    public DatosInvalidosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
