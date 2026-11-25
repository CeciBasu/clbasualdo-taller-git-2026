package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import java.util.Locale;

/**
 * Descripción:
 * Representa al Esqueleto, un enemigo que ataca de lejos con flechas.
 *
 * Responsabilidad:
 * Lleva la cuenta de sus flechas, las gasta al atacar y permite recargarlas.
 * También tiene el mensaje disparar() sobrecargado: sin distancia y con distancia,
 * y es UICapable para poder dibujarse en la UI.
 */
public class Esqueleto extends EntidadHostil implements UICapable {
    /** Flechas que le quedan al esqueleto. */
    private int flechas;

    /**
     * Descripción:
     * Crea un esqueleto normal llamado "Esqueleto" con 20 de vida.
     */
    public Esqueleto() { this("Esqueleto", 20); }

    /**
     * Descripción:
     * Crea un esqueleto con nombre y vida elegidos. Siempre hace 4 de daño, ataca hasta 8.0 de distancia y empieza con 16 flechas.
     *
     * Parámetros:
     * nombre - Nombre del esqueleto.
     * vida - Vida inicial del esqueleto.
     */
    public Esqueleto(String nombre, int vida) {
        super(nombre, vida, 4, 8.0);
        this.flechas = 16;
    }

    /**
     * Descripción:
     * Devuelve cuántas flechas tiene el esqueleto.
     *
     * Retorno:
     * La cantidad de flechas.
     */
    public int getFlechas() { return flechas; }

    /**
     * Descripción:
     * Le agrega flechas al esqueleto.
     *
     * Parámetros:
     * cantidad - Cantidad de flechas que se le agregan.
     */
    public void recargar(int cantidad) {
        flechas += cantidad;
        System.out.println(getNombre() + " recarga, ahora tiene " + flechas + " flechas.");
    }

    /**
     * Descripción:
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
     * Descripción:
     * Primera versión de disparar(): el esqueleto dispara sin que le digan a quien le
     * dispara. Como no se le pasa distancia, no hay nada que revisar y siempre gasta
     * una flecha (si es que le queda alguna).
     *
     * Retorno:
     * Un texto que dice que dispara y cuántas flechas le quedan.
     */
    public String disparar() {
        return disparar(0.0);
    }

    /**
     * Descripción:
     * Segunda versión de disparar(): el mismo disparo, pero indicando a qué distancia
     * está el objetivo. Ahí aparece el contexto que la otra versión no tiene: si la
     * distancia es mayor al rango de ataque, la flecha se pierde y no se gasta. Si la
     * distancia está dentro del rango, se comporta igual que disparar(). Que la distancia
     * no sea negativa lo revisa la capa service.
     *
     * Parámetros:
     * distancia - Distancia hasta el objetivo, tiene que ser 0 o mayor.
     *
     * Retorno:
     * Un texto que dice que dispara, que no alcanza, o que se queda sin flechas.
     */
    public String disparar(double distancia) {
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
     * Descripción:
     * Dice cómo reacciona el esqueleto: retrocede y dispara.
     *
     * Retorno:
     * Un texto que describe la reacción.
     */
    @Override
    public String reaccionar() {
        return getNombre() + " retrocede y dispara una flecha.";
    }

    /**
     * Descripción:
     * Devuelve la imagen con la que la UI dibuja al Esqueleto.
     *
     * Retorno:
     * El nombre de la imagen del Esqueleto.
     */
    @Override
    public String imagen() {
        return "esqueleto.png";
    }

    /**
     * Descripción:
     * Devuelve la posición del Esqueleto formateada para la UI.
     *
     * Retorno:
     * Un texto con las coordenadas del Esqueleto.
     */
    @Override
    public String posicion() {
        return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", getX(), getY(), getZ());
    }

    /**
     * Descripción:
     * Devuelve la velocidad con la que se mueve el Esqueleto.
     *
     * Retorno:
     * La velocidad del Esqueleto.
     */
    @Override
    public double velocidad() {
        return 0.25;
    }

    /**
     * Descripción:
     * Reemplaza el sonido genérico del default con el propio del Esqueleto.
     *
     * Retorno:
     * Un texto que describe el sonido del Esqueleto.
     */
    @Override
    public String sonido() {
        return getNombre() + " tensa el arco: creak...";
    }
}
