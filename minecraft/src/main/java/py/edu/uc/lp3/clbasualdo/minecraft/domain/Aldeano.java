package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import java.util.Locale;

/**
 * Descripción:
 * Representa a un aldeano del juego, que es una entidad pasiva con una profesión.
 *
 * Responsabilidad:
 * Guarda la profesión y las esmeraldas del aldeano, y se encarga de comerciar y de reaccionar ante el peligro.
 * También es UICapable para poder dibujarse en la UI.
 */
public class Aldeano extends EntidadPasiva implements UICapable {
    /** Trabajo del aldeano, por ejemplo Herrero. */
    private final String profesion;
    /** Esmeraldas que el aldeano fue juntando al comerciar. */
    private int esmeraldas;

    /**
     * Descripción:
     * Crea un aldeano sin profesión.
     */
    public Aldeano() { this("Sin Profesión"); }

    /**
     * Descripción:
     * Crea un aldeano con una profesión. Si la profesión viene vacía o nula, se usa "Sin Profesión".
     *
     * Parámetros:
     * profesion - El trabajo que va a tener el aldeano.
     */
    public Aldeano(String profesion) {
        super("Aldeano", 20);
        this.profesion = (profesion == null || profesion.isBlank()) ? "Sin Profesión" : profesion;
        this.esmeraldas = 0;
    }

    /**
     * Descripción:
     * Devuelve la profesión del aldeano.
     *
     * Retorno:
     * El texto con la profesión.
     */
    public String getProfesion() { return profesion; }

    /**
     * Descripción:
     * Devuelve cuántas esmeraldas tiene el aldeano.
     *
     * Retorno:
     * La cantidad de esmeraldas.
     */
    public int getEsmeraldas() { return esmeraldas; }

    /**
     * Descripción:
     * Muestra un mensaje diciendo que el aldeano está comerciando. No cambia sus esmeraldas.
     */
    public void comerciar() {
        System.out.println("El Aldeano (" + profesion + ") está comerciando.");
    }

    /**
     * Descripción:
     * Cierra un trato y suma las esmeraldas ofrecidas a las que ya tenía.
     *
     * Parámetros:
     * esmeraldasOfrecidas - Cantidad de esmeraldas que se ganan en el trato.
     */
    public void comerciar(int esmeraldasOfrecidas) {
        esmeraldas += esmeraldasOfrecidas;
        System.out.printf("%s (%s) cierra un trato y ahora tiene %d esmeraldas.%n",
                getNombre(), profesion, esmeraldas);
    }

    /**
     * Descripción:
     * Dice cómo reacciona el aldeano ante una amenaza: se asusta y se esconde.
     *
     * Retorno:
     * Un texto que describe la reacción.
     */
    @Override
    public String reaccionar() {
        return getNombre() + " se asusta y corre a esconderse.";
    }

    /**
     * Descripción:
     * Devuelve la imagen con la que la UI dibuja al Aldeano.
     *
     * Retorno:
     * El nombre de la imagen del Aldeano.
     */
    @Override
    public String imagen() {
        return "aldeano.png";
    }

    /**
     * Descripción:
     * Devuelve la posición del Aldeano formateada para la UI.
     *
     * Retorno:
     * Un texto con las coordenadas del Aldeano.
     */
    @Override
    public String posicion() {
        return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", getX(), getY(), getZ());
    }

    /**
     * Descripción:
     * Devuelve la velocidad con la que se mueve el Aldeano.
     *
     * Retorno:
     * La velocidad del Aldeano.
     */
    @Override
    public double velocidad() {
        return 0.20;
    }

    /**
     * Descripción:
     * Reemplaza el sonido genérico del default con el propio del Aldeano.
     *
     * Retorno:
     * Un texto que describe el sonido del Aldeano.
     */
    @Override
    public String sonido() {
        return getNombre() + " murmura: hmm...";
    }
}
