package py.edu.uc.lp3.clbasualdo.minecraft.domain;

/**
 * Descripcion:
 * Representa a un animal del juego, como una vaca. Es una entidad pasiva.
 *
 * Responsabilidad:
 * Se encarga de que el animal pueda comer, ser domesticado y tener crias con otro de su misma especie.
 */
public class Animal extends EntidadPasiva {
    /** Tipo de animal, sirve para saber si dos animales se pueden reproducir. */
    private final String especie;
    /** Indica si el animal ya fue domesticado. */
    private boolean domesticado;

    /**
     * Descripcion:
     * Crea un animal usando su nombre tambien como especie.
     *
     * Parametros:
     * nombre - Nombre del animal.
     */
    public Animal(String nombre) { this(nombre, nombre); }

    /**
     * Descripcion:
     * Crea un animal con nombre y especie. Si la especie viene vacia o nula, se usa el nombre.
     *
     * Parametros:
     * nombre - Nombre del animal.
     * especie - Tipo de animal.
     */
    public Animal(String nombre, String especie) {
        super(nombre, 10);
        this.especie = (especie == null || especie.isBlank()) ? nombre : especie;
        this.domesticado = false;
    }

    /**
     * Descripcion:
     * Devuelve la especie del animal.
     *
     * Retorno:
     * El texto con la especie.
     */
    public String getEspecie() { return especie; }

    /**
     * Descripcion:
     * Dice si el animal esta domesticado.
     *
     * Retorno:
     * true si esta domesticado, false si no.
     */
    public boolean isDomesticado() { return domesticado; }

    /**
     * Descripcion:
     * El animal come una cantidad normal de alimento (2 puntos) y recupera vida.
     */
    public void comer() {
        alimentarse(2);
    }

    /**
     * Descripcion:
     * El animal come una cantidad que se elige y recupera esa misma cantidad de vida.
     *
     * Parametros:
     * puntosAlimento - Cantidad de comida que come el animal.
     */
    public void comer(int puntosAlimento) {
        alimentarse(puntosAlimento);
    }

    /**
     * Descripcion:
     * Metodo que usan los dos comer() para curar al animal. Lanza un error si los puntos son negativos.
     *
     * Parametros:
     * puntos - Cantidad de vida que recupera.
     */
    private void alimentarse(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("Los puntos de alimento no pueden ser negativos.");
        }
        curar(puntos);
        System.out.println(getNombre() + " esta comiendo y recupera " + puntos + " de vida.");
    }

    /**
     * Descripcion:
     * Marca al animal como domesticado.
     */
    public void domesticar() {
        domesticado = true;
        System.out.println(getNombre() + " ahora esta domesticado.");
    }

    /**
     * Descripcion:
     * Crea una cria si los dos animales son de la misma especie y estan domesticados.
     * Si no se cumple algo de eso, lanza un error.
     *
     * Parametros:
     * pareja - El otro animal con el que se reproduce.
     *
     * Retorno:
     * El animal bebe que nace.
     */
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

    /**
     * Descripcion:
     * Dice como reacciona el animal ante el jugador: sigue comiendo sin hacerle caso.
     *
     * Retorno:
     * Un texto que describe la reaccion.
     */
    @Override
    public String reaccionar() {
        return getNombre() + " sigue comiendo, no le presta atencion al jugador.";
    }
}
