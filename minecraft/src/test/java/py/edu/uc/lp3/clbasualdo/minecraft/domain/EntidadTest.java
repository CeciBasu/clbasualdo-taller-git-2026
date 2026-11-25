package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Descripción:
 * Pruebas de las reglas que sostiene la clase base Entidad.
 *
 * Responsabilidad:
 * Verificar que el objeto nazca válido, que la vida respete sus límites y que
 * no se pueda dejar una entidad en un estado inválido desde afuera.
 */
class EntidadTest {

    /**
     * Descripción:
     * La vida nunca baja de 0, aunque el daño sea enorme.
     */
    @Test
    void laVidaNoBajaDeCero() {
        Zombie zombie = new Zombie();
        zombie.recibirDanio(9999);
        assertEquals(0, zombie.getVida());
        assertFalse(zombie.estaVivo());
    }

    /**
     * Descripción:
     * Curarse nunca deja la vida por encima de la máxima.
     */
    @Test
    void curarNoSuperaLaVidaMaxima() {
        Jugador jugador = new Jugador("Steve", 20, 1);
        jugador.recibirDanio(15);
        assertEquals(5, jugador.getVida());
        jugador.regenerar(100);
        assertEquals(20, jugador.getVida());
        assertEquals(20, jugador.getVidaMaxima());
    }

    /**
     * Descripción:
     * La distancia se calcula bien, también hacia sí misma.
     */
    @Test
    void distanciaEnLineaRecta() {
        Zombie a = new Zombie();
        Zombie b = new Zombie();
        a.teletransportar(0, 0, 0);
        b.teletransportar(3, 4, 0);
        assertEquals(5.0, a.distanciaHacia(b), 0.0001);
        assertEquals(0.0, a.distanciaHacia(a), 0.0001);
    }

    /**
     * Descripción:
     * El estado no se puede tocar por asignación directa: nombre y vida máxima
     * son private y solo existen getters, no setters.
     */
    @Test
    void elEstadoSeCambiaSoloPorMensajes() throws Exception {
        for (String metodo : new String[]{"setNombre", "setVida", "setVidaMaxima", "setX"}) {
            boolean existeSetter = false;
            for (var m : Entidad.class.getMethods()) {
                if (m.getName().equals(metodo)) existeSetter = true;
            }
            assertFalse(existeSetter, "Entidad no deberia exponer " + metodo + "()");
        }
    }

    /**
     * Descripción:
     * Dos entidades con el mismo nombre pero distinto tipo no son iguales,
     * y una entidad es igual a si misma.
     */
    @Test
    void igualdadPorNombreYTipo() {
        Zombie zombie = new Zombie();
        Zombie otroZombie = new Zombie();
        Creeper creeper = new Creeper();
        assertTrue(zombie.equals(otroZombie));
        assertFalse(zombie.equals(creeper));
        assertTrue(zombie.equals(zombie));
        assertEquals(zombie.hashCode(), otroZombie.hashCode());
    }
}
