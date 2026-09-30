package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Descripcion:
 * Pruebas de las reglas que sostiene la clase base Entidad.
 *
 * Responsabilidad:
 * Verificar que el objeto nazca valido, que la vida respete sus limites y que
 * no se pueda dejar una entidad en un estado invalido desde afuera.
 */
class EntidadTest {

    /**
     * Descripcion:
     * Una entidad con nombre vacio o nulo no puede construirse.
     */
    @Test
    void nombreVacioOInvalidoSeRechaza() {
        assertThrows(IllegalArgumentException.class, () -> new Esqueleto("", 20));
        assertThrows(IllegalArgumentException.class, () -> new Esqueleto("   ", 20));
        assertThrows(IllegalArgumentException.class, () -> new Esqueleto(null, 20));
    }

    /**
     * Descripcion:
     * Una entidad no puede nacer muerta: la vida inicial tiene que ser mayor a 0.
     */
    @Test
    void vidaInicialNoPositivaSeRechaza() {
        assertThrows(IllegalArgumentException.class, () -> new Esqueleto("Bony", 0));
        assertThrows(IllegalArgumentException.class, () -> new Esqueleto("Bony", -10));
    }

    /**
     * Descripcion:
     * La vida nunca baja de 0, aunque el dano sea enorme.
     */
    @Test
    void laVidaNoBajaDeCero() {
        Zombie zombie = new Zombie();
        zombie.recibirDanio(9999);
        assertEquals(0, zombie.getVida());
        assertFalse(zombie.estaVivo());
    }

    /**
     * Descripcion:
     * Un dano negativo no se acepta, para que la vida no pueda subir de contrabando.
     */
    @Test
    void danoNegativoSeRechaza() {
        Zombie zombie = new Zombie();
        assertThrows(IllegalArgumentException.class, () -> zombie.recibirDanio(-1));
        assertEquals(20, zombie.getVida());
    }

    /**
     * Descripcion:
     * Curarse nunca deja la vida por encima de la maxima.
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
     * Descripcion:
     * Curar una cantidad negativa se rechaza.
     */
    @Test
    void cantidadNegativaAlCurarSeRechaza() {
        Jugador jugador = new Jugador("Steve", 20, 1);
        assertThrows(IllegalArgumentException.class, () -> jugador.regenerar(-5));
    }

    /**
     * Descripcion:
     * Una entidad muerta no puede moverse, pero si puede desaparecer del mundo.
     */
    @Test
    void unaEntidadMuertaNoSeMueve() {
        Zombie zombie = new Zombie();
        zombie.recibirDanio(20);
        assertFalse(zombie.estaVivo());
        assertThrows(IllegalStateException.class, () -> zombie.moverse(1, 0, 0));
    }

    /**
     * Descripcion:
     * Solo se desaparece cuando ya se esta muerto: una entidad viva no puede,
     * y una muerta si.
     */
    @Test
    void soloDesapareceQuienEstaMuerto() {
        Zombie zombie = new Zombie();
        assertThrows(IllegalStateException.class, zombie::desaparecer);
        zombie.recibirDanio(20);
        zombie.desaparecer();
    }

    /**
     * Descripcion:
     * La distancia se calcula bien y con la entidad nula se rechaza.
     */
    @Test
    void distanciaYDestinoNulo() {
        Zombie a = new Zombie();
        Zombie b = new Zombie();
        a.teletransportar(0, 0, 0);
        b.teletransportar(3, 4, 0);
        assertEquals(5.0, a.distanciaHacia(b), 0.0001);
        assertEquals(0.0, a.distanciaHacia(a), 0.0001);
        assertThrows(IllegalArgumentException.class, () -> a.distanciaHacia(null));
    }

    /**
     * Descripcion:
     * El estado no se puede tocar por asignacion directa: nombre y vida maxima
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
     * Descripcion:
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
