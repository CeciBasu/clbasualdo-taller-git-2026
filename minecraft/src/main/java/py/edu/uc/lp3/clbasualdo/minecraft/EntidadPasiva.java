package py.edu.uc.lp3.clbasualdo.minecraft;

public abstract class EntidadPasiva extends Entidad {
    private static final double DISTANCIA_SEGURA = 6.0;

    public EntidadPasiva(String nombre, int vida) { super(nombre, vida); }

    public void huir() {
        System.out.println(getNombre() + " está huyendo.");
    }

    /** Huye alejándose de la posición real de la amenaza. */
    public void huir(Entidad amenaza) {
        if (amenaza == null) {
            throw new IllegalArgumentException("La amenaza no puede ser nula.");
        }
        double dx = getX() - amenaza.getX();
        double dy = getY() - amenaza.getY();
        double dz = getZ() - amenaza.getZ();
        double norma = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (norma == 0) {
            dx = 1;
            norma = 1;
        }
        System.out.println(reaccionar());
        moverse((dx / norma) * 2, (dy / norma) * 2, (dz / norma) * 2);
    }

    /** Indica si una amenaza está dentro de la distancia considerada segura. */
    public boolean estaEnPeligro(Entidad amenaza) {
        return amenaza != null && distanciaHacia(amenaza) <= DISTANCIA_SEGURA;
    }
}
