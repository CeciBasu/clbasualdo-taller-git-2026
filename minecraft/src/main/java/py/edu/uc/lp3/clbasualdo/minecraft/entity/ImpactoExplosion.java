package py.edu.uc.lp3.clbasualdo.minecraft.entity;

import jakarta.persistence.Embeddable;

/**
 * Descripción:
 * Lo que le pasó a una entidad guardada en una explosión. Se guarda como parte de
 * SimulacionExplosionEntity, en la tabla "explosion_impactos".
 *
 * Responsabilidad:
 * Recordar quién era la entidad, a qué distancia estaba del Creeper y con cuánta vida
 * quedó. Es inmutable: se arma con el constructor.
 */
@Embeddable
public class ImpactoExplosion {

    /** Id de la entidad en la tabla "entidades". */
    private Long entidadId;

    /** Subtipo: Creeper, Zombie o Jugador. */
    private String tipo;

    /** Nombre de la entidad. */
    private String nombre;

    /** Vida antes de la explosión. */
    private int vidaInicial;

    /** Vida después de la explosión. */
    private int vidaFinal;

    /** Distancia al Creeper, en bloques. */
    private double distanciaAlCreeper;

    /** Si quedó dentro del radio de la explosión. */
    private boolean alcanzada;

    /**
     * Descripción:
     * Constructor vacío que exige JPA.
     */
    protected ImpactoExplosion() {
    }

    /**
     * Descripción:
     * Crea el registro del impacto sobre una entidad.
     *
     * Parámetros:
     * entidadId - Id de la entidad.
     * tipo - Subtipo de la entidad.
     * nombre - Nombre de la entidad.
     * vidaInicial - Vida antes de la explosión.
     * vidaFinal - Vida después de la explosión.
     * distanciaAlCreeper - Distancia al Creeper.
     * alcanzada - Si quedó dentro del radio.
     */
    public ImpactoExplosion(Long entidadId, String tipo, String nombre, int vidaInicial, int vidaFinal,
            double distanciaAlCreeper, boolean alcanzada) {
        this.entidadId = entidadId;
        this.tipo = tipo;
        this.nombre = nombre;
        this.vidaInicial = vidaInicial;
        this.vidaFinal = vidaFinal;
        this.distanciaAlCreeper = distanciaAlCreeper;
        this.alcanzada = alcanzada;
    }

    /** @return el id de la entidad. */
    public Long getEntidadId() {
        return entidadId;
    }

    /** @return el subtipo. */
    public String getTipo() {
        return tipo;
    }

    /** @return el nombre. */
    public String getNombre() {
        return nombre;
    }

    /** @return la vida antes de la explosión. */
    public int getVidaInicial() {
        return vidaInicial;
    }

    /** @return la vida después de la explosión. */
    public int getVidaFinal() {
        return vidaFinal;
    }

    /** @return la distancia al Creeper. */
    public double getDistanciaAlCreeper() {
        return distanciaAlCreeper;
    }

    /** @return si quedó dentro del radio. */
    public boolean isAlcanzada() {
        return alcanzada;
    }
}
