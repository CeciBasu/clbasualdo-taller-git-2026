package py.edu.uc.lp3.clbasualdo.minecraft;

public class Animal extends EntidadPasiva {
    private final String especie;
    private boolean domesticado;

    public Animal(String nombre) { this(nombre, nombre); }

    public Animal(String nombre, String especie) {
        super(nombre, 10);
        this.especie = (especie == null || especie.isBlank()) ? nombre : especie;
        this.domesticado = false;
    }

    public String getEspecie() { return especie; }
    public boolean isDomesticado() { return domesticado; }

    public void comer() {
        alimentarse(2);
    }

    /** Comer una cantidad concreta de alimento y regenerar vida en base a eso. */
    public void comer(int puntosAlimento) {
        alimentarse(puntosAlimento);
    }

    private void alimentarse(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("Los puntos de alimento no pueden ser negativos.");
        }
        curar(puntos);
        System.out.println(getNombre() + " esta comiendo y recupera " + puntos + " de vida.");
    }

    public void domesticar() {
        domesticado = true;
        System.out.println(getNombre() + " ahora esta domesticado.");
    }

    /** Genera una cria si ambos animales son de la misma especie y estan domesticados. */
    public Animal reproducirse(Animal pareja) {
        if (pareja == null || !pareja.getEspecie().equals(this.especie)) {
            throw new IllegalArgumentException("Solo se puede reproducir con la misma especie.");
        }
        if (!this.domesticado || !pareja.domesticado) {
            throw new IllegalStateException("Ambos animales deben estar domesticados para reproducirse.");
        }
        System.out.println(this.getNombre() + " y " + pareja.getNombre() + " tienen una cria.");
        return new Animal("Bebe " + especie, especie);
    }

    @Override
    public String reaccionar() {
        return getNombre() + " sigue comiendo, no le presta atencion al jugador.";
    }
}
