package py.edu.uc.lp3.clbasualdo.minecraft.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Descripción:
 * Fila de la tabla "emboscadas": una jugada del escenario que A2 y A3 aceptaron.
 *
 * Responsabilidad:
 * Guardar qué Creeper emboscó a qué Jugador, la simulación de explosión que se ejecutó y lo
 * que respondió cada compañero. Solo se crea cuando las dos reglas ajenas aceptaron; se
 * arma completa con el constructor y no tiene setters.
 */
@Entity
@Table(name = "emboscadas")
public class EmboscadaEntity {

    /** Identificador autogenerado por H2. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Momento en que se guardó la jugada. */
    private LocalDateTime fecha;

    /** Id del Creeper que emboscó. */
    private Long creeperId;

    /** Id del Jugador emboscado. */
    private Long jugadorId;

    /** Id de la simulación de explosión que se guardó con la jugada. */
    private Long simulacionId;

    /** Nombre de A2 al momento de la jugada. */
    private String companeroA2;

    /** Respuesta de A2, tal cual llegó. */
    @Column(length = 4000)
    private String respuestaA2;

    /** Nombre de A3 al momento de la jugada. */
    private String companeroA3;

    /** Respuesta de A3, tal cual llegó. */
    @Column(length = 4000)
    private String respuestaA3;

    /**
     * Descripción:
     * Constructor vacío que exige JPA.
     */
    protected EmboscadaEntity() {
    }

    /**
     * Descripción:
     * Crea la jugada aceptada.
     *
     * Parámetros:
     * fecha - Momento de la jugada.
     * creeperId - Id del Creeper.
     * jugadorId - Id del Jugador.
     * simulacionId - Id de la simulación guardada.
     * companeroA2 - Nombre de A2.
     * respuestaA2 - Respuesta de A2.
     * companeroA3 - Nombre de A3.
     * respuestaA3 - Respuesta de A3.
     */
    public EmboscadaEntity(LocalDateTime fecha, Long creeperId, Long jugadorId, Long simulacionId,
            String companeroA2, String respuestaA2, String companeroA3, String respuestaA3) {
        this.fecha = fecha;
        this.creeperId = creeperId;
        this.jugadorId = jugadorId;
        this.simulacionId = simulacionId;
        this.companeroA2 = companeroA2;
        this.respuestaA2 = recortar(respuestaA2);
        this.companeroA3 = companeroA3;
        this.respuestaA3 = recortar(respuestaA3);
    }

    /**
     * Descripción:
     * Corta una respuesta muy larga para que entre en la columna.
     *
     * Parámetros:
     * texto - Respuesta original.
     *
     * Retorno:
     * El texto, de 4000 caracteres como máximo.
     */
    private static String recortar(String texto) {
        return texto == null || texto.length() <= 4000 ? texto : texto.substring(0, 4000);
    }

    /** @return el id de la jugada. */
    public Long getId() {
        return id;
    }

    /** @return la fecha de la jugada. */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /** @return el id del Creeper. */
    public Long getCreeperId() {
        return creeperId;
    }

    /** @return el id del Jugador. */
    public Long getJugadorId() {
        return jugadorId;
    }

    /** @return el id de la simulación guardada. */
    public Long getSimulacionId() {
        return simulacionId;
    }

    /** @return el nombre de A2. */
    public String getCompaneroA2() {
        return companeroA2;
    }

    /** @return la respuesta de A2. */
    public String getRespuestaA2() {
        return respuestaA2;
    }

    /** @return el nombre de A3. */
    public String getCompaneroA3() {
        return companeroA3;
    }

    /** @return la respuesta de A3. */
    public String getRespuestaA3() {
        return respuestaA3;
    }
}
