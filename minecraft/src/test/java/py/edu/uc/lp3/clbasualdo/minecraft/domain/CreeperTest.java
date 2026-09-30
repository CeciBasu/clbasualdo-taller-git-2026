package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Descripcion:
 * Pruebas del comportamiento del Creeper.
 *
 * Responsabilidad:
 * Verificar que la explosion respete su radio, que no se|lastime a si mismo
 * y que solo pueda detonar una vez.
 */
class CreeperTest {

    /**
     * Descripcion:
     * La explosion alcanza a las entidades que estan dentro del radio (3.0)
     * y las que estan fuera no reciben dano.
     */
    @Test
    void soloLastimaADentroDelRadio() {
        Creeper creeper = new Creeper();
        creeper.teletransportar(0, 0, 0);

        Zombie cerca = new Zombie();
        cerca.teletransportar(2, 0, 0);
        Zombie lejos = new Zombie();
        lejos.teletransportar(10, 0, 0);

        creeper.explotar(List.of(cerca, lejos));

        assertEquals(0, cerca.getVida(), "el que estaba a distancia 2 debe recibir dano");
        assertEquals(20, lejos.getVida(), "el que estaba a distancia 10 no debe recibir dano");
    }

    /**
     * Descripcion:
     * El Creeper se excluye a si mismo de la lista de alcanzados, asi que la
     * unica fuente de dano que recibe es su propia destruccion final.
     */
    @Test
    void noSeLastimaASiMismo() {
        Creeper creeper = new Creeper();
        Aldeano aldeano = new Aldeano("Herrero");
        creeper.teletransportar(0, 0, 0);
        aldeano.teletransportar(1, 0, 0);

        creeper.explotar(List.of(creeper, aldeano));

        // Muere por explode(), no por Damage(), y el aldeano tambien muere.
        assertFalse(creeper.estaVivo());
        assertEquals(0, aldeano.getVida());
    }

    /**
     * Descripcion:
     * Al detonar, el Creeper se autodestruye y queda marcado como detonado.
     */
    @Test
    void seDestruyeAlExplotar() {
        Creeper creeper = new Creeper();
        assertFalse(creeper.isDetonado());
        creeper.explotar(List.of());
        assertTrue(creeper.isDetonado());
        assertFalse(creeper.estaVivo());
    }

    /**
     * Descripcion:
     * Una segunda llamada a explotar() no vuelve a|lastimar a nadie: no hace nada.
     */
    @Test
    void soloPuedeExplotarUnaVez() {
        Creeper creeper = new Creeper();
        Zombie zombie = new Zombie();
        zombie.teletransportar(1, 0, 0);

        creeper.explotar(List.of(zombie));
        assertEquals(0, zombie.getVida());

        // Se reconstruye un objetivo vivo y se vuelve a llamar: no debe cambiar.
        Zombie otro = new Zombie();
        otro.teletransportar(1, 0, 0);
        creeper.explotar(List.of(otro));
        assertEquals(20, otro.getVida(), "la segunda explosion no debe hacer dano");
    }

    /**
     * Descripcion:
     * Una lista nula se tolera: el Creeper explota igual, solo que sin alcanzados.
     */
    @Test
    void listaNulaNoRompeLaExplosion() {
        Creeper creeper = new Creeper();
        creeper.explotar(null);
        assertTrue(creeper.isDetonado());
    }
}
