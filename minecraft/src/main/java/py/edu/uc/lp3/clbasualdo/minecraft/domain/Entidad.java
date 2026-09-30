package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import java.util.Objects;

/**
 * Descripcion:
 * Clase base de todo lo que existe en el mundo del juego: jugador, enemigos, aldeanos y animales.
 *
 * Responsabilidad:
 * Guarda lo que todas las entidades tienen en comun (nombre, vida y posicion) y se encarga
 * de recibir danio, curarse, moverse y calcular distancias.
 */
public abstract class Entidad {
    /** Nombre de la entidad, no se puede cambiar. */
    private final String nombre;
    /** Vida actual de la entidad. */
    private int vida;
    /** Vida con la que empezo la entidad, es el limite al curarse. */
    private final int vidaMaxima;
    /** Posicion en el eje X. */
    private double x;
    /** Posicion en el eje Y (la altura). */
    private double y;
    /** Posicion en el eje Z. */
    private double z;

    /**
     * Descripcion:
     * Crea una entidad en la posicion (0, 0, 0).
     *
     * Parametros:
     * nombre - Nombre de la entidad.
     * vida - Vida inicial, tiene que ser mayor a 0.
     */
    public Entidad(String nombre, int vida) {
        this(nombre, vida, 0, 0, 0);
    }

    /**
     * Descripcion:
     * Crea una entidad en una posicion elegida. Lanza un error si el nombre esta vacio o la vida no es mayor a 0.
     *
     * Parametros:
     * nombre - Nombre de la entidad.
     * vida - Vida inicial, tiene que ser mayor a 0.
     * x - Posicion inicial en X.
     * y - Posicion inicial en Y.
     * z - Posicion inicial en Z.
     */
    public Entidad(String nombre, int vida, double x, double y, double z) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (vida <= 0) {
            throw new IllegalArgumentException("La vida inicial debe ser mayor a 0.");
        }
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Descripcion:
     * Devuelve el nombre de la entidad.
     *
     * Retorno:
     * El nombre.
     */
    public String getNombre() { return nombre; }

    /**
     * Descripcion:
     * Devuelve la vida actual de la entidad.
     *
     * Retorno:
     * La vida actual.
     */
    public int getVida() { return vida; }

    /**
     * Descripcion:
     * Devuelve la vida maxima de la entidad.
     *
     * Retorno:
     * La vida maxima.
     */
    public int getVidaMaxima() { return vidaMaxima; }

    /**
     * Descripcion:
     * Devuelve la posicion en X.
     *
     * Retorno:
     * El valor de X.
     */
    public double getX() { return x; }

    /**
     * Descripcion:
     * Devuelve la posicion en Y.
     *
     * Retorno:
     * El valor de Y.
     */
    public double getY() { return y; }

    /**
     * Descripcion:
     * Devuelve la posicion en Z.
     *
     * Retorno:
     * El valor de Z.
     */
    public double getZ() { return z; }

    /**
     * Descripcion:
     * Muestra por pantalla el nombre, la vida y la posicion de la entidad.
     */
    public void mostrarInfo() {
        System.out.printf("Nombre: %s%n", nombre);
        System.out.printf("Vida: %d/%d%n", vida, vidaMaxima);
        System.out.printf("Posición: (%.1f, %.1f, %.1f)%n", x, y, z);
    }

    /**
     * Descripcion:
     * Le resta vida a la entidad. La vida nunca baja de 0 y si llega a 0 se avisa que murio.
     * Lanza un error si el danio es negativo.
     *
     * Parametros:
     * danio - Cantidad de vida que se pierde.
     */
    public void recibirDanio(int danio) {
        if (danio < 0) {
            throw new IllegalArgumentException("El daño no puede ser negativo.");
        }
        vida -= danio;
        if (vida < 0) vida = 0;
        if (!estaVivo()) {
            System.out.println(nombre + " ha muerto.");
        }
    }

    /**
     * Descripcion:
     * Suma vida a la entidad sin pasarse de la vida maxima. Es protegido para que solo lo usen
     * las clases hijas, porque no todas las entidades se pueden curar desde afuera (un Creeper no se regenera).
     * Lanza un error si la cantidad es negativa.
     *
     * Parametros:
     * cantidad - Cantidad de vida que se recupera.
     */
    protected void curar(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad a curar no puede ser negativa.");
        }
        vida = Math.min(vidaMaxima, vida + cantidad);
    }

    /**
     * Descripcion:
     * Dice si la entidad sigue viva.
     *
     * Retorno:
     * true si tiene mas de 0 de vida, false si no.
     */
    public boolean estaVivo() { return vida > 0; }

    /**
     * Descripcion:
     * Mueve la entidad un paso hacia adelante en el eje X.
     */
    public void moverse() {
        moverse(1, 0, 0);
    }

    /**
     * Descripcion:
     * Mueve la entidad sumando un desplazamiento a su posicion actual.
     * Lanza un error si la entidad esta muerta.
     *
     * Parametros:
     * dx - Cuanto se mueve en X.
     * dy - Cuanto se mueve en Y.
     * dz - Cuanto se mueve en Z.
     */
    public void moverse(double dx, double dy, double dz) {
        if (!estaVivo()) {
            throw new IllegalStateException(nombre + " no puede moverse: está muerto.");
        }
        this.x += dx;
        this.y += dy;
        this.z += dz;
        System.out.printf("%s se desplaza a (%.1f, %.1f, %.1f).%n", nombre, x, y, z);
    }

    /**
     * Descripcion:
     * Cambia la posicion de la entidad a una nueva, como el comando /tp del juego.
     *
     * Parametros:
     * x - Nueva posicion en X.
     * y - Nueva posicion en Y.
     * z - Nueva posicion en Z.
     */
    public void teletransportar(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        System.out.printf("%s se teletransporta a (%.1f, %.1f, %.1f).%n", nombre, x, y, z);
    }

    /**
     * Descripcion:
     * Calcula la distancia en linea recta entre esta entidad y otra. Lanza un error si la otra es nula.
     *
     * Parametros:
     * otra - La entidad hacia la que se mide la distancia.
     *
     * Retorno:
     * La distancia entre las dos entidades.
     */
    public double distanciaHacia(Entidad otra) {
        if (otra == null) {
            throw new IllegalArgumentException("La entidad destino no puede ser nula.");
        }
        double dx = this.x - otra.x;
        double dy = this.y - otra.y;
        double dz = this.z - otra.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Descripcion:
     * Hace que la entidad desaparezca del mundo. Solo se puede si ya esta muerta,
     * si sigue viva lanza un error.
     */
    public void desaparecer() {
        if (estaVivo()) {
            throw new IllegalStateException(nombre + " no puede desaparecer: todavía sigue vivo.");
        }
        System.out.println(nombre + " desaparece del mundo.");
    }

    /**
     * Descripcion:
     * Metodo abstracto: cada clase hija define como reacciona ante el jugador,
     * porque la clase padre no puede saberlo.
     *
     * Retorno:
     * Un texto que describe la reaccion.
     */
    public abstract String reaccionar();

    /**
     * Descripcion:
     * Arma un texto con los datos de la entidad (tipo, nombre, vida y posicion).
     *
     * Retorno:
     * El texto con la informacion de la entidad.
     */
    @Override
    public String toString() {
        return String.format("%s[nombre=%s, vida=%d/%d, pos=(%.1f,%.1f,%.1f)]",
                getClass().getSimpleName(), nombre, vida, vidaMaxima, x, y, z);
    }

    /**
     * Descripcion:
     * Compara dos entidades. Se consideran iguales si tienen el mismo nombre y son del mismo tipo de clase.
     *
     * Parametros:
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
        return nombre.equals(entidad.nombre) && getClass() == entidad.getClass();
    }

    /**
     * Descripcion:
     * Calcula un numero a partir del nombre y la clase. Va junto con equals().
     *
     * Retorno:
     * El numero calculado.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre, getClass());
    }
}
