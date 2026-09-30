package py.edu.uc.lp3.clbasualdo.minecraft;

/**
 * Descripcion:
 * Representa al Enderman, un enemigo que se teletransporta y ataca cuando el
 * jugador lo mira.
 *
 * Responsabilidad:
 * Se encarga de teletransportarse, de avisar que ataca al ser observado y de
 * responder como reacciona ante el jugador.
 */
public class Enderman extends EntidadHostil {
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
}
