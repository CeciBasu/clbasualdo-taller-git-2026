package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Descripcion:
 * Representa al jugador del juego, el personaje que controla la persona.
 *
 * Responsabilidad:
 * Se encarga de atacar, construir, guardar items en el inventario y subir de nivel al ganar experiencia.
 */
public class Jugador extends Entidad {
    /** Distancia maxima a la que el jugador puede atacar. */
    private static final double RANGO_ATAQUE = 3.0;
    /** Danio que hace el jugador en cada ataque. */
    private static final int DANIO_BASE = 4;

    /** Nivel actual del jugador. */
    private int nivel;
    /** Puntos de experiencia que lleva hacia el siguiente nivel. */
    private int experiencia;
    /** Lista de los items que tiene el jugador. */
    private final List<String> inventario;

    /**
     * Descripcion:
     * Crea un jugador con su vida y nivel inicial, sin experiencia y con el inventario vacio.
     * Lanza un error si el nivel es negativo.
     *
     * Parametros:
     * nombre - Nombre del jugador.
     * vida - Vida inicial del jugador.
     * nivel - Nivel con el que empieza.
     */
    public Jugador(String nombre, int vida, int nivel) {
        super(nombre, vida);
        if (nivel < 0) {
            throw new IllegalArgumentException("El nivel no puede ser negativo.");
        }
        this.nivel = nivel;
        this.experiencia = 0;
        this.inventario = new ArrayList<>();
    }

    /**
     * Descripcion:
     * Devuelve el nivel del jugador.
     *
     * Retorno:
     * El nivel actual.
     */
    public int getNivel() { return nivel; }

    /**
     * Descripcion:
     * Devuelve la experiencia del jugador.
     *
     * Retorno:
     * Los puntos de experiencia actuales.
     */
    public int getExperiencia() { return experiencia; }

    /**
     * Descripcion:
     * Devuelve una copia del inventario, asi desde afuera no se puede modificar la lista original.
     *
     * Retorno:
     * Una lista con los items del jugador.
     */
    public List<String> getInventario() { return List.copyOf(inventario); }

    /**
     * Descripcion:
     * Muestra un mensaje diciendo que el jugador esta construyendo.
     */
    public void construir() {
        System.out.println(getNombre() + " esta construyendo.");
    }

    /**
     * Descripcion:
     * Coloca un bloque en el lugar donde esta el jugador. Lanza un error si el bloque esta vacio o es nulo.
     *
     * Parametros:
     * bloque - Nombre del bloque que se coloca.
     */
    public void construir(String bloque) {
        if (bloque == null || bloque.isBlank()) {
            throw new IllegalArgumentException("El bloque no puede estar vacío.");
        }
        System.out.printf("%s coloca un bloque de %s en (%.1f, %.1f, %.1f).%n",
                getNombre(), bloque, getX(), getY(), getZ());
    }

    /**
     * Descripcion:
     * El jugador ataca a una entidad. Revisa que este vivo, que el objetivo tambien y que este a su alcance.
     * Si el objetivo muere por el golpe, el jugador gana 10 de experiencia.
     * Lanza un error si el jugador esta muerto o el objetivo es nulo.
     *
     * Parametros:
     * objetivo - La entidad que se quiere atacar.
     */
    public void atacar(Entidad objetivo) {
        if (!estaVivo()) {
            throw new IllegalStateException(getNombre() + " no puede atacar: está muerto.");
        }
        if (objetivo == null) {
            throw new IllegalArgumentException("El objetivo no puede ser nulo.");
        }
        if (!objetivo.estaVivo()) {
            System.out.println(objetivo.getNombre() + " ya está muerto.");
            return;
        }
        if (distanciaHacia(objetivo) > RANGO_ATAQUE) {
            System.out.println(getNombre() + " está muy lejos para atacar a " + objetivo.getNombre() + ".");
            return;
        }
        System.out.println(getNombre() + " esta atacando a " + objetivo.getNombre() + ".");
        objetivo.recibirDanio(DANIO_BASE);
        if (!objetivo.estaVivo()) {
            ganarExperiencia(10);
        }
    }

    /**
     * Descripcion:
     * Muestra por pantalla el nivel del jugador.
     */
    public void mostrarNivel() { System.out.println("Nivel: " + nivel); }

    /**
     * Descripcion:
     * Suma experiencia al jugador. Cada 100 puntos sube un nivel y esos puntos se descuentan.
     * Lanza un error si los puntos son negativos.
     *
     * Parametros:
     * puntos - Cantidad de experiencia que gana.
     */
    public void ganarExperiencia(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("La experiencia no puede ser negativa.");
        }
        experiencia += puntos;
        System.out.println(getNombre() + " gana " + puntos + " de experiencia.");
        // Se usa while por si gana suficiente experiencia para subir varios niveles juntos
        while (experiencia >= 100) {
            experiencia -= 100;
            nivel++;
            System.out.println(getNombre() + " sube al nivel " + nivel + "!");
        }
    }

    /**
     * Descripcion:
     * Guarda un item en el inventario. Lanza un error si el item esta vacio o es nulo.
     *
     * Parametros:
     * item - Nombre del item que se guarda.
     */
    public void agregarItem(String item) {
        if (item == null || item.isBlank()) {
            throw new IllegalArgumentException("El item no puede estar vacío.");
        }
        inventario.add(item);
        System.out.println(getNombre() + " recoge " + item + ".");
    }

    /**
     * Descripcion:
     * Usa un item del inventario y lo saca de la lista. Si no lo tiene, solo muestra un mensaje.
     *
     * Parametros:
     * item - Nombre del item que se quiere usar.
     *
     * Retorno:
     * true si tenia el item y se uso, false si no lo tenia.
     */
    public boolean usarItem(String item) {
        boolean usado = inventario.remove(item);
        System.out.println(usado
                ? getNombre() + " usa " + item + "."
                : getNombre() + " no tiene " + item + " en su inventario.");
        return usado;
    }

    /**
     * Descripcion:
     * El jugador recupera vida. Usa el metodo curar() que hereda de Entidad.
     *
     * Parametros:
     * cantidad - Cantidad de vida que recupera.
     */
    public void regenerar(int cantidad) {
        curar(cantidad);
    }

    /**
     * Descripcion:
     * Dice como reacciona el jugador: decide si pelear o retroceder segun su vida.
     *
     * Retorno:
     * Un texto que describe la reaccion.
     */
    @Override
    public String reaccionar() {
        return getNombre() + " evalua si combatir o retroceder segun su vida restante.";
    }
}
