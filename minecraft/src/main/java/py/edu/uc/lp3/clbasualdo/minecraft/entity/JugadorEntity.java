package py.edu.uc.lp3.clbasualdo.minecraft.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Descripcion:
 * Hija de EntidadEntity que representa un Jugador persistido.
 *
 * Responsabilidad:
 * Guardar los datos propios del Jugador (nivel y experiencia) en la misma tabla
 * de la familia, usando el discriminador "Jugador".
 */
@Entity
@DiscriminatorValue("Jugador")
public class JugadorEntity extends EntidadEntity {

    /** Nivel actual del jugador. */
    private int nivel;

    /** Experiencia acumulada del jugador. */
    private int experiencia;

    /**
     * Descripcion:
     * Constructor vacio requerido por JPA y Jackson.
     */
    protected JugadorEntity() {
    }

    /**
     * Descripcion:
     * Crea un Jugador persistible. La vida maxima arranca igual a la vida.
     *
     * Parametros:
     * nombre - Nombre del jugador.
     * vida - Vida inicial.
     * nivel - Nivel inicial.
     * x - Posicion en X.
     * y - Posicion en Y.
     * z - Posicion en Z.
     */
    public JugadorEntity(String nombre, int vida, int nivel, double x, double y, double z) {
        super(nombre, vida, vida, x, y, z);
        this.nivel = nivel;
    }

    /**
     * Descripcion:
     * Devuelve el nivel del jugador.
     *
     * Retorno:
     * El nivel actual.
     */
    public int getNivel() {
        return nivel;
    }

    /**
     * Descripcion:
     * Asigna el nivel del jugador.
     *
     * Parametros:
     * nivel - Nivel a asignar.
     */
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    /**
     * Descripcion:
     * Devuelve la experiencia del jugador.
     *
     * Retorno:
     * La experiencia acumulada.
     */
    public int getExperiencia() {
        return experiencia;
    }

    /**
     * Descripcion:
     * Asigna la experiencia del jugador.
     *
     * Parametros:
     * experiencia - Experiencia a asignar.
     */
    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    /**
     * Descripcion:
     * Copia los datos comunes y los propios del Jugador.
     *
     * Parametros:
     * otro - Entidad de la que se copian los datos.
     */
    @Override
    public void copiarDatosDesde(EntidadEntity otro) {
        super.copiarDatosDesde(otro);
        if (otro instanceof JugadorEntity jugador) {
            this.nivel = jugador.nivel;
            this.experiencia = jugador.experiencia;
        }
    }
}
