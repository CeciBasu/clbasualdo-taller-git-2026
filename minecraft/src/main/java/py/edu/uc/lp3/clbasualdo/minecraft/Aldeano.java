package py.edu.uc.lp3.clbasualdo.minecraft;

public class Aldeano extends EntidadPasiva {
    private final String profesion;
    private int esmeraldas;

    public Aldeano() { this("Sin Profesion"); }

    public Aldeano(String profesion) {
        super("Aldeano", 20);
        this.profesion = (profesion == null || profesion.isBlank()) ? "Sin Profesion" : profesion;
        this.esmeraldas = 0;
    }

    public String getProfesion() { return profesion; }
    public int getEsmeraldas() { return esmeraldas; }

    public void comerciar() {
        System.out.println("El Aldeano (" + profesion + ") esta comerciando.");
    }

    /** Cierra un trato real y acumula esmeraldas. */
    public void comerciar(int esmeraldasOfrecidas) {
        if (esmeraldasOfrecidas < 0) {
            throw new IllegalArgumentException("Las esmeraldas ofrecidas no pueden ser negativas.");
        }
        esmeraldas += esmeraldasOfrecidas;
        System.out.printf("%s (%s) cierra un trato y ahora tiene %d esmeraldas.%n",
                getNombre(), profesion, esmeraldas);
    }

    @Override
    public String reaccionar() {
        return getNombre() + " se asusta y corre a esconderse.";
    }
}
