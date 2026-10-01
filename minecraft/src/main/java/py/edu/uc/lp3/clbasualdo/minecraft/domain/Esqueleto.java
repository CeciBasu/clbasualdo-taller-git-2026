package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import java.util.Locale;

/**
 * Descripcion:
 * Representa al Esqueleto, un enemigo que ataca de lejos con flechas.
 *
 * Responsabilidad:
 * Lleva la cuenta de sus flechas, las gasta al atacar y permite recargarlas.
 * Tambien tiene el mensaje disparar() sobrecargado: sin distancia y con distancia.
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
     * Primera version de disparar(): el esqueleto dispara sin que le digan a quien le
     * dispara. Como no se le pasa distancia, no hay nada que revisar y siempre gasta
     * una flecha (si es que le queda alguna).
     *
     * Retorno:
     * Un texto que dice que dispara y cuantas flechas le quedan.
     */
    public String disparar() {
        return disparar(0.0);
    }

    /**
     * Descripcion:
     * Segunda version de disparar(): el mismo disparo, pero indicando a que distancia
     * esta el objetivo. Ahi aparece el contexto que la otra version no tiene: si la
     * distancia es mayor al rango de ataque, la flecha se pierde y no se gasta, y si
     * la distancia viene negativa se la rechaza. Si la distancia esta dentro del
     * rango, se comporta igual que disparar().
     *
     * Parametros:
     * distancia - Distancia hasta el objetivo, tiene que ser 0 o mayor.
     *
     * Retorno:
     * Un texto que dice que dispara, que no alcanza, o que se queda sin flechas.
     */
    public String disparar(double distancia) {
        if (distancia < 0) {
            throw new IllegalArgumentException("La distancia no puede ser negativa.");
        }
        if (flechas <= 0) {
            return getNombre() + " no tiene flechas y ataca cuerpo a cuerpo.";
        }
        if (distancia > getRangoAtaque()) {
            return String.format(Locale.ROOT, "%s no alcanza a disparar a %.1f (rango %.1f) y no gasta flecha.",
                    getNombre(), distancia, getRangoAtaque());
        }
        flechas--;
        return String.format(Locale.ROOT, "%s dispara una flecha a %.1f. Le quedan %d.",
                getNombre(), distancia, flechas);
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
