package py.edu.uc.lp3.clbasualdo.minecraft.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Descripcion:
 * Hija de EntidadEntity que representa un Creeper persistido.
 *
 * Responsabilidad:
 * Guardar el dato propio del Creeper (si ya detono) en la misma tabla de la
 * familia, usando el discriminador "Creeper".
 */
@Entity
@DiscriminatorValue("Creeper")
public class CreeperEntity extends EntidadEntity {

    /** Indica si el Creeper ya exploto. */
    private boolean detonado;

    /**
     * Descripcion:
     * Constructor vacio requerido por JPA y Jackson.
     */
    protected CreeperEntity() {
    }

    /**
     * Descripcion:
     * Crea un Creeper persistible. La vida maxima arranca igual a la vida.
     *
     * Parametros:
     * nombre - Nombre del Creeper.
     * vida - Vida inicial.
     * x - Posicion en X.
     * y - Posicion en Y.
     * z - Posicion en Z.
     */
    public CreeperEntity(String nombre, int vida, double x, double y, double z) {
        super(nombre, vida, vida, x, y, z);
    }

    /**
     * Descripcion:
     * Dice si el Creeper ya detono.
     *
     * Retorno:
     * true si exploto, false si no.
     */
    public boolean isDetonado() {
        return detonado;
    }

    /**
     * Descripcion:
     * Asigna si el Creeper ya detono.
     *
     * Parametros:
     * detonado - Estado de detonacion.
     */
    public void setDetonado(boolean detonado) {
        this.detonado = detonado;
    }

    /**
     * Descripcion:
     * Copia los datos comunes y el estado de detonacion de otro Creeper.
     *
     * Parametros:
     * otro - Entidad de la que se copian los datos.
     */
    @Override
    public void copiarDatosDesde(EntidadEntity otro) {
        super.copiarDatosDesde(otro);
        if (otro instanceof CreeperEntity creeper) {
            this.detonado = creeper.detonado;
        }
    }
}
