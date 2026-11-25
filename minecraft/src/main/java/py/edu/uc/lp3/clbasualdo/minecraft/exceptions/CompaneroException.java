package py.edu.uc.lp3.clbasualdo.minecraft.exceptions;

/**
 * Descripción:
 * Base de los errores que vienen del servicio de un compañero (A2 o A3).
 *
 * Responsabilidad:
 * Guardar de quién fue el error (rol y nombre), qué código HTTP devolvió y qué cuerpo mandó,
 * para que el handler pueda atribuirlo sin perder la respuesta original.
 */
public abstract class CompaneroException extends MinecraftException {

    /** Rol del compañero en el grupo: "A2" o "A3". */
    private final String rol;

    /** Nombre del compañero. */
    private final String companero;

    /** Código HTTP que devolvió el compañero, o null si no respondió. */
    private final Integer estadoCompanero;

    /** Cuerpo que devolvió el compañero, tal cual, o null si no respondió. */
    private final String respuestaCompanero;

    /**
     * Descripción:
     * Crea el error atribuido a un compañero.
     *
     * Parámetros:
     * mensaje - Explicación del error.
     * rol - "A2" o "A3".
     * companero - Nombre del compañero.
     * estadoCompanero - Código HTTP que devolvió, o null.
     * respuestaCompanero - Cuerpo que devolvió, o null.
     * causa - Error original, o null.
     */
    protected CompaneroException(String mensaje, String rol, String companero, Integer estadoCompanero,
            String respuestaCompanero, Throwable causa) {
        super(mensaje, causa);
        this.rol = rol;
        this.companero = companero;
        this.estadoCompanero = estadoCompanero;
        this.respuestaCompanero = respuestaCompanero;
    }

    /** @return el rol del compañero ("A2" o "A3"). */
    public String getRol() {
        return rol;
    }

    /** @return el nombre del compañero. */
    public String getCompanero() {
        return companero;
    }

    /** @return el código HTTP que devolvió el compañero, o null. */
    public Integer getEstadoCompanero() {
        return estadoCompanero;
    }

    /** @return el cuerpo que devolvió el compañero, o null. */
    public String getRespuestaCompanero() {
        return respuestaCompanero;
    }
}
