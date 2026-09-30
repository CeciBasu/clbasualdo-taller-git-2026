package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Descripcion:
 * Pruebas del Enderman.
 *
 * Responsabilidad:
 * Verificar que el Enderman nazca con los valores de la jerarquia EntidadHostil,
 * que respete su rango de ataque y que se teletransporte como entidad viva.
 */
class EndermanTest {

    /**
     * Descripcion:
     * El Enderman nace con 40 de vida, 7 de dano y 16.0 de rango, y es valido.
     */
    @Test
    void naceConLosValoresDeLaJerarquia() {
        Enderman enderman = new Enderman();
        assertTrue(enderman.estaVivo());
        assertEquals(40, enderman.getVida());
        assertEquals(40, enderman.getVidaMaxima());
        assertEquals(7, enderman.getDanioAtaque());
        assertEquals(16.0, enderman.getRangoAtaque());
        assertEquals("Enderman", enderman.getNombre());
    }

    /**
     * Descripcion:
     * Es un hostil: ataca con su propio mensaje y su reaccion no viene de la base.
     */
    @Test
    void atacaYReaccionaConSuPropioComportamiento() {
        Enderman enderman = new Enderman();
        enderman.atacar();
        String reaccion = enderman.reaccionar();
        assertTrue(reaccion.contains("teletransporta"), "debe decir que se teletransporta");
    }

    /**
     * Descripcion:
     * Su rango de 16.0 es el mayor de la jerarquia: alcanza a mas lejos que el
     * zombie y que el esqueleto.
     */
    @Test
    void suRangoEsElMayorDeLosHostiles() {
        Enderman enderman = new Enderman();
        enderman.teletransportar(0, 0, 0);

        Zombie zombie = new Zombie();
        zombie.teletransportar(10, 0, 0);
        enderman.golpear(zombie);
        assertEquals(13, zombie.getVida(), "a 10 bloques el zombie sigue dentro de rango");
    }

    /**
     * Descripcion:
     * Se teletransporta, y la entidad se puede mover con su mecanismo inherited.
     */
    @Test
    void seTeletransportaYMueve() {
        Enderman enderman = new Enderman();
        enderman.teletransportarse();
        enderman.teletransportar(10, 64, -5);
        assertEquals(10.0, enderman.getX(), 0.0001);
        assertEquals(64.0, enderman.getY(), 0.0001);
        assertEquals(-5.0, enderman.getZ(), 0.0001);
    }

    /**
     * Descripcion:
     * Muerto no ataca: la regla la sigue aplicando la clase base.
     */
    @Test
    void muertoNoAtaca() {
        Enderman enderman = new Enderman();
        enderman.recibirDanio(40);
        assertFalse(enderman.estaVivo());
        assertThrows(IllegalStateException.class, () -> enderman.golpear(new Zombie()));
    }
}
