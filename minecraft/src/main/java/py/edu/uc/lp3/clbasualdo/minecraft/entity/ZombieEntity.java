package py.edu.uc.lp3.clbasualdo.minecraft.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Descripcion:
 * Hija de EntidadEntity que representa un Zombie persistido.
 *
 * Responsabilidad:
 * Guardar el dato propio del Zombie (si rompio una puerta) en la misma tabla de
 * la familia, usando el discriminador "Zombie".
 */
@Entity
@DiscriminatorValue("Zombie")
public class ZombieEntity extends EntidadEntity {

    /** Indica si el Zombie ya rompio una puerta. */
    private boolean puertaRota;

    /**
     * Descripcion:
     * Constructor vacio requerido por JPA y Jackson.
     */
    protected ZombieEntity() {
    }

    /**
     * Descripcion:
     * Crea un Zombie persistible. La vida maxima arranca igual a la vida.
     *
     * Parametros:
     * nombre - Nombre del Zombie.
     * vida - Vida inicial.
     * x - Posicion en X.
     * y - Posicion en Y.
     * z - Posicion en Z.
     */
    public ZombieEntity(String nombre, int vida, double x, double y, double z) {
        super(nombre, vida, vida, x, y, z);
    }

    /**
     * Descripcion:
     * Dice si el Zombie rompio una puerta.
     *
     * Retorno:
     * true si la rompio, false si no.
     */
    public boolean isPuertaRota() {
        return puertaRota;
    }

    /**
     * Descripcion:
     * Asigna si el Zombie rompio una puerta.
     *
     * Parametros:
     * puertaRota - Estado de la puerta.
     */
    public void setPuertaRota(boolean puertaRota) {
        this.puertaRota = puertaRota;
    }

    /**
     * Descripcion:
     * Copia los datos comunes y el estado de la puerta de otro Zombie.
     *
     * Parametros:
     * otro - Entidad de la que se copian los datos.
     */
    @Override
    public void copiarDatosDesde(EntidadEntity otro) {
        super.copiarDatosDesde(otro);
        if (otro instanceof ZombieEntity zombie) {
            this.puertaRota = zombie.puertaRota;
        }
    }
}
