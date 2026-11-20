package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import java.util.Locale;

/**
 * Descripción:
 * Representa al Zombie, un enemigo que persigue al jugador y lo ataca de cerca.
 *
 * Responsabilidad:
 * Se encarga de atacar en corto alcance y de romper puertas. También es UICapable
 * y reemplaza el sonido por defecto de la interfaz con el suyo propio.
 */
public class Zombie extends EntidadHostil implements UICapable {
    /** Indica si el zombie ya rompió una puerta. */
    private boolean puertaRota;

    /**
     * Descripción:
     * Crea un Zombie con 20 de vida, 6 de daño y 1.5 de rango de ataque.
     */
    public Zombie() { super("Zombie", 20, 6, 1.5); }

    /**
     * Descripción:
     * Muestra un mensaje del ataque del zombie.
     */
    @Override
    public void atacar() {
        System.out.println("El Zombie ataca al jugador.");
    }

    /**
     * Descripción:
     * Dice si el zombie ya rompió una puerta.
     *
     * Retorno:
     * true si la puerta está rota, false si no.
     */
    public boolean isPuertaRota() { return puertaRota; }

    /**
     * Descripción:
     * El zombie rompe una puerta a golpes y queda marcado como que la rompió.
     */
    public void romperPuerta() {
        puertaRota = true;
        System.out.println(getNombre() + " derriba la puerta a golpes.");
    }

    /**
     * Descripción:
     * Dice cómo reacciona el zombie: hace ruidos y persigue al jugador.
     *
     * Retorno:
     * Un texto que describe la reacción.
     */
    @Override
    public String reaccionar() {
        return getNombre() + " gruñe y persigue al jugador.";
    }

    /**
     * Descripción:
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
     * Descripción:
     * Devuelve la posición del Zombie formateada para la UI.
     *
     * Retorno:
     * Un texto con las coordenadas del Zombie.
     */
    @Override
    public String posicion() {
        return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", getX(), getY(), getZ());
    }

    /**
     * Descripción:
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
     * Descripción:
     * Reemplaza el sonido genérico del default con el propio del Zombie.
     *
     * Retorno:
     * Un texto que describe el sonido del Zombie.
     */
    @Override
    public String sonido() {
        return getNombre() + " gime: grrrr...";
    }
}
