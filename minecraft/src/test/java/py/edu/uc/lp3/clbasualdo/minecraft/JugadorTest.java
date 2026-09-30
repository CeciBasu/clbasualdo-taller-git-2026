package py.edu.uc.lp3.clbasualdo.minecraft;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Descripcion:
 * Pruebas del Jugador: inventario, experiencia y nivel.
 *
 * Responsabilidad:
 * Verificar que el inventario no se pueda modificar por fuera, que la
 * experiencia no acepte negativos y que al subir de nivel se descuente.
 */
class JugadorTest {

    /**
     * Descripcion:
     * El inventario se devuelve como copia: modificar lo que devuelve el getter
     * no toca el inventario real del jugador.
     */
    @Test
    void elInventarioNoSePuedeModificarDesdeAfuera() {
        Jugador jugador = new Jugador("Steve", 20, 1);
        jugador.agregarItem("Espada de hierro");
        assertEquals(1, jugador.getInventario().size());

        var copia = jugador.getInventario();
        assertThrows(UnsupportedOperationException.class, () -> copia.add("Pico"));

        assertEquals(1, jugador.getInventario().size(), "el inventario real no debe cambiar");
        assertTrue(jugador.getInventario().contains("Espada de hierro"));
    }

    /**
     * Descripcion:
     * Usar un item lo saca del inventario y devuelve si se pudo usar.
     */
    @Test
    void usarItemLoSacaDelInventario() {
        Jugador jugador = new Jugador("Steve", 20, 1);
        jugador.agregarItem("Manzana");
        assertTrue(jugador.usarItem("Manzana"));
        assertFalse(jugador.getInventario().contains("Manzana"));
        assertFalse(jugador.usarItem("Manzana"), "no puede usar dos veces el mismo item");
    }

    /**
     * Descripcion:
     * Un item vacio o nulo no entra al inventario.
     */
    @Test
    void unItemInvalidoNoEntra() {
        Jugador jugador = new Jugador("Steve", 20, 1);
        assertThrows(IllegalArgumentException.class, () -> jugador.agregarItem(""));
        assertThrows(IllegalArgumentException.class, () -> jugador.agregarItem("  "));
        assertThrows(IllegalArgumentException.class, () -> jugador.agregarItem(null));
        assertTrue(jugador.getInventario().isEmpty());
    }

    /**
     * Descripcion:
     * Cada 100 puntos de experiencia se sube un nivel y esos puntos se descuentan.
     */
    @Test
    void laExperienciaSubeDeNivel() {
        Jugador jugador = new Jugador("Steve", 20, 1);
        jugador.ganarExperiencia(30);
        assertEquals(1, jugador.getNivel());
        assertEquals(30, jugador.getExperiencia());

        jugador.ganarExperiencia(70);
        assertEquals(2, jugador.getNivel());
        assertEquals(0, jugador.getExperiencia());
    }

    /**
     * Descripcion:
     * Suficiente experiencia de una sola vez puede subir varios niveles.
     */
    @Test
    void unaSolaGanadaPuedeSubirVariosNiveles() {
        Jugador jugador = new Jugador("Steve", 20, 1);
        jugador.ganarExperiencia(250);
        assertEquals(3, jugador.getNivel());
        assertEquals(50, jugador.getExperiencia());
    }

    /**
     * Descripcion:
     * La experiencia y el nivel negativos se rechazan.
     */
    @Test
    void experienciaYNivelNegativosSeRechazan() {
        assertThrows(IllegalArgumentException.class, () -> new Jugador("Steve", 20, -1));
        Jugador jugador = new Jugador("Steve", 20, 1);
        assertThrows(IllegalArgumentException.class, () -> jugador.ganarExperiencia(-10));
        assertEquals(0, jugador.getExperiencia());
    }

    /**
     * Descripcion:
     * Solo el golpe que deja sin vida al objetivo da experiencia. Un zombi de 20
     * de vida aguanta 4 golpes de 4, asi que la experiencia llega en el quinto.
     * Atacar fuera de rango no da nada.
     */
    @Test
    void matarDaExperienciaYAtacarDeLejosNo() {
        Jugador jugador = new Jugador("Steve", 20, 1);

        Zombie lejos = new Zombie();
        lejos.teletransportar(50, 0, 0);
        jugador.atacar(lejos);
        assertEquals(0, jugador.getExperiencia(), "atacar fuera de rango no da experiencia");
        assertEquals(20, lejos.getVida(), "fuera de rango no hay dano");

        Zombie cerca = new Zombie();
        cerca.teletransportar(1, 0, 0);
        for (int i = 0; i < 4; i++) {
            jugador.atacar(cerca);
            assertTrue(cerca.estaVivo(), "con 4 de vida el zombi sigue vivo");
            assertEquals(0, jugador.getExperiencia(), "mientras no muera, no hay experiencia");
        }
        jugador.atacar(cerca);
        assertEquals(0, cerca.getVida());
        assertEquals(10, jugador.getExperiencia());
    }

    /**
     * Descripcion:
     * Un jugador muerto no ataca, y atacar al aire se rechaza.
     */
    @Test
    void unJugadorMuertoNoAtaca() {
        Jugador jugador = new Jugador("Steve", 20, 1);
        jugador.recibirDanio(20);
        assertThrows(IllegalStateException.class, () -> jugador.atacar(new Zombie()));
        assertThrows(IllegalArgumentException.class, () -> new Jugador("Steve", 20, 1).atacar(null));
    }
}
