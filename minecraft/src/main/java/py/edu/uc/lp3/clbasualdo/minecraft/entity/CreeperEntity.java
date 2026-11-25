package py.edu.uc.lp3.clbasualdo.minecraft.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Descripción:
 * Fila de la tabla "entidades" que representa a un Creeper.
 *
 * Responsabilidad:
 * Guardar si el Creeper ya detonó. En Minecraft un Creeper explota una sola vez y muere,
 * por eso "detonado" y "vida" no son independientes: un Creeper detonado tiene vida 0.
 * La única forma de marcarlo detonado es registrarDetonacion(), que cambia los dos datos
 * juntos. Si la detonación es legal lo decide la capa service.
 */
@Entity
@DiscriminatorValue("Creeper")
public class CreeperEntity extends EntidadEntity {

    /** Indica si el Creeper ya explotó. */
    private boolean detonado;

    /**
     * Descripción:
     * Constructor vacío que exige JPA.
     */
    protected CreeperEntity() {
    }

    /**
     * Descripción:
     * Crea un Creeper sin detonar, con la vida inicial como vida máxima.
     *
     * Parámetros:
     * nombre - Nombre único.
     * vida - Vida inicial.
     * x - Posición en X.
     * y - Posición en Y.
     * z - Posición en Z.
     */
    public CreeperEntity(String nombre, int vida, double x, double y, double z) {
        this(nombre, vida, vida, x, y, z, false);
    }

    /**
     * Descripción:
     * Crea un Creeper con todos sus datos. El service ya revisó que la combinación sea posible.
     *
     * Parámetros:
     * nombre - Nombre único.
     * vida - Vida actual.
     * vidaMaxima - Vida máxima.
     * x - Posición en X.
     * y - Posición en Y.
     * z - Posición en Z.
     * detonado - Si ya explotó.
     */
    public CreeperEntity(String nombre, int vida, int vidaMaxima, double x, double y, double z, boolean detonado) {
        super(nombre, vida, vidaMaxima, x, y, z);
        this.detonado = detonado;
    }

    /**
     * Descripción:
     * Dice si el Creeper ya explotó.
     *
     * Retorno:
     * true si ya detonó.
     */
    public boolean isDetonado() {
        return detonado;
    }

    /**
     * Descripción:
     * Registra la explosión: el Creeper queda detonado y sin vida en el mismo paso.
     */
    public void registrarDetonacion() {
        this.detonado = true;
        recibirDanio(getVida());
    }

    /**
     * Descripción:
     * Copia los datos comunes y el estado de detonación desde otro Creeper.
     *
     * Parámetros:
     * otro - Entidad con los datos nuevos.
     */
    @Override
    public void copiarDatosDesde(EntidadEntity otro) {
        super.copiarDatosDesde(otro);
        if (otro instanceof CreeperEntity creeper) {
            this.detonado = creeper.detonado;
        }
    }
}
