package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import java.util.List;
import java.util.Locale;

/**
 * Descripcion:
 * Representa al Creeper, un enemigo que se acerca al jugador y explota.
 *
 * Responsabilidad:
 * Se encarga de explotar, lastimar a las entidades que esten cerca y destruirse a si mismo.
 * Tambien es UICapable para poder dibujarse en la UI.
 */
public class Creeper extends EntidadHostil implements UICapable {
    /** Distancia maxima a la que la explosion lastima a otras entidades. */
    private static final double RADIO_EXPLOSION = 3.0;
    /** Indica si el Creeper ya exploto. */
    private boolean detonado;

    /**
     * Descripcion:
     * Crea un Creeper con 20 de vida, 25 de danio y 3.0 de rango de ataque.
     */
    public Creeper() { super("Creeper", 20, 25, 3.0); }

    /**
     * Descripcion:
     * Muestra un mensaje avisando que el Creeper esta por explotar.
     */
    @Override
    public void atacar() {
        System.out.println("El Creeper está a punto de explotar.");
    }

    /**
     * Descripcion:
     * Dice si el Creeper ya exploto.
     *
     * Retorno:
     * true si ya exploto, false si no.
     */
    public boolean isDetonado() { return detonado; }

    /**
     * Descripcion:
     * El Creeper explota y lastima a todas las entidades vivas que esten dentro del radio de explosion.
     * Despues de explotar se muere. Si ya exploto no hace nada, y si esta muerto lanza un error.
     *
     * Parametros:
     * cercanas - Lista de entidades que pueden ser alcanzadas por la explosion.
     */
    public void explotar(List<Entidad> cercanas) {
        if (detonado) {
            System.out.println(getNombre() + " ya explotó.");
            return;
        }
        if (!estaVivo()) {
            throw new IllegalStateException(getNombre() + " no puede explotar: está muerto.");
        }
        System.out.println(getNombre() + " explota!");
        if (cercanas != null) {
            for (Entidad e : cercanas) {
                // No se lastima a si mismo aca, eso se hace al final
                if (e != this && e.estaVivo() && distanciaHacia(e) <= RADIO_EXPLOSION) {
                    e.recibirDanio(getDanioAtaque());
                    System.out.printf("La explosion alcanza a %s (%d de daño).%n", e.getNombre(), getDanioAtaque());
                }
            }
        }
        detonado = true;
        // Se quita toda su vida para que muera
        recibirDanio(getVida());
    }

    /**
     * Descripcion:
     * Dice como reacciona el Creeper: se acerca y esta por explotar.
     *
     * Retorno:
     * Un texto que describe la reaccion.
     */
    @Override
    public String reaccionar() {
        return getNombre() + " se acerca silbando y está a punto de explotar.";
    }

    /**
     * Descripcion:
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
     * Descripcion:
     * Devuelve la posicion del Creeper formateada para la UI.
     *
     * Retorno:
     * Un texto con las coordenadas del Creeper.
     */
    @Override
    public String posicion() {
        return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", getX(), getY(), getZ());
    }

    /**
     * Descripcion:
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
     * Descripcion:
     * Reemplaza el sonido generico del default con el propio del Creeper.
     *
     * Retorno:
     * Un texto que describe el sonido del Creeper.
     */
    @Override
    public String sonido() {
        return getNombre() + " chisporrotea: ssssss...";
    }
}
