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
    /** Ruta base del CRUD de la familia de entidades. */
    public static final String ENTIDADES = "/entidades";
    /** Ruta para crear un esqueleto. */
    public static final String ESQUELETO = "/esqueleto";
    /** Ruta para ver las dos versiones del mensaje disparar(). */
    public static final String ESQUELETO_DISPARAR = "/esqueleto/disparar";
    /** Ruta que muestra la reaccion de cada tipo de entidad. */
    public static final String COMPORTAMIENTO = "/comportamiento";
    /** Ruta que muestra los datos de UI de las entidades UICapable. */
    public static final String UI = "/ui";
    /** Ruta que ejecuta la simulacion de una explosion. */
    public static final String SIMULACION_EXPLOSION = "/simulacion/explosion";
    /** Ruta que devuelve una simulacion persistida por su id. */
    public static final String SIMULACION_EXPLOSION_ID = "/simulacion/explosion/{id}";
    /** Ruta que lista todas las simulaciones persistidas. */
    public static final String SIMULACION_EXPLOSIONES = "/simulacion/explosiones";

    private ApiPaths() {
    }
}
