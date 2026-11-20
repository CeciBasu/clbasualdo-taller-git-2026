package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import java.util.Objects;

/**
 * Descripción:
 * Clase base de todo lo que existe en el mundo del juego: jugador, enemigos, aldeanos y animales.
 *
 * Responsabilidad:
 * Guarda lo que todas las entidades tienen en común (nombre, vida y posición) y cambia ese
 * estado cuando recibe un mensaje (recibir daño, curarse, moverse). No valida los datos que
 * recibe ni lanza excepciones: eso lo hace la capa service antes de enviarle el mensaje.
 */
public abstract class Entidad {
    /** Nombre de la entidad, no se puede cambiar. */
    private final String nombre;
    /** Vida actual de la entidad. */
    private int vida;
    /** Vida con la que empezó la entidad, es el límite al curarse. */
    private final int vidaMaxima;
    /** Posición en el eje X. */
    private double x;
    /** Posición en el eje Y (la altura). */
    private double y;
    /** Posición en el eje Z. */
    private double z;

    /**
     * Descripción:
     * Crea una entidad en la posición (0, 0, 0).
     *
     * Parámetros:
     * nombre - Nombre de la entidad.
     * vida - Vida inicial; el service se asegura de que sea mayor a 0.
     */
    public Entidad(String nombre, int vida) {
        this(nombre, vida, 0, 0, 0);
    }

    /**
     * Descripción:
     * Crea una entidad en una posición elegida. La vida inicial también es la vida máxima.
     *
     * Parámetros:
     * nombre - Nombre de la entidad.
     * vida - Vida inicial.
     * x - Posición inicial en X.
     * y - Posición inicial en Y.
     * z - Posición inicial en Z.
     */
    public Entidad(String nombre, int vida, double x, double y, double z) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Descripción:
     * Devuelve el nombre de la entidad.
     *
     * Retorno:
     * El nombre.
     */
    public String getNombre() { return nombre; }

    /**
     * Descripción:
     * Devuelve la vida actual de la entidad.
     *
     * Retorno:
     * La vida actual.
     */
    public int getVida() { return vida; }

    /**
     * Descripción:
     * Devuelve la vida máxima de la entidad.
     *
     * Retorno:
     * La vida máxima.
     */
    public int getVidaMaxima() { return vidaMaxima; }

    /**
     * Descripción:
     * Devuelve la posición en X.
     *
     * Retorno:
     * El valor de X.
     */
    public double getX() { return x; }

    /**
     * Descripción:
     * Devuelve la posición en Y.
     *
     * Retorno:
     * El valor de Y.
     */
    public double getY() { return y; }

    /**
     * Descripción:
     * Devuelve la posición en Z.
     *
     * Retorno:
     * El valor de Z.
     */
    public double getZ() { return z; }

    /**
     * Descripción:
     * Muestra por pantalla el nombre, la vida y la posición de la entidad.
     */
    public void mostrarInfo() {
        System.out.printf("Nombre: %s%n", nombre);
        System.out.printf("Vida: %d/%d%n", vida, vidaMaxima);
        System.out.printf("Posición: (%.1f, %.1f, %.1f)%n", x, y, z);
    }

    /**
     * Descripción:
     * Le resta vida a la entidad. La vida nunca baja de 0 y, si llega a 0, se avisa que murió.
     *
     * Parámetros:
     * danio - Cantidad de vida que se pierde.
     */
    public void recibirDanio(int danio) {
        vida = Math.max(0, vida - danio);
        if (!estaVivo()) {
            System.out.println(nombre + " ha muerto.");
        }
    }

    /**
     * Descripción:
     * Suma vida a la entidad sin pasarse de la vida máxima. Es protegido para que solo lo usen
     * las clases hijas, porque no todas las entidades se pueden curar desde afuera (un Creeper
     * no se regenera).
     *
     * Parámetros:
     * cantidad - Cantidad de vida que se recupera.
     */
    protected void curar(int cantidad) {
        vida = Math.min(vidaMaxima, vida + cantidad);
    }

    /**
     * Descripción:
     * Dice si la entidad sigue viva.
     *
     * Retorno:
     * true si tiene más de 0 de vida, false si no.
     */
    public boolean estaVivo() { return vida > 0; }

    /**
     * Descripción:
     * Mueve la entidad un paso hacia adelante en el eje X.
     */
    public void moverse() {
        moverse(1, 0, 0);
    }

    /**
     * Descripción:
     * Mueve la entidad sumando un desplazamiento a su posición actual.
     *
     * Parámetros:
     * dx - Cuánto se mueve en X.
     * dy - Cuánto se mueve en Y.
     * dz - Cuánto se mueve en Z.
     */
    public void moverse(double dx, double dy, double dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
        System.out.printf("%s se desplaza a (%.1f, %.1f, %.1f).%n", nombre, x, y, z);
    }

    /**
     * Descripción:
     * Cambia la posición de la entidad a una nueva, como el comando /tp del juego.
     *
     * Parámetros:
     * x - Nueva posición en X.
     * y - Nueva posición en Y.
     * z - Nueva posición en Z.
     */
    public void teletransportar(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        System.out.printf("%s se teletransporta a (%.1f, %.1f, %.1f).%n", nombre, x, y, z);
    }

    /**
     * Descripción:
     * Calcula la distancia en línea recta entre esta entidad y otra.
     *
     * Parámetros:
     * otra - La entidad hacia la que se mide la distancia.
     *
     * Retorno:
     * La distancia entre las dos entidades.
     */
    public double distanciaHacia(Entidad otra) {
        double dx = this.x - otra.x;
        double dy = this.y - otra.y;
        double dz = this.z - otra.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Descripción:
     * Hace que la entidad desaparezca del mundo.
     */
    public void desaparecer() {
        System.out.println(nombre + " desaparece del mundo.");
    }

    /**
     * Descripción:
     * Método abstracto: cada clase hija define cómo reacciona ante el jugador,
     * porque la clase padre no puede saberlo.
     *
     * Retorno:
     * Un texto que describe la reacción.
     */
    public abstract String reaccionar();

    /**
     * Descripción:
     * Arma un texto con los datos de la entidad (tipo, nombre, vida y posición).
     *
     * Retorno:
     * El texto con la información de la entidad.
     */
    @Override
    public String toString() {
        return String.format("%s[nombre=%s, vida=%d/%d, pos=(%.1f,%.1f,%.1f)]",
                getClass().getSimpleName(), nombre, vida, vidaMaxima, x, y, z);
    }

    /**
     * Descripción:
     * Compara dos entidades. Son iguales si tienen el mismo nombre y son de la misma clase.
     *
     * Parámetros:
     * o - El objeto con el que se compara.
     *
     * Retorno:
     * true si son iguales, false si no.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entidad)) return false;
        Entidad entidad = (Entidad) o;
        return Objects.equals(nombre, entidad.nombre) && getClass() == entidad.getClass();
    }

    /**
     * Descripción:
     * Calcula un número a partir del nombre y la clase. Va junto con equals().
     *
     * Retorno:
     * El número calculado.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre, getClass());
    }
}
