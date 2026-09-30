package py.edu.uc.lp3.clbasualdo.minecraft;

import java.util.List;

public class Creeper extends EntidadHostil {
    private static final double RADIO_EXPLOSION = 3.0;
    private boolean detonado;

    public Creeper() { super("Creeper", 20, 25, 3.0); }

    @Override
    public void atacar() {
        System.out.println("El Creeper está a punto de explotar.");
    }

    public boolean isDetonado() { return detonado; }

    /** Explota dañando a toda entidad viva dentro del radio de explosión y se autodestruye. */
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
                if (e != this && e.estaVivo() && distanciaHacia(e) <= RADIO_EXPLOSION) {
                    e.recibirDanio(getDanioAtaque());
                    System.out.printf("La explosion alcanza a %s (%d de daño).%n", e.getNombre(), getDanioAtaque());
                }
            }
        }
        detonado = true;
        recibirDanio(getVida());
    }

    @Override
    public String reaccionar() {
        return getNombre() + " se acerca silbando y está a punto de explotar.";
    }
}
