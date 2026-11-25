package py.edu.uc.lp3.clbasualdo.minecraft.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Descripción:
 * Fila de la tabla "entidades" que representa a un Zombie.
 *
 * Responsabilidad:
 * Guardar si el Zombie ya rompió una puerta, además de los datos comunes.
 */
@Entity
@DiscriminatorValue("Zombie")
public class ZombieEntity extends EntidadEntity {

    /** Indica si el Zombie ya rompió una puerta. */
    private boolean puertaRota;

    /**
     * Descripción:
     * Constructor vacío que exige JPA.
     */
    protected ZombieEntity() {
    }

    /**
     * Descripción:
     * Crea un Zombie que todavía no rompió ninguna puerta.
     *
     * Parámetros:
     * nombre - Nombre único.
     * vida - Vida inicial, que también es la máxima.
     * x - Posición en X.
     * y - Posición en Y.
     * z - Posición en Z.
     */
    public ZombieEntity(String nombre, int vida, double x, double y, double z) {
        this(nombre, vida, vida, x, y, z, false);
    }

    /**
     * Descripción:
     * Crea un Zombie con todos sus datos.
     *
     * Parámetros:
     * nombre - Nombre único.
     * vida - Vida actual.
     * vidaMaxima - Vida máxima.
     * x - Posición en X.
     * y - Posición en Y.
     * z - Posición en Z.
     * puertaRota - Si ya rompió una puerta.
     */
    public ZombieEntity(String nombre, int vida, int vidaMaxima, double x, double y, double z, boolean puertaRota) {
        super(nombre, vida, vidaMaxima, x, y, z);
        this.puertaRota = puertaRota;
    }

    /**
     * Descripción:
     * Dice si el Zombie ya rompió una puerta.
     *
     * Retorno:
     * true si la rompió.
     */
    public boolean isPuertaRota() {
        return puertaRota;
    }

    /**
     * Descripción:
     * Copia los datos comunes y el estado de la puerta desde otro Zombie.
     *
     * Parámetros:
     * otro - Entidad con los datos nuevos.
     */
    @Override
    public void copiarDatosDesde(EntidadEntity otro) {
        super.copiarDatosDesde(otro);
        if (otro instanceof ZombieEntity zombie) {
            this.puertaRota = zombie.puertaRota;
        }
    }
}
