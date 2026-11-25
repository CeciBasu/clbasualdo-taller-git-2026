package py.edu.uc.lp3.clbasualdo.minecraft.exceptions;

/**
 * Descripción:
 * Excepción que se lanza cuando se busca o se intenta borrar un recurso que no
 * existe en la base de datos.
 *
 * Responsabilidad:
 * Distinguir un id inexistente de un dato inválido o de una regla de juego
 * incumplida, para que el handler HTTP responda 404 y no 400.
 */
public class RecursoNoEncontradoException extends MinecraftException {

    /**
     * Descripción:
     * Crea el error con el mensaje del dominio.
     *
     * Parámetros:
     * mensaje - Explicación de lo que no se encontró.
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Descripción:
     * Crea el error con su mensaje y la causa original.
     *
     * Parámetros:
     * mensaje - Explicación de lo que no se encontró.
     * causa - El error que lo provocó.
     */
    public RecursoNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
