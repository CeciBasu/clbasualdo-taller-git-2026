package py.edu.uc.lp3.clbasualdo.minecraft.domain;

/**
 * Descripcion:
 * Representa a un aldeano del juego, que es una entidad pasiva con una profesion.
 *
 * Responsabilidad:
 * Guarda la profesion y las esmeraldas del aldeano, y se encarga de comerciar y de reaccionar ante el peligro.
 */
public class Aldeano extends EntidadPasiva {
    /** Trabajo del aldeano, por ejemplo Herrero. */
    private final String profesion;
    /** Esmeraldas que el aldeano fue juntando al comerciar. */
    private int esmeraldas;

    /**
     * Descripcion:
     * Crea un aldeano sin profesion.
     */
    public Aldeano() { this("Sin Profesion"); }

    /**
     * Descripcion:
     * Crea un aldeano con una profesion. Si la profesion viene vacia o nula, se usa "Sin Profesion".
     *
     * Parametros:
     * profesion - El trabajo que va a tener el aldeano.
     */
    public Aldeano(String profesion) {
        super("Aldeano", 20);
        this.profesion = (profesion == null || profesion.isBlank()) ? "Sin Profesion" : profesion;
        this.esmeraldas = 0;
    }

    /**
     * Descripcion:
     * Devuelve la profesion del aldeano.
     *
     * Retorno:
     * El texto con la profesion.
     */
    public String getProfesion() { return profesion; }

    /**
     * Descripcion:
     * Devuelve cuantas esmeraldas tiene el aldeano.
     *
     * Retorno:
     * La cantidad de esmeraldas.
     */
    public int getEsmeraldas() { return esmeraldas; }

    /**
     * Descripcion:
     * Muestra un mensaje diciendo que el aldeano esta comerciando. No cambia sus esmeraldas.
     */
    public void comerciar() {
        System.out.println("El Aldeano (" + profesion + ") esta comerciando.");
    }

    /**
     * Descripcion:
     * Cierra un trato y suma las esmeraldas ofrecidas a las que ya tenia.
     * Lanza un error si la cantidad es negativa.
     *
     * Parametros:
     * esmeraldasOfrecidas - Cantidad de esmeraldas que se ganan en el trato.
     */
    public void comerciar(int esmeraldasOfrecidas) {
        if (esmeraldasOfrecidas < 0) {
            throw new IllegalArgumentException("Las esmeraldas ofrecidas no pueden ser negativas.");
        }
        esmeraldas += esmeraldasOfrecidas;
        System.out.printf("%s (%s) cierra un trato y ahora tiene %d esmeraldas.%n",
                getNombre(), profesion, esmeraldas);
    }

    /**
     * Descripcion:
     * Dice como reacciona el aldeano ante una amenaza: se asusta y se esconde.
     *
     * Retorno:
     * Un texto que describe la reaccion.
     */
    @Override
    public String reaccionar() {
        return getNombre() + " se asusta y corre a esconderse.";
    }
}
