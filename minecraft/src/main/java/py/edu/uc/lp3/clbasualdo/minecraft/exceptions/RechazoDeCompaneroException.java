package py.edu.uc.lp3.clbasualdo.minecraft.exceptions;

/**
 * Descripción:
 * La regla de un compañero rechazó la jugada (su API respondió 4xx).
 *
 * Responsabilidad:
 * Cortar el escenario antes de guardar nada y decir qué compañero lo rechazó. El handler
 * la traduce a 422 con el rol, el nombre y la respuesta original del compañero.
 */
public class RechazoDeCompaneroException extends CompaneroException {

    /**
     * Descripción:
     * Crea el rechazo atribuido a un compañero.
     *
     * Parámetros:
     * rol - "A2" o "A3".
     * companero - Nombre del compañero.
     * regla - Regla del compañero que se consultó.
     * estadoCompanero - Código HTTP que devolvió.
     * respuestaCompanero - Cuerpo que devolvió.
     */
    public RechazoDeCompaneroException(String rol, String companero, String regla, int estadoCompanero,
            String respuestaCompanero) {
        super("Rechazo de " + rol + " (" + companero + ", regla " + regla + "): la jugada no se guardó.",
                rol, companero, estadoCompanero, respuestaCompanero, null);
    }
}
