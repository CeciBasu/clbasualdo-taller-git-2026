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
 * Descripcion:
 * Entidad de persistencia que representa la clase base de la familia guardada en H2.
 *
 * Responsabilidad:
 * Mapear la tabla "entidades" con los datos comunes (nombre, vida, vida maxima y
 * posicion) y declarar la herencia de JPA. El discriminador "tipo" distingue en la
 * misma tabla si la fila es un Creeper, un Zombie o un Jugador.
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

    /** Identificador unico autogenerado por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de la entidad. */
    private String nombre;

    /** Vida actual de la entidad. */
    private int vida;

    /** Vida maxima que puede recuperar la entidad. */
    private int vidaMaxima;

    /** Posicion en el eje X. */
    private double x;

    /** Posicion en el eje Y. */
    private double y;

    /** Posicion en el eje Z. */
    private double z;

    /**
     * Descripcion:
     * Constructor vacio requerido por JPA y por Jackson. Es protected para que
     * nadie fuera de la jerarquia pueda crear una entidad sin datos de negocio.
     */
    protected EntidadEntity() {
    }

    /**
     * Descripcion:
     * Crea una entidad persistible con todos sus datos comunes.
     *
     * Parametros:
     * nombre - Nombre de la entidad.
     * vida - Vida actual.
     * vidaMaxima - Vida maxima.
     * x - Posicion en X.
     * y - Posicion en Y.
     * z - Posicion en Z.
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
     * Descripcion:
     * Devuelve el id autogenerado.
     *
     * Retorno:
     * El id de la fila.
     */
    public Long getId() {
        return id;
    }

    /**
     * Descripcion:
     * Asigna el id. Lo usa JPA al recuperar la fila.
     *
     * Parametros:
     * id - Id a asignar.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Descripcion:
     * Devuelve el nombre.
     *
     * Retorno:
     * El nombre de la entidad.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Descripcion:
     * Asigna el nombre.
     *
     * Parametros:
     * nombre - Nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Descripcion:
     * Devuelve la vida actual.
     *
     * Retorno:
     * La vida actual.
     */
    public int getVida() {
        return vida;
    }

    /**
     * Descripcion:
     * Asigna la vida actual.
     *
     * Parametros:
     * vida - Vida a asignar.
     */
    public void setVida(int vida) {
        this.vida = vida;
    }

    /**
     * Descripcion:
     * Devuelve la vida maxima.
     *
     * Retorno:
     * La vida maxima.
     */
    public int getVidaMaxima() {
        return vidaMaxima;
    }

    /**
     * Descripcion:
     * Asigna la vida maxima.
     *
     * Parametros:
     * vidaMaxima - Vida maxima a asignar.
     */
    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
    }

    /**
     * Descripcion:
     * Devuelve la posicion en X.
     *
     * Retorno:
     * El valor de X.
     */
    public double getX() {
        return x;
    }

    /**
     * Descripcion:
     * Asigna la posicion en X.
     *
     * Parametros:
     * x - Valor a asignar.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Descripcion:
     * Devuelve la posicion en Y.
     *
     * Retorno:
     * El valor de Y.
     */
    public double getY() {
        return y;
    }

    /**
     * Descripcion:
     * Asigna la posicion en Y.
     *
     * Parametros:
     * y - Valor a asignar.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Descripcion:
     * Devuelve la posicion en Z.
     *
     * Retorno:
     * El valor de Z.
     */
    public double getZ() {
        return z;
    }

    /**
     * Descripcion:
     * Asigna la posicion en Z.
     *
     * Parametros:
     * z - Valor a asignar.
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Descripcion:
     * Copia los datos comunes de otra entidad sobre esta. Lo usa el servicio de
     * actualizacion para no repetir la asignacion campo por campo.
     *
     * Parametros:
     * otro - Entidad de la que se copian los datos.
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
