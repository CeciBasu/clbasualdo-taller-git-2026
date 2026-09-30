package py.edu.uc.lp3.clbasualdo.minecraft;

/**
 * Descripcion:
 * Clase base de las entidades que no atacan, como los aldeanos y los animales.
 *
 * Responsabilidad:
 * Se encarga de que estas entidades puedan huir de una amenaza y de saber si estan en peligro.
 */
public abstract class EntidadPasiva extends Entidad {
    /** Distancia a partir de la cual una amenaza ya no es peligrosa. */
    private static final double DISTANCIA_SEGURA = 6.0;

    /**
     * Descripcion:
     * Crea una entidad pasiva.
     *
     * Parametros:
     * nombre - Nombre de la entidad.
     * vida - Vida inicial de la entidad.
     */
    public EntidadPasiva(String nombre, int vida) { super(nombre, vida); }

    /**
     * Descripcion:
     * Muestra un mensaje diciendo que la entidad esta huyendo. No la mueve de lugar.
     */
    public void huir() {
        System.out.println(getNombre() + " está huyendo.");
    }

    /**
     * Descripcion:
     * La entidad se aleja 2 bloques de la amenaza, en direccion contraria a donde esta esta.
     * Lanza un error si la amenaza es nula.
     *
     * Parametros:
     * amenaza - La entidad de la que se quiere huir.
     */
    public void huir(Entidad amenaza) {
        if (amenaza == null) {
            throw new IllegalArgumentException("La amenaza no puede ser nula.");
        }
        double dx = getX() - amenaza.getX();
        double dy = getY() - amenaza.getY();
        double dz = getZ() - amenaza.getZ();
        double norma = Math.sqrt(dx * dx + dy * dy + dz * dz);
        // Si estan en el mismo punto, se elige huir hacia X para no dividir por cero
        if (norma == 0) {
            dx = 1;
            norma = 1;
        }
        System.out.println(reaccionar());
        moverse((dx / norma) * 2, (dy / norma) * 2, (dz / norma) * 2);
    }

    /**
     * Descripcion:
     * Revisa si una amenaza esta lo bastante cerca como para ser peligrosa.
     *
     * Parametros:
     * amenaza - La entidad que se quiere revisar.
     *
     * Retorno:
     * true si la amenaza esta dentro de la distancia segura, false si no o si es nula.
     */
    public boolean estaEnPeligro(Entidad amenaza) {
        return amenaza != null && distanciaHacia(amenaza) <= DISTANCIA_SEGURA;
    }
}
