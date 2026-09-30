package py.edu.uc.lp3.clbasualdo.minecraft;

import java.util.Objects;

public abstract class Entidad {
    private final String nombre;
    private int vida;
    private final int vidaMaxima;
    private double x;
    private double y;
    private double z;

    public Entidad(String nombre, int vida) {
        this(nombre, vida, 0, 0, 0);
    }

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

    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }

    public void mostrarInfo() {
        System.out.printf("Nombre: %s%n", nombre);
        System.out.printf("Vida: %d/%d%n", vida, vidaMaxima);
        System.out.printf("Posición: (%.1f, %.1f, %.1f)%n", x, y, z);
    }

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

    /** Herramienta interna de la jerarquia: no toda entidad puede pedir esto desde afuera (un Creeper no se regenera), por eso no es público general, pero Jugador sí la necesita. */
    protected void curar(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad a curar no puede ser negativa.");
        }
        vida = Math.min(vidaMaxima, vida + cantidad);
    }

    public boolean estaVivo() { return vida > 0; }

    /** Movimiento por defecto: un paso hacia adelante en el eje X. */
    public void moverse() {
        moverse(1, 0, 0);
    }

    /** Movimiento relativo usando desplazamientos en cada eje. */
    public void moverse(double dx, double dy, double dz) {
        if (!estaVivo()) {
            throw new IllegalStateException(nombre + " no puede moverse: está muerto.");
        }
        this.x += dx;
        this.y += dy;
        this.z += dz;
        System.out.printf("%s se desplaza a (%.1f, %.1f, %.1f).%n", nombre, x, y, z);
    }

    /** Fija una nueva posición absoluta, como un comando /tp. */
    public void teletransportar(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        System.out.printf("%s se teletransporta a (%.1f, %.1f, %.1f).%n", nombre, x, y, z);
    }

    /** Distancia euclidiana en el mundo 3D hacia otra entidad. */
    public double distanciaHacia(Entidad otra) {
        if (otra == null) {
            throw new IllegalArgumentException("La entidad destino no puede ser nula.");
        }
        double dx = this.x - otra.x;
        double dy = this.y - otra.y;
        double dz = this.z - otra.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public void desaparecer() {
        if (estaVivo()) {
            throw new IllegalStateException(nombre + " no puede desaparecer: todavía sigue vivo.");
        }
        System.out.println(nombre + " desaparece del mundo.");
    }

    /** Cada hija reacciona distinto ante el jugador; el padre no puede saber como. */
    public abstract String reaccionar();

    @Override
    public String toString() {
        return String.format("%s[nombre=%s, vida=%d/%d, pos=(%.1f,%.1f,%.1f)]",
                getClass().getSimpleName(), nombre, vida, vidaMaxima, x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entidad)) return false;
        Entidad entidad = (Entidad) o;
        return nombre.equals(entidad.nombre) && getClass() == entidad.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, getClass());
    }
}
