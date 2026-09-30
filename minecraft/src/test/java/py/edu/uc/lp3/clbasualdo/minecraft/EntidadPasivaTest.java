package py.edu.uc.lp3.clbasualdo.minecraft;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Descripcion:
 * Pruebas de las entidades pasivas: huida y deteccion de peligro.
 *
 * Responsabilidad:
 * Verificar que huir se aleje de la amenaza, que no falle cuando estan en el
 * mismo punto y que el limite de peligro se sostenga en el borde.
 */
class EntidadPasivaTest {

    /**
     * Descripcion:
     * Huir aleja 2 bloques en direccion contraria a la amenaza.
     */
    @Test
    void huirSeAlejaDeLaAmenaza() {
        Aldeano aldeano = new Aldeano("Herrero");
        Zombie zombie = new Zombie();
        aldeano.teletransportar(0, 0, 0);
        zombie.teletransportar(10, 0, 0);

        aldeano.huir(zombie);

        assertEquals(-2.0, aldeano.getX(), 0.0001);
        assertEquals(0.0, aldeano.getY(), 0.0001);
        assertEquals(0.0, aldeano.getZ(), 0.0001);
    }

    /**
     * Descripcion:
     * Cuando la entidad y la amenaza estan en el mismo punto, la distancia es 0
     * y dividir por ella reventaria. El codigo elige una direccion y sigue.
     */
    @Test
    void huirEnElMismoPuntoNoFalla() {
        Aldeano aldeano = new Aldeano("Herrero");
        Zombie zombie = new Zombie();
        aldeano.teletransportar(3, 3, 3);
        zombie.teletransportar(3, 3, 3);

        aldeano.huir(zombie);

        // Se aleja 2 bloques en X, que es la direccion de fallback.
        assertEquals(5.0, aldeano.getX(), 0.0001);
    }

    /**
     * Descripcion:
     * Una amenaza nula se rechaza al huir.
     */
    @Test
    void huirDeUnaAmenazaNulaSeRechaza() {
        Aldeano aldeano = new Aldeano();
        assertThrows(IllegalArgumentException.class, () -> aldeano.huir(null));
    }

    /**
     * Descripcion:
     * El limite de peligro son 6.0 bloques: justo en el borde todavia es peligro,
     * un centimetro mas alla ya no lo es.
     */
    @Test
    void elLimiteDePeligroSeSostieneEnElBorde() {
        Aldeano aldeano = new Aldeano();
        Zombie zombie = new Zombie();
        aldeano.teletransportar(0, 0, 0);

        zombie.teletransportar(6.0, 0, 0);
        assertTrue(aldeano.estaEnPeligro(zombie), "a 6.0 exactos sigue en peligro");

        zombie.teletransportar(6.01, 0, 0);
        assertFalse(aldeano.estaEnPeligro(zombie), "a 6.01 ya esta fuera de peligro");
    }

    /**
     * Descripcion:
     * Sin amenaza no hay peligro, ni siquiera con una amenaza nula.
     */
    @Test
    void sinAmenazaNoHayPeligro() {
        Aldeano aldeano = new Aldeano();
        assertFalse(aldeano.estaEnPeligro(null));
    }
}
