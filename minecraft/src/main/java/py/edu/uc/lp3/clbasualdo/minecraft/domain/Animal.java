package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import java.util.Locale;

/**
 * Descripción:
 * Representa a un animal del juego, como una vaca. Es una entidad pasiva.
 *
 * Responsabilidad:
 * Se encarga de que el animal pueda comer, ser domesticado y tener crias con otro de su misma especie.
 * También es UICapable y no reemplaza el sonido, así que usa el comportamiento por defecto de la interfaz.
 */
public class Animal extends EntidadPasiva implements UICapable {
    /** Tipo de animal, sirve para saber si dos animales se pueden reproducir. */
    private final String especie;
    /** Indica si el animal ya fue domesticado. */
    private boolean domesticado;

    /**
     * Descripción:
     * Crea un animal usando su nombre también como especie.
     *
     * Parámetros:
     * nombre - Nombre del animal.
     */
    public Animal(String nombre) { this(nombre, nombre); }

    /**
     * Descripción:
     * Crea un animal con nombre y especie. Si la especie viene vacía o nula, se usa el nombre.
     *
     * Parámetros:
     * nombre - Nombre del animal.
     * especie - Tipo de animal.
     */
    public Animal(String nombre, String especie) {
        super(nombre, 10);
        this.especie = (especie == null || especie.isBlank()) ? nombre : especie;
        this.domesticado = false;
    }

    /**
     * Descripción:
     * Devuelve la especie del animal.
     *
     * Retorno:
     * El texto con la especie.
     */
    public String getEspecie() { return especie; }

    /**
     * Descripción:
     * Dice si el animal está domesticado.
     *
     * Retorno:
     * true si está domesticado, false si no.
     */
    public boolean isDomesticado() { return domesticado; }

    /**
     * Descripción:
     * El animal come una cantidad normal de alimento (2 puntos) y recupera vida.
     */
    public void comer() {
        alimentarse(2);
    }

    /**
     * Descripción:
     * El animal come una cantidad que se elige y recupera esa misma cantidad de vida.
     *
     * Parámetros:
     * puntosAlimento - Cantidad de comida que come el animal.
     */
    public void comer(int puntosAlimento) {
        alimentarse(puntosAlimento);
    }

    /**
     * Descripción:
     * Método que usan los dos comer() para curar al animal.
     *
     * Parámetros:
     * puntos - Cantidad de vida que recupera.
     */
    private void alimentarse(int puntos) {
        curar(puntos);
        System.out.println(getNombre() + " está comiendo y recupera " + puntos + " de vida.");
    }

    /**
     * Descripción:
     * Marca al animal como domesticado.
     */
    public void domesticar() {
        domesticado = true;
        System.out.println(getNombre() + " ahora está domesticado.");
    }

    /**
     * Descripción:
     * Crea una cría con la pareja. Que sean de la misma especie y estén domesticados lo revisa
     * la capa service antes de enviar el mensaje.
     *
     * Parámetros:
     * pareja - El otro animal con el que se reproduce.
     *
     * Retorno:
     * El animal bebé que nace.
     */
    public Animal reproducirse(Animal pareja) {
        System.out.println(this.getNombre() + " y " + pareja.getNombre() + " tienen una cría.");
        return new Animal("Bebé " + especie, especie);
    }

    /**
     * Descripción:
     * Dice cómo reacciona el animal ante el jugador: sigue comiendo sin hacerle caso.
     *
     * Retorno:
     * Un texto que describe la reacción.
     */
    @Override
    public String reaccionar() {
        return getNombre() + " sigue comiendo, no le presta atención al jugador.";
    }

    /**
     * Descripción:
     * Devuelve la imagen con la que la UI dibuja al animal, según su especie.
     *
     * Retorno:
     * El nombre de la imagen del animal.
     */
    @Override
    public String imagen() {
        return getEspecie() + ".png";
    }

    /**
     * Descripción:
     * Devuelve la posición del animal formateada para la UI.
     *
     * Retorno:
     * Un texto con las coordenadas del animal.
     */
    @Override
    public String posicion() {
        return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", getX(), getY(), getZ());
    }

    /**
     * Descripción:
     * Devuelve la velocidad con la que se mueve el animal.
     *
     * Retorno:
     * La velocidad del animal.
     */
    @Override
    public double velocidad() {
        return 0.25;
    }
}
