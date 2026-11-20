package py.edu.uc.lp3.clbasualdo.minecraft.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

/**
 * Descripción:
 * Fila de la tabla "explosiones": la foto de una simulación ya ejecutada.
 *
 * Responsabilidad:
 * Guardar qué Creeper guardado detonó, dónde estaba, con cuánta vida llegó y qué le pasó a
 * cada entidad guardada que estaba viva en ese momento. Así un GET posterior devuelve el
 * resultado sin volver a ejecutar la explosión. Se arma completa con el constructor y no
 * tiene setters: una explosión ya ocurrida no cambia.
 */
@Entity
@Table(name = "explosiones")
public class SimulacionExplosionEntity {

    /** Identificador autogenerado por H2. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha y hora en que se ejecutó la simulación. */
    private LocalDateTime fecha;

    /** Radio de la explosión, en bloques. */
    private double radioExplosion;

    /** Id del Creeper guardado que detonó. */
    private Long creeperId;

    /** Nombre del Creeper al momento de detonar. */
    private String creeperNombre;

    /** Vida del Creeper justo antes de detonar. */
    private int creeperVidaInicial;

    /** Posición X del Creeper. */
    private double creeperX;

    /** Posición Y del Creeper. */
    private double creeperY;

    /** Posición Z del Creeper. */
    private double creeperZ;

    /** Lo que le pasó a cada entidad viva al momento de la explosión. */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "explosion_impactos", joinColumns = @JoinColumn(name = "explosion_id"))
    @OrderColumn(name = "orden")
    private List<ImpactoExplosion> impactos = new ArrayList<>();

    /**
     * Descripción:
     * Constructor vacío que exige JPA.
     */
    protected SimulacionExplosionEntity() {
    }

    /**
     * Descripción:
     * Crea la foto completa de una explosión.
     *
     * Parámetros:
     * fecha - Momento de la simulación.
     * radioExplosion - Radio de la explosión.
     * creeper - Creeper guardado que detonó, leído antes de detonar.
     * creeperVidaInicial - Vida del Creeper antes de detonar.
     * impactos - Resultado sobre cada entidad viva.
     */
    public SimulacionExplosionEntity(LocalDateTime fecha, double radioExplosion, CreeperEntity creeper,
            int creeperVidaInicial, List<ImpactoExplosion> impactos) {
        this.fecha = fecha;
        this.radioExplosion = radioExplosion;
        this.creeperId = creeper.getId();
        this.creeperNombre = creeper.getNombre();
        this.creeperVidaInicial = creeperVidaInicial;
        this.creeperX = creeper.getX();
        this.creeperY = creeper.getY();
        this.creeperZ = creeper.getZ();
        this.impactos = new ArrayList<>(impactos);
    }

    /** @return el id de la simulación. */
    public Long getId() {
        return id;
    }

    /** @return la fecha de la simulación. */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /** @return el radio de la explosión. */
    public double getRadioExplosion() {
        return radioExplosion;
    }

    /** @return el id del Creeper que detonó. */
    public Long getCreeperId() {
        return creeperId;
    }

    /** @return el nombre del Creeper. */
    public String getCreeperNombre() {
        return creeperNombre;
    }

    /** @return la vida del Creeper antes de detonar. */
    public int getCreeperVidaInicial() {
        return creeperVidaInicial;
    }

    /** @return la posición X del Creeper. */
    public double getCreeperX() {
        return creeperX;
    }

    /** @return la posición Y del Creeper. */
    public double getCreeperY() {
        return creeperY;
    }

    /** @return la posición Z del Creeper. */
    public double getCreeperZ() {
        return creeperZ;
    }

    /** @return una copia de los impactos guardados. */
    public List<ImpactoExplosion> getImpactos() {
        return List.copyOf(impactos);
    }
}
