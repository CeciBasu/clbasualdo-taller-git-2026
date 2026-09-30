package py.edu.uc.lp3.clbasualdo.minecraft.exceptions;

/**
 * Descripcion:
 * Excepcion base de la aplicacion. Todas las excepciones propias heredan de aca.
 *
 * Responsabilidad:
 * Servir como tipo comun para agrupar los errores del juego y poder atraparlos
 * todos juntos cuando haga falta.
 */
public class MinecraftException extends RuntimeException {

    /**
     * Descripcion:
     * Crea el error con su mensaje.
     *
     * Parametros:
     * mensaje - Explicacion de lo que salio mal.
     */
    public MinecraftException(String mensaje) {
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
    public MinecraftException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
