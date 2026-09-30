package py.edu.uc.lp3.clbasualdo.minecraft.domain;

/**
 * Descripcion:
 * Clase base de las entidades enemigas, como el Zombie, el Esqueleto y el Creeper.
 *
 * Responsabilidad:
 * Guarda el danio y el rango de ataque de los enemigos y se encarga de golpear a otras entidades.
 */
public abstract class EntidadHostil extends Entidad {
    /** Cantidad de vida que quita cada golpe. */
    private final int danioAtaque;
    /** Distancia maxima a la que puede golpear. */
    private final double rangoAtaque;

    /**
     * Descripcion:
     * Crea una entidad hostil. Lanza un error si el danio o el rango no son mayores a 0.
     *
     * Parametros:
     * nombre - Nombre del enemigo.
     * vida - Vida inicial del enemigo.
     * danioAtaque - Danio que hace cada golpe.
     * rangoAtaque - Distancia maxima de ataque.
     */
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

    /**
     * Descripcion:
     * Devuelve el danio que hace el enemigo al atacar.
     *
     * Retorno:
     * El danio de ataque.
     */
    public int getDanioAtaque() { return danioAtaque; }

    /**
     * Descripcion:
     * Devuelve la distancia maxima a la que el enemigo puede atacar.
     *
     * Retorno:
     * El rango de ataque.
     */
    public double getRangoAtaque() { return rangoAtaque; }

    /**
     * Descripcion:
     * Metodo abstracto que muestra el gesto o sonido del ataque. Cada enemigo lo hace a su manera.
     */
    public abstract void atacar();

    /**
     * Descripcion:
     * Ataque de verdad: revisa que el enemigo y el objetivo esten vivos y que el objetivo este dentro del rango.
     * Si todo esta bien, hace el gesto de ataque y le quita vida al objetivo.
     * Si el enemigo esta muerto o el objetivo es nulo, lanza un error.
     *
     * Parametros:
     * objetivo - La entidad que se quiere golpear.
     */
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
