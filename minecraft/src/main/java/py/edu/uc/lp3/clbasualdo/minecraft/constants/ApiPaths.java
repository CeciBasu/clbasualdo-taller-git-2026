package py.edu.uc.lp3.clbasualdo.minecraft.constants;

/**
 * Descripcion:
 * Clase estatica que centraliza las rutas que expone la API REST.
 *
 * Responsabilidad:
 * Guardar en un solo lugar los caminos de los controladores, para no repetir
 * textos sueltos por todo el codigo.
 */
public final class ApiPaths {
    /** Ruta de la pagina de inicio. */
    public static final String INDEX = "/";
    /** Ruta para crear un esqueleto. */
    public static final String ESQUELETO = "/esqueleto";
    /** Ruta para ver las dos versiones del mensaje disparar(). */
    public static final String ESQUELETO_DISPARAR = "/esqueleto/disparar";
    /** Ruta que muestra la reaccion de cada tipo de entidad. */
    public static final String COMPORTAMIENTO = "/comportamiento";

    private ApiPaths() {
    }
}
