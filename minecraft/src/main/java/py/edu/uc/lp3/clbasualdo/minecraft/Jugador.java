package py.edu.uc.lp3.clbasualdo.minecraft;

import java.util.ArrayList;
import java.util.List;

public class Jugador extends Entidad {
    private static final double RANGO_ATAQUE = 3.0;
    private static final int DANIO_BASE = 4;

    private int nivel;
    private int experiencia;
    private final List<String> inventario;

    public Jugador(String nombre, int vida, int nivel) {
        super(nombre, vida);
        if (nivel < 0) {
            throw new IllegalArgumentException("El nivel no puede ser negativo.");
        }
        this.nivel = nivel;
        this.experiencia = 0;
        this.inventario = new ArrayList<>();
    }

    public int getNivel() { return nivel; }
    public int getExperiencia() { return experiencia; }
    public List<String> getInventario() { return List.copyOf(inventario); }

    public void construir() {
        System.out.println(getNombre() + " esta construyendo.");
    }

    /** Coloca un bloque concreto en la posicion actual del jugador. */
    public void construir(String bloque) {
        if (bloque == null || bloque.isBlank()) {
            throw new IllegalArgumentException("El bloque no puede estar vacío.");
        }
        System.out.printf("%s coloca un bloque de %s en (%.1f, %.1f, %.1f).%n",
                getNombre(), bloque, getX(), getY(), getZ());
    }

    /** Ataque real: valida vida, rango y otorga experiencia si el objetivo muere. */
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

    public void mostrarNivel() { System.out.println("Nivel: " + nivel); }

    public void ganarExperiencia(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("La experiencia no puede ser negativa.");
        }
        experiencia += puntos;
        System.out.println(getNombre() + " gana " + puntos + " de experiencia.");
        while (experiencia >= 100) {
            experiencia -= 100;
            nivel++;
            System.out.println(getNombre() + " sube al nivel " + nivel + "!");
        }
    }

    public void agregarItem(String item) {
        if (item == null || item.isBlank()) {
            throw new IllegalArgumentException("El item no puede estar vacío.");
        }
        inventario.add(item);
        System.out.println(getNombre() + " recoge " + item + ".");
    }

    public boolean usarItem(String item) {
        boolean usado = inventario.remove(item);
        System.out.println(usado
                ? getNombre() + " usa " + item + "."
                : getNombre() + " no tiene " + item + " en su inventario.");
        return usado;
    }

    /** Regenera vida usando la herramienta protegida heredada de Entidad. */
    public void regenerar(int cantidad) {
        curar(cantidad);
    }

    @Override
    public String reaccionar() {
        return getNombre() + " evalua si combatir o retroceder segun su vida restante.";
    }
}
