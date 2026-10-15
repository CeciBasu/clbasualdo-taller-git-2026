package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import java.util.Locale;

/**
 * Descripcion:
 * Representa al Enderman, un enemigo que se teletransporta y ataca cuando el
 * jugador lo mira.
 *
 * Responsabilidad:
 * Se encarga de teletransportarse, de avisar que ataca al ser observado y de
 * responder como reacciona ante el jugador. Tambien es UICapable para la UI.
 */
public class Enderman extends EntidadHostil implements UICapable {
    /**
     * Descripcion:
     * Crea un Enderman con 40 de vida, 7 de danio y 16.0 de rango de ataque.
     */
    public Enderman() {
        super("Enderman", 40, 7, 16.0);
    }

    /**
     * Descripcion:
     * Muestra un mensaje avisando que el Enderman ataca porque lo miraron.
     */
    @Override
    public void atacar() {
        System.out.println("El Enderman ataca al jugador al ser mirado.");
    }

    /**
     * Descripcion:
     * El Enderman se teletransporta fuera de la vista del jugador.
     */
    public void teletransportarse() {
        System.out.println(getNombre() + " se teletransporta.");
    }

    /**
     * Descripcion:
     * Dice como reacciona el Enderman: se teletransporta al ser observado.
     *
     * Retorno:
     * Un texto que describe la reaccion.
     */
    @Override
    public String reaccionar() {
        return getNombre() + " se teletransporta al ser observado.";
    }

    /**
     * Descripcion:
     * Devuelve la imagen con la que la UI dibuja al Enderman.
     *
     * Retorno:
     * El nombre de la imagen del Enderman.
     */
    @Override
    public String imagen() {
        return "enderman.png";
    }

    /**
     * Descripcion:
     * Devuelve la posicion del Enderman formateada para la UI.
     *
     * Retorno:
     * Un texto con las coordenadas del Enderman.
     */
    @Override
    public String posicion() {
        return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", getX(), getY(), getZ());
    }

    /**
     * Descripcion:
     * Devuelve la velocidad con la que se mueve el Enderman.
     *
     * Retorno:
     * La velocidad del Enderman.
     */
    @Override
    public double velocidad() {
        return 0.30;
    }

    /**
     * Descripcion:
     * Reemplaza el sonido generico del default con el propio del Enderman.
     *
     * Retorno:
     * Un texto que describe el sonido del Enderman.
     */
    @Override
    public String sonido() {
        return getNombre() + " emite un zumbido: vwoop...";
    }
}
