package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Descripción:
 * Pruebas del comportamiento del Creeper.
 *
 * Responsabilidad:
 * Verificar que la explosión respete su radio, que no se lastime a sí mismo
 * y que solo pueda detonar una vez.
 */
class CreeperTest {

    /**
     * Descripción:
     * La explosión alcanza a las entidades que están dentro del radio (3.0)
     * y las que están fuera no reciben daño.
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
     * Descripción:
     * El Creeper se excluye a sí mismo de la lista de alcanzados, así que la
     * única fuente de daño que recibe es su propia destrucción final.
     */
    @Test
    void noSeLastimaASiMismo() {
        Creeper creeper = new Creeper();
        Aldeano aldeano = new Aldeano("Herrero");
        creeper.teletransportar(0, 0, 0);
        aldeano.teletransportar(1, 0, 0);

        creeper.explotar(List.of(creeper, aldeano));

        // Muere por explode(), no por Damage(), y el aldeano también muere.
        assertFalse(creeper.estaVivo());
        assertEquals(0, aldeano.getVida());
    }

    /**
     * Descripción:
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
     * Descripción:
     * Una lista nula se tolera: el Creeper explota igual, solo que sin alcanzados.
     */
    @Test
    void listaNulaNoRompeLaExplosion() {
        Creeper creeper = new Creeper();
        creeper.explotar(null);
        assertTrue(creeper.isDetonado());
    }
}
