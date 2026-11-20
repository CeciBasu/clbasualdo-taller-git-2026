package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import java.util.List;
import java.util.Locale;

/**
 * Descripción:
 * Representa al Creeper, un enemigo que se acerca al jugador y explota.
 *
 * Responsabilidad:
 * Se encarga de explotar, lastimar a las entidades que estén cerca y destruirse a sí mismo.
 * También es UICapable para poder dibujarse en la UI.
 */
public class Creeper extends EntidadHostil implements UICapable {
    /** Distancia máxima a la que la explosión lastima a otras entidades. */
    private static final double RADIO_EXPLOSION = 3.0;
    /** Indica si el Creeper ya explotó. */
    private boolean detonado;

    /**
     * Descripción:
     * Crea un Creeper con 20 de vida, 25 de daño y 3.0 de rango de ataque.
     */
    public Creeper() { super("Creeper", 20, 25, 3.0); }

    /**
     * Descripción:
     * Muestra un mensaje avisando que el Creeper está por explotar.
     */
    @Override
    public void atacar() {
        System.out.println("El Creeper está a punto de explotar.");
    }

    /**
     * Descripción:
     * Dice si el Creeper ya explotó.
     *
     * Retorno:
     * true si ya explotó, false si no.
     */
    public boolean isDetonado() { return detonado; }

    /**
     * Descripción:
     * Devuelve el radio máximo que alcanza la explosión del Creeper.
     *
     * Retorno:
     * El radio de explosión.
     */
    public double getRadioExplosion() { return RADIO_EXPLOSION; }

    /**
     * Descripción:
     * El Creeper explota y lastima a todas las entidades vivas que estén dentro del radio de explosión.
     * Después de explotar queda detonado y muere. Las dos reglas (solo detona si está vivo y solo
     * una vez) las aplica la capa service antes de enviar este mensaje.
     *
     * Parámetros:
     * cercanas - Lista de entidades que pueden ser alcanzadas por la explosión.
     */
    public void explotar(List<Entidad> cercanas) {
        System.out.println(getNombre() + " explota.");
        if (cercanas != null) {
            for (Entidad e : cercanas) {
                // No se lastima a sí mismo acá, eso se hace al final
                if (e != this && e.estaVivo() && distanciaHacia(e) <= RADIO_EXPLOSION) {
                    e.recibirDanio(getDanioAtaque());
                    System.out.printf("La explosión alcanza a %s (%d de daño).%n", e.getNombre(), getDanioAtaque());
                }
            }
        }
        detonado = true;
        // Se quita toda su vida para que muera
        recibirDanio(getVida());
    }

    /**
     * Descripción:
     * Dice cómo reacciona el Creeper: se acerca y está por explotar.
     *
     * Retorno:
     * Un texto que describe la reacción.
     */
    @Override
    public String reaccionar() {
        return getNombre() + " se acerca silbando y está a punto de explotar.";
    }

    /**
     * Descripción:
     * Devuelve la imagen con la que la UI dibuja al Creeper.
     *
     * Retorno:
     * El nombre de la imagen del Creeper.
     */
    @Override
    public String imagen() {
        return "creeper.png";
    }

    /**
     * Descripción:
     * Devuelve la posición del Creeper formateada para la UI.
     *
     * Retorno:
     * Un texto con las coordenadas del Creeper.
     */
    @Override
    public String posicion() {
        return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", getX(), getY(), getZ());
    }

    /**
     * Descripción:
     * Devuelve la velocidad con la que se mueve el Creeper.
     *
     * Retorno:
     * La velocidad del Creeper.
     */
    @Override
    public double velocidad() {
        return 0.25;
    }

    /**
     * Descripción:
     * Reemplaza el sonido genérico del default con el propio del Creeper.
     *
     * Retorno:
     * Un texto que describe el sonido del Creeper.
     */
    @Override
    public String sonido() {
        return getNombre() + " chisporrotea: ssssss...";
    }
}
