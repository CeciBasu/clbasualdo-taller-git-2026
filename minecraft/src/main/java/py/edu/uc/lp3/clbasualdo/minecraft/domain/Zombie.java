package py.edu.uc.lp3.clbasualdo.minecraft.domain;

/**
 * Descripcion:
 * Representa al Zombie, un enemigo que persigue al jugador y lo ataca de cerca.
 *
 * Responsabilidad:
 * Se encarga de atacar en corto alcance y de romper puertas.
 */
public class Zombie extends EntidadHostil {
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
}
