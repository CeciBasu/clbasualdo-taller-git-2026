package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Descripcion:
 * Pruebas del Esqueleto: municion, recarga y alcance del ataque.
 *
 * Responsabilidad:
 * Verificar que las flechas se gasten al atacar, que la recarga no acepte
 * cantidades negativas y que el golpe respete el rango y la vida.
 */
class EsqueletoTest {

    /**
     * Descripcion:
     * Un esqueleto nuevo arranca con 16 flechas y cada ataque gasta una.
     */
    @Test
    void cadaDisparoGastaUnaFlecha() {
        Esqueleto esqueleto = new Esqueleto();
        assertEquals(16, esqueleto.getFlechas());
        esqueleto.atacar();
        assertEquals(15, esqueleto.getFlechas());
        esqueleto.atacar();
        assertEquals(14, esqueleto.getFlechas());
    }

    /**
     * Descripcion:
     * Sin flechas el esqueleto ataca cuerpo a cuerpo y no queda en negativo.
     */
    @Test
    void sinFlechasAtacaCuerpoACuerpo() {
        Esqueleto esqueleto = new Esqueleto();
        for (int i = 0; i < 16; i++) {
            esqueleto.atacar();
        }
        assertEquals(0, esqueleto.getFlechas());
        esqueleto.atacar();
        assertEquals(0, esqueleto.getFlechas(), "no puede quedar con flechas negativas");
    }

    /**
     * Descripcion:
     * Recargar suma flechas y una cantidad negativa se rechaza.
     */
    @Test
    void recargarSumaYRechazaNegativos() {
        Esqueleto esqueleto = new Esqueleto();
        esqueleto.recargar(10);
        assertEquals(26, esqueleto.getFlechas());
        esqueleto.recargar(0);
        assertEquals(26, esqueleto.getFlechas());
        assertThrows(IllegalArgumentException.class, () -> esqueleto.recargar(-1));
        assertEquals(26, esqueleto.getFlechas());
    }

    /**
     * Descripcion:
     * El golpe solo llega si el objetivo esta dentro del rango (8.0) y vivo.
     */
    @Test
    void elGolpeRespetaElRango() {
        Esqueleto esqueleto = new Esqueleto();
        esqueleto.teletransportar(0, 0, 0);

        Zombie lejos = new Zombie();
        lejos.teletransportar(20, 0, 0);
        esqueleto.golpear(lejos);
        assertEquals(20, lejos.getVida(), "fuera de rango no hay dano");
        assertEquals(16, esqueleto.getFlechas(), "si no alcanza, no gasta flecha");

        Zombie cerca = new Zombie();
        cerca.teletransportar(5, 0, 0);
        esqueleto.golpear(cerca);
        assertEquals(16, cerca.getVida(), "4 de dano dentro de rango");
        assertEquals(15, esqueleto.getFlechas());
    }

    /**
     * Descripcion:
     * Un esqueleto muerto no ataca y un objetivo nulo se rechaza.
     */
    @Test
    void unEsqueletoMuertoNoAtaca() {
        Esqueleto esqueleto = new Esqueleto();
        Zombie objetivo = new Zombie();
        esqueleto.recibirDanio(20);
        assertFalse(esqueleto.estaVivo());
        assertThrows(IllegalStateException.class, () -> esqueleto.golpear(objetivo));
        assertThrows(IllegalArgumentException.class, () -> new Esqueleto().golpear(null));
    }

    /**
     * Descripcion:
     * El objetivo nulo se rechaza, y tambien el esqueleto con vida invalida.
     */
    @Test
    void elConstructorValidaSusDatos() {
        assertThrows(IllegalArgumentException.class, () -> new Esqueleto("Bony", 0));
        assertThrows(IllegalArgumentException.class, () -> new Esqueleto("", 20));
        assertTrue(new Esqueleto().estaVivo());
        assertEquals("Esqueleto", new Esqueleto().getNombre());
    }
}
