package py.edu.uc.lp3.clbasualdo.minecraft;

public class Zombie extends EntidadHostil {
    private boolean puertaRota;

    public Zombie() { super("Zombie", 20, 6, 1.5); }

    @Override
    public void atacar() {
        System.out.println("El Zombie ataca al jugador.");
    }

    public boolean isPuertaRota() { return puertaRota; }

    public void romperPuerta() {
        puertaRota = true;
        System.out.println(getNombre() + " derriba la puerta a golpes.");
    }

    @Override
    public String reaccionar() {
        return getNombre() + " gruñe y persigue al jugador.";
    }
}
