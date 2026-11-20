package py.edu.uc.lp3.clbasualdo.minecraft.domain;

/**
 * Descripción:
 * Clase base de las entidades enemigas, como el Zombie, el Esqueleto y el Creeper.
 *
 * Responsabilidad:
 * Guarda el daño y el rango de ataque de los enemigos y se encarga de golpear a otras entidades.
 */
public abstract class EntidadHostil extends Entidad {
    /** Cantidad de vida que quita cada golpe. */
    private final int danioAtaque;
    /** Distancia máxima a la que puede golpear. */
    private final double rangoAtaque;

    /**
     * Descripción:
     * Crea una entidad hostil con su daño y su rango de ataque.
     *
     * Parámetros:
     * nombre - Nombre del enemigo.
     * vida - Vida inicial del enemigo.
     * danioAtaque - Danio que hace cada golpe.
     * rangoAtaque - Distancia máxima de ataque.
     */
    public EntidadHostil(String nombre, int vida, int danioAtaque, double rangoAtaque) {
        super(nombre, vida);
        this.danioAtaque = danioAtaque;
        this.rangoAtaque = rangoAtaque;
    }

    /**
     * Descripción:
     * Devuelve el daño que hace el enemigo al atacar.
     *
     * Retorno:
     * El daño de ataque.
     */
    public int getDanioAtaque() { return danioAtaque; }

    /**
     * Descripción:
     * Devuelve la distancia máxima a la que el enemigo puede atacar.
     *
     * Retorno:
     * El rango de ataque.
     */
    public double getRangoAtaque() { return rangoAtaque; }

    /**
     * Descripción:
     * Método abstracto que muestra el gesto o sonido del ataque. Cada enemigo lo hace a su manera.
     */
    public abstract void atacar();

    /**
     * Descripción:
     * Ataque de verdad: si el objetivo está vivo y dentro del rango, hace el gesto de ataque
     * y le quita vida al objetivo. Si no, solo lo informa. Que el atacante esté vivo y que el
     * objetivo exista lo revisa la capa service antes de enviar el mensaje.
     *
     * Parámetros:
     * objetivo - La entidad que se quiere golpear.
     */
    public void golpear(Entidad objetivo) {
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
