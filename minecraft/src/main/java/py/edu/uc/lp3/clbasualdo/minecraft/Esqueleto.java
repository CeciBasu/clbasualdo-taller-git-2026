package py.edu.uc.lp3.clbasualdo.minecraft;

/**
 * Descripcion:
 * Representa al Esqueleto, un enemigo que ataca de lejos con flechas.
 *
 * Responsabilidad:
 * Lleva la cuenta de sus flechas, las gasta al atacar y permite recargarlas.
 */
public class Esqueleto extends EntidadHostil {
    /** Flechas que le quedan al esqueleto. */
    private int flechas;

    /**
     * Descripcion:
     * Crea un esqueleto normal llamado "Esqueleto" con 20 de vida.
     */
    public Esqueleto() { this("Esqueleto", 20); }

    /**
     * Descripcion:
     * Crea un esqueleto con nombre y vida elegidos. Siempre hace 4 de danio, ataca hasta 8.0 de distancia y empieza con 16 flechas.
     *
     * Parametros:
     * nombre - Nombre del esqueleto.
     * vida - Vida inicial del esqueleto.
     */
    public Esqueleto(String nombre, int vida) {
        super(nombre, vida, 4, 8.0);
        this.flechas = 16;
    }

    /**
     * Descripcion:
     * Devuelve cuantas flechas tiene el esqueleto.
     *
     * Retorno:
     * La cantidad de flechas.
     */
    public int getFlechas() { return flechas; }

    /**
     * Descripcion:
     * Le agrega flechas al esqueleto. Lanza un error si la cantidad es negativa.
     *
     * Parametros:
     * cantidad - Cantidad de flechas que se le agregan.
     */
    public void recargar(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad a recargar no puede ser negativa.");
        }
        flechas += cantidad;
        System.out.println(getNombre() + " recarga, ahora tiene " + flechas + " flechas.");
    }

    /**
     * Descripcion:
     * El esqueleto dispara una flecha y se le gasta una. Si no le quedan, ataca cuerpo a cuerpo.
     */
    @Override
    public void atacar() {
        if (flechas <= 0) {
            System.out.println("El Esqueleto no tiene flechas y ataca cuerpo a cuerpo.");
            return;
        }
        flechas--;
        System.out.println("El Esqueleto dispara una flecha. Le quedan " + flechas + ".");
    }

    /**
     * Descripcion:
     * Dice como reacciona el esqueleto: retrocede y dispara.
     *
     * Retorno:
     * Un texto que describe la reaccion.
     */
    @Override
    public String reaccionar() {
        return getNombre() + " retrocede y dispara una flecha.";
    }
}
