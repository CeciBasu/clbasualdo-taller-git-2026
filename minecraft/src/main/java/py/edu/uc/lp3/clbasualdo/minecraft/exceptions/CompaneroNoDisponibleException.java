package py.edu.uc.lp3.clbasualdo.minecraft.exceptions;

/**
 * Descripción:
 * El servicio de un compañero no respondió o falló por su cuenta (5xx, conexión rechazada,
 * tiempo agotado).
 *
 * Responsabilidad:
 * Distinguir «la regla rechazó» de «la API del compañero falló», y decir de quién fue. El
 * handler la traduce a 503 y la base queda igual.
 */
public class CompaneroNoDisponibleException extends CompaneroException {

    /**
     * Descripción:
     * Crea el error de un compañero que no está disponible.
     *
     * Parámetros:
     * rol - "A2" o "A3".
     * companero - Nombre del compañero.
     * url - URL que se intentó.
     * estadoCompanero - Código HTTP 5xx, o null si no hubo respuesta.
     * respuestaCompanero - Cuerpo recibido, o null.
     * causa - Error original.
     */
    public CompaneroNoDisponibleException(String rol, String companero, String url, Integer estadoCompanero,
            String respuestaCompanero, Throwable causa) {
        super("La API de " + rol + " (" + companero + ") no está disponible en " + url
                + ": la jugada no se guardó.", rol, companero, estadoCompanero, respuestaCompanero, causa);
    }
}
