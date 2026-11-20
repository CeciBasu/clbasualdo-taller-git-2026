package py.edu.uc.lp3.clbasualdo.minecraft.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

/**
 * Descripción:
 * Entidad de persistencia que representa la clase base de la familia guardada en H2.
 *
 * Responsabilidad:
 * Mapear la tabla "entidades" con los datos comunes (nombre, vida, vida máxima y
 * posición) y declarar la herencia de JPA. El discriminador "tipo" distingue en la
 * misma tabla si la fila es un Creeper, un Zombie o un Jugador.
 *
 * No tiene setters públicos: el estado se arma con el constructor y solo cambia con
 * mensajes que no pueden dejar una combinación imposible (recibirDanio, copiarDatosDesde).
 * Las reglas que deciden si un cambio es legal viven en la capa service.
 */
@Entity
@Table(name = "entidades")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreeperEntity.class, name = "Creeper"),
        @JsonSubTypes.Type(value = ZombieEntity.class, name = "Zombie"),
        @JsonSubTypes.Type(value = JugadorEntity.class, name = "Jugador")
})
public abstract class EntidadEntity {

    /** Identificador único autogenerado por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre único de la entidad. */
    private String nombre;

    /** Vida actual. */
    private int vida;

    /** Vida máxima: la vida actual nunca la supera. */
    private int vidaMaxima;

    /** Posición en el eje X. */
    private double x;

    /** Posición en el eje Y (la altura). */
    private double y;

    /** Posición en el eje Z. */
    private double z;

    /**
     * Descripción:
     * Constructor vacío que exige JPA para leer filas de la base.
     */
    protected EntidadEntity() {
    }

    /**
     * Descripción:
     * Crea la parte común de una entidad persistible.
     *
     * Parámetros:
     * nombre - Nombre único.
     * vida - Vida actual.
     * vidaMaxima - Vida máxima.
     * x - Posición en X.
     * y - Posición en Y.
     * z - Posición en Z.
     */
    protected EntidadEntity(String nombre, int vida, int vidaMaxima, double x, double y, double z) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMaxima = vidaMaxima;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Descripción:
     * Devuelve el identificador asignado por H2.
     *
     * Retorno:
     * El id, o null si todavía no se guardó.
     */
    public Long getId() {
        return id;
    }

    /**
     * Descripción:
     * Devuelve el nombre.
     *
     * Retorno:
     * El nombre de la entidad.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Descripción:
     * Devuelve la vida actual.
     *
     * Retorno:
     * La vida actual.
     */
    public int getVida() {
        return vida;
    }

    /**
     * Descripción:
     * Devuelve la vida máxima.
     *
     * Retorno:
     * La vida máxima.
     */
    public int getVidaMaxima() {
        return vidaMaxima;
    }

    /**
     * Descripción:
     * Devuelve la posición en X.
     *
     * Retorno:
     * El valor de X.
     */
    public double getX() {
        return x;
    }

    /**
     * Descripción:
     * Devuelve la posición en Y.
     *
     * Retorno:
     * El valor de Y.
     */
    public double getY() {
        return y;
    }

    /**
     * Descripción:
     * Devuelve la posición en Z.
     *
     * Retorno:
     * El valor de Z.
     */
    public double getZ() {
        return z;
    }

    /**
     * Descripción:
     * Dice si la entidad sigue viva.
     *
     * Retorno:
     * true si la vida es mayor a 0.
     */
    public boolean estaViva() {
        return vida > 0;
    }

    /**
     * Descripción:
     * Le resta vida a la entidad sin bajar de 0. Solo puede quitar vida, nunca darla,
     * así que no puede revivir a una entidad muerta ni a un Creeper detonado.
     *
     * Parámetros:
     * danio - Vida que pierde; el service garantiza que no sea negativa.
     */
    public void recibirDanio(int danio) {
        this.vida = Math.max(0, this.vida - danio);
    }

    /**
     * Descripción:
     * Copia los datos comunes desde otra entidad del mismo tipo. La usa el service en un
     * PUT, después de validar que la combinación nueva sea legal.
     *
     * Parámetros:
     * otro - Entidad con los datos nuevos.
     */
    public void copiarDatosDesde(EntidadEntity otro) {
        this.nombre = otro.nombre;
        this.vida = otro.vida;
        this.vidaMaxima = otro.vidaMaxima;
        this.x = otro.x;
        this.y = otro.y;
        this.z = otro.z;
    }
}
