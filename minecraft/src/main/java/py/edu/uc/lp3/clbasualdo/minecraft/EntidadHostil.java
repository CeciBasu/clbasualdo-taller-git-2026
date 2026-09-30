package py.edu.uc.lp3.clbasualdo.minecraft;

public abstract class EntidadHostil extends Entidad {
    private final int danioAtaque;
    private final double rangoAtaque;

    public EntidadHostil(String nombre, int vida, int danioAtaque, double rangoAtaque) {
        super(nombre, vida);
        if (danioAtaque <= 0) {
            throw new IllegalArgumentException("El daño de ataque debe ser mayor a 0.");
        }
        if (rangoAtaque <= 0) {
            throw new IllegalArgumentException("El rango de ataque debe ser mayor a 0.");
        }
        this.danioAtaque = danioAtaque;
        this.rangoAtaque = rangoAtaque;
    }

    public int getDanioAtaque() { return danioAtaque; }
    public double getRangoAtaque() { return rangoAtaque; }

    /** Gesto/sonido de ataque, distinto para cada hija. */
    public abstract void atacar();

    /** Ataque real: valida que ambas partes estén vivas y dentro de rango antes de aplicar daño. */
    public void golpear(Entidad objetivo) {
        if (!estaVivo()) {
            throw new IllegalStateException(getNombre() + " no puede atacar: está muerto.");
        }
        if (objetivo == null) {
            throw new IllegalArgumentException("El objetivo no puede ser nulo.");
        }
        if (!objetivo.estaVivo()) {
            System.out.println(getNombre() + " no ataca: " + objetivo.getNombre() + " ya está muerto.");
            return;
        }
        double distancia = distanciaHacia(objetivo);
        if (distancia > rangoAtaque) {
            System.out.printf("%s no alcanza a %s (distancia %.1f > rango %.1f).%n",
                    getNombre(), objetivo.getNombre(), distancia, rangoAtaque);
            return;
        }
        atacar();
        objetivo.recibirDanio(danioAtaque);
        System.out.printf("%s inflige %d de daño a %s.%n", getNombre(), danioAtaque, objetivo.getNombre());
    }
}
