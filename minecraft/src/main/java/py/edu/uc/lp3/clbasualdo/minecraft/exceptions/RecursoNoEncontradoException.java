package py.edu.uc.lp3.clbasualdo.minecraft.exceptions;

/**
 * Descripcion:
 * Excepcion que se lanza cuando se busca o se intenta borrar un recurso que no
 * existe en la base de datos.
 *
 * Responsabilidad:
 * Distinguir un id inexistente de un dato invalido o de una regla de juego
 * incumplida, para que el handler HTTP responda 404 y no 400.
 */
public class RecursoNoEncontradoException extends MinecraftException {

    /**
     * Descripcion:
     * Crea el error con el mensaje del dominio.
     *
     * Parametros:
     * mensaje - Explicacion de lo que no se encontro.
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Descripcion:
     * Crea el error con su mensaje y la causa original.
     *
     * Parametros:
     * mensaje - Explicacion de lo que no se encontro.
     * causa - El error que lo provoco.
     */
    public RecursoNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
