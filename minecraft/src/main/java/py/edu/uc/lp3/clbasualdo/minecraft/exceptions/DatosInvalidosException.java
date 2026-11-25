package py.edu.uc.lp3.clbasualdo.minecraft.exceptions;

/**
 * Descripción:
 * Excepción que se lanza cuando los datos que llegan a la API no son válidos
 * para el dominio (por ejemplo, una vida de 0).
 *
 * Responsabilidad:
 * Avisar que el problema viene de la entrada del usuario, para que el
 * controlador pueda responder un error 400.
 */
public class DatosInvalidosException extends MinecraftException {

    /**
     * Descripción:
     * Crea el error con el mensaje del dominio.
     *
     * Parámetros:
     * mensaje - Explicación de lo que salió mal.
     */
    public DatosInvalidosException(String mensaje) {
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
    public DatosInvalidosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
