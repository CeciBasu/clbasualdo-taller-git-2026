package py.edu.uc.lp3.clbasualdo.minecraft.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Descripción:
 * Fila de la tabla "entidades" que representa a un Jugador.
 *
 * Responsabilidad:
 * Guardar el nivel y la experiencia acumulada del jugador, además de los datos comunes.
 */
@Entity
@DiscriminatorValue("Jugador")
public class JugadorEntity extends EntidadEntity {

    /** Nivel actual del jugador. */
    private int nivel;

    /** Experiencia acumulada hacia el próximo nivel (de 0 a 99). */
    private int experiencia;

    /**
     * Descripción:
     * Constructor vacío que exige JPA.
     */
    protected JugadorEntity() {
    }

    /**
     * Descripción:
     * Crea un jugador sin experiencia acumulada.
     *
     * Parámetros:
     * nombre - Nombre único.
     * vida - Vida inicial, que también es la máxima.
     * nivel - Nivel inicial.
     * x - Posición en X.
     * y - Posición en Y.
     * z - Posición en Z.
     */
    public JugadorEntity(String nombre, int vida, int nivel, double x, double y, double z) {
        this(nombre, vida, vida, x, y, z, nivel, 0);
    }

    /**
     * Descripción:
     * Crea un jugador con todos sus datos.
     *
     * Parámetros:
     * nombre - Nombre único.
     * vida - Vida actual.
     * vidaMaxima - Vida máxima.
     * x - Posición en X.
     * y - Posición en Y.
     * z - Posición en Z.
     * nivel - Nivel.
     * experiencia - Experiencia acumulada.
     */
    public JugadorEntity(String nombre, int vida, int vidaMaxima, double x, double y, double z,
            int nivel, int experiencia) {
        super(nombre, vida, vidaMaxima, x, y, z);
        this.nivel = nivel;
        this.experiencia = experiencia;
    }

    /**
     * Descripción:
     * Devuelve el nivel.
     *
     * Retorno:
     * El nivel actual.
     */
    public int getNivel() {
        return nivel;
    }

    /**
     * Descripción:
     * Devuelve la experiencia acumulada.
     *
     * Retorno:
     * La experiencia hacia el próximo nivel.
     */
    public int getExperiencia() {
        return experiencia;
    }

    /**
     * Descripción:
     * Copia los datos comunes, el nivel y la experiencia desde otro jugador.
     *
     * Parámetros:
     * otro - Entidad con los datos nuevos.
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
