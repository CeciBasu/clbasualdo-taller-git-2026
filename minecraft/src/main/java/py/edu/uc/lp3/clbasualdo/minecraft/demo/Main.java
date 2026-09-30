package py.edu.uc.lp3.clbasualdo.minecraft.demo;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.Aldeano;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Animal;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Creeper;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Esqueleto;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Jugador;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Zombie;

import java.util.List;

/**
 * Descripcion:
 * Demostracion que prueba todas las clases del juego. Vive en el paquete demo
 * para que el dominio (Entidad y sus hijas) no dependa de ella.
 *
 * Responsabilidad:
 * Crea al jugador y a las entidades, las pone en el mundo y muestra por pantalla
 * distintas situaciones como combate, huida y explosion.
 */
public class Main {
    /**
     * Descripcion:
     * Metodo donde empieza el programa. Crea las entidades, las ubica y prueba sus acciones.
     *
     * Parametros:
     * args - Argumentos que se reciben al ejecutar el programa (no se usan).
     */
    public static void main(String[] args) {
        Jugador jugador = new Jugador("Steve", 20, 1);
        jugador.teletransportar(0, 64, 0);

        Zombie zombie = new Zombie();
        zombie.teletransportar(1, 64, 0);

        Esqueleto esqueleto = new Esqueleto();
        esqueleto.teletransportar(5, 64, 2);

        Creeper creeper = new Creeper();
        creeper.teletransportar(2, 64, 1);

        Aldeano aldeano = new Aldeano("Herrero");
        aldeano.teletransportar(-3, 64, 0);

        Animal vaca = new Animal("Vaca", "Vaca");
        vaca.teletransportar(-1, 64, -2);

        System.out.println("===== JUGADOR =====");
        jugador.mostrarInfo();
        jugador.mostrarNivel();
        jugador.construir("Piedra");
        jugador.agregarItem("Espada de hierro");

        System.out.println("\n===== ENTIDADES HOSTILES =====");
        zombie.mostrarInfo(); zombie.atacar();
        esqueleto.mostrarInfo(); esqueleto.atacar();
        creeper.mostrarInfo(); creeper.atacar();

        System.out.println("\n===== ENTIDADES PASIVAS =====");
        aldeano.mostrarInfo(); aldeano.comerciar(3); aldeano.huir();
        vaca.mostrarInfo(); vaca.comer(); vaca.huir();

        System.out.println("\n===== COMBATE =====");
        System.out.printf("Distancia jugador-zombie: %.2f bloques%n", jugador.distanciaHacia(zombie));
        jugador.atacar(zombie);
        zombie.golpear(jugador);
        System.out.println("¿El jugador está vivo? " + jugador.estaVivo());

        System.out.println("\n===== HUIDA DEL ALDEANO ANTE EL ZOMBIE =====");
        System.out.println("¿Está en peligro? " + aldeano.estaEnPeligro(zombie));
        aldeano.huir(zombie);

        System.out.println("\n===== EXPLOSION DEL CREEPER =====");
        creeper.explotar(List.of(jugador, zombie, esqueleto, aldeano, vaca));
        System.out.println("¿El Creeper sigue vivo? " + creeper.estaVivo());
    }
}
