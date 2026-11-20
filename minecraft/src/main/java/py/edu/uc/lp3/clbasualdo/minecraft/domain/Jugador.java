package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Descripción:
 * Representa al jugador del juego, el personaje que controla la persona.
 *
 * Responsabilidad:
 * Se encarga de atacar, construir, guardar ítems en el inventario y subir de nivel al ganar experiencia.
 * También es UICapable para poder dibujarse en la UI.
 */
public class Jugador extends Entidad implements UICapable {
    /** Distancia máxima a la que el jugador puede atacar. */
    private static final double RANGO_ATAQUE = 3.0;
    /** Danio que hace el jugador en cada ataque. */
    private static final int DANIO_BASE = 4;

    /** Nivel actual del jugador. */
    private int nivel;
    /** Puntos de experiencia que lleva hacia el siguiente nivel. */
    private int experiencia;
    /** Lista de los ítems que tiene el jugador. */
    private final List<String> inventario;

    /**
     * Descripción:
     * Crea un jugador con su vida y nivel inicial, sin experiencia y con el inventario vacío.
     *
     * Parámetros:
     * nombre - Nombre del jugador.
     * vida - Vida inicial del jugador.
     * nivel - Nivel con el que empieza.
     */
    public Jugador(String nombre, int vida, int nivel) {
        super(nombre, vida);
        this.nivel = nivel;
        this.experiencia = 0;
        this.inventario = new ArrayList<>();
    }

    /**
     * Descripción:
     * Devuelve el nivel del jugador.
     *
     * Retorno:
     * El nivel actual.
     */
    public int getNivel() { return nivel; }

    /**
     * Descripción:
     * Devuelve la experiencia del jugador.
     *
     * Retorno:
     * Los puntos de experiencia actuales.
     */
    public int getExperiencia() { return experiencia; }

    /**
     * Descripción:
     * Devuelve una copia del inventario, así desde afuera no se puede modificar la lista original.
     *
     * Retorno:
     * Una lista con los ítems del jugador.
     */
    public List<String> getInventario() { return List.copyOf(inventario); }

    /**
     * Descripción:
     * Muestra un mensaje diciendo que el jugador está construyendo.
     */
    public void construir() {
        System.out.println(getNombre() + " está construyendo.");
    }

    /**
     * Descripción:
     * Coloca un bloque en el lugar donde está el jugador.
     *
     * Parámetros:
     * bloque - Nombre del bloque que se coloca.
     */
    public void construir(String bloque) {
        System.out.printf("%s coloca un bloque de %s en (%.1f, %.1f, %.1f).%n",
                getNombre(), bloque, getX(), getY(), getZ());
    }

    /**
     * Descripción:
     * El jugador ataca a una entidad. Si el objetivo está muerto o fuera de su alcance, solo lo informa.
     * Si el objetivo muere por el golpe, el jugador gana 10 de experiencia.
     *
     * Parámetros:
     * objetivo - La entidad que se quiere atacar.
     */
    public void atacar(Entidad objetivo) {
        if (!objetivo.estaVivo()) {
            System.out.println(objetivo.getNombre() + " ya está muerto.");
            return;
        }
        if (distanciaHacia(objetivo) > RANGO_ATAQUE) {
            System.out.println(getNombre() + " está muy lejos para atacar a " + objetivo.getNombre() + ".");
            return;
        }
        System.out.println(getNombre() + " está atacando a " + objetivo.getNombre() + ".");
        objetivo.recibirDanio(DANIO_BASE);
        if (!objetivo.estaVivo()) {
            ganarExperiencia(10);
        }
    }

    /**
     * Descripción:
     * Muestra por pantalla el nivel del jugador.
     */
    public void mostrarNivel() { System.out.println("Nivel: " + nivel); }

    /**
     * Descripción:
     * Suma experiencia al jugador. Cada 100 puntos sube un nivel y esos puntos se descuentan.
     *
     * Parámetros:
     * puntos - Cantidad de experiencia que gana.
     */
    public void ganarExperiencia(int puntos) {
        experiencia += puntos;
        System.out.println(getNombre() + " gana " + puntos + " de experiencia.");
        // Se usa while por si gana suficiente experiencia para subir varios niveles juntos
        while (experiencia >= 100) {
            experiencia -= 100;
            nivel++;
            System.out.println(getNombre() + " sube al nivel " + nivel + ".");
        }
    }

    /**
     * Descripción:
     * Guarda un ítem en el inventario.
     *
     * Parámetros:
     * item - Nombre del ítem que se guarda.
     */
    public void agregarItem(String item) {
        inventario.add(item);
        System.out.println(getNombre() + " recoge " + item + ".");
    }

    /**
     * Descripción:
     * Usa un ítem del inventario y lo saca de la lista. Si no lo tiene, solo muestra un mensaje.
     *
     * Parámetros:
     * item - Nombre del ítem que se quiere usar.
     *
     * Retorno:
     * true si tenía el ítem y se usó, false si no lo tenía.
     */
    public boolean usarItem(String item) {
        boolean usado = inventario.remove(item);
        System.out.println(usado
                ? getNombre() + " usa " + item + "."
                : getNombre() + " no tiene " + item + " en su inventario.");
        return usado;
    }

    /**
     * Descripción:
     * El jugador recupera vida. Usa el método curar() que hereda de Entidad.
     *
     * Parámetros:
     * cantidad - Cantidad de vida que recupera.
     */
    public void regenerar(int cantidad) {
        curar(cantidad);
    }

    /**
     * Descripción:
     * Dice cómo reacciona el jugador: decide si pelear o retroceder según su vida.
     *
     * Retorno:
     * Un texto que describe la reacción.
     */
    @Override
    public String reaccionar() {
        return getNombre() + " evalúa si combatir o retroceder según su vida restante.";
    }

    /**
     * Descripción:
     * Devuelve la imagen con la que la UI dibuja al Jugador.
     *
     * Retorno:
     * El nombre de la imagen del Jugador.
     */
    @Override
    public String imagen() {
        return "jugador.png";
    }

    /**
     * Descripción:
     * Devuelve la posición del Jugador formateada para la UI.
     *
     * Retorno:
     * Un texto con las coordenadas del Jugador.
     */
    @Override
    public String posicion() {
        return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", getX(), getY(), getZ());
    }

    /**
     * Descripción:
     * Devuelve la velocidad con la que se mueve el Jugador.
     *
     * Retorno:
     * La velocidad del Jugador.
     */
    @Override
    public double velocidad() {
        return 0.20;
    }

    /**
     * Descripción:
     * Reemplaza el sonido genérico del default con el propio del Jugador.
     *
     * Retorno:
     * Un texto que describe el sonido del Jugador.
     */
    @Override
    public String sonido() {
        return getNombre() + " respira y golpea bloques.";
    }
}
