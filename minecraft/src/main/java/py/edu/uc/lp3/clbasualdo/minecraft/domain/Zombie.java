package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import java.util.Locale;

/**
 * Descripcion:
 * Representa al Zombie, un enemigo que persigue al jugador y lo ataca de cerca.
 *
 * Responsabilidad:
 * Se encarga de atacar en corto alcance y de romper puertas. Tambien es UICapable
 * y reemplaza el sonido por defecto de la interfaz con el suyo propio.
 */
public class Zombie extends EntidadHostil implements UICapable {
    /** Indica si el zombie ya rompio una puerta. */
    private boolean puertaRota;

    /**
     * Descripcion:
     * Crea un Zombie con 20 de vida, 6 de danio y 1.5 de rango de ataque.
     */
    public Zombie() { super("Zombie", 20, 6, 1.5); }

    /**
     * Descripcion:
     * Muestra un mensaje del ataque del zombie.
     */
    @Override
    public void atacar() {
        System.out.println("El Zombie ataca al jugador.");
    }

    /**
     * Descripcion:
     * Dice si el zombie ya rompio una puerta.
     *
     * Retorno:
     * true si la puerta esta rota, false si no.
     */
    public boolean isPuertaRota() { return puertaRota; }

    /**
     * Descripcion:
     * El zombie rompe una puerta a golpes y queda marcado como que la rompio.
     */
    public void romperPuerta() {
        puertaRota = true;
        System.out.println(getNombre() + " derriba la puerta a golpes.");
    }

    /**
     * Descripcion:
     * Dice como reacciona el zombie: hace ruidos y persigue al jugador.
     *
     * Retorno:
     * Un texto que describe la reaccion.
     */
    @Override
    public String reaccionar() {
        return getNombre() + " gruñe y persigue al jugador.";
    }

    /**
     * Descripcion:
     * Devuelve la imagen con la que la UI dibuja al Zombie.
     *
     * Retorno:
     * El nombre de la imagen del Zombie.
     */
    @Override
    public String imagen() {
        return "zombie.png";
    }

    /**
     * Descripcion:
     * Devuelve la posicion del Zombie formateada para la UI.
     *
     * Retorno:
     * Un texto con las coordenadas del Zombie.
     */
    @Override
    public String posicion() {
        return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", getX(), getY(), getZ());
    }

    /**
     * Descripcion:
     * Devuelve la velocidad con la que se mueve el Zombie.
     *
     * Retorno:
     * La velocidad del Zombie.
     */
    @Override
    public double velocidad() {
        return 0.23;
    }

    /**
     * Descripcion:
     * Reemplaza el sonido generico del default con el propio del Zombie.
     *
     * Retorno:
     * Un texto que describe el sonido del Zombie.
     */
    @Override
    public String sonido() {
        return getNombre() + " gime: grrrr...";
    }
}
