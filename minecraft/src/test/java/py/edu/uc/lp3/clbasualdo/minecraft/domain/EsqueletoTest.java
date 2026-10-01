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

    /**
     * Descripcion:
     * Sobrecarga de disparar(): la version sin distancia gasta una flecha y lo dice.
     */
    @Test
    void dispararSinDistanciaGastaUnaFlecha() {
        Esqueleto esqueleto = new Esqueleto("Bony", 20);

        String texto = esqueleto.disparar();

        assertEquals(15, esqueleto.getFlechas());
        assertTrue(texto.contains("dispara una flecha"), texto);
    }

    /**
     * Descripcion:
     * La version con distancia, si el objetivo esta dentro del rango (8.0), se
     * comporta igual que la version sin distancia.
     */
    @Test
    void dispararConDistanciaDentroDelRangoGastaFlecha() {
        Esqueleto esqueleto = new Esqueleto("Bony", 20);

        String texto = esqueleto.disparar(5.0);

        assertEquals(15, esqueleto.getFlechas());
        assertTrue(texto.contains("dispara una flecha"), texto);
    }

    /**
     * Descripcion:
     * Esta es la diferencia entre las dos versiones: con distancia se puede fallar
     * el disparo. Si el objetivo esta fuera del rango, la flecha se pierde y no se
     * gasta, cosa que la version sin distancia no puede hacer.
     */
    @Test
    void dispararFueraDelRangoNoGastaFlecha() {
        Esqueleto esqueleto = new Esqueleto("Bony", 20);

        String texto = esqueleto.disparar(20.0);

        assertEquals(16, esqueleto.getFlechas(), "si no alcanza, no gasta flecha");
        assertTrue(texto.contains("no alcanza"), texto);
    }

    /**
     * Descripcion:
     * Una distancia negativa se rechaza, igual que en el resto de los mensajes.
     * La version sin distancia no puede dar este error.
     */
    @Test
    void dispararConDistanciaNegativaSeRechaza() {
        Esqueleto esqueleto = new Esqueleto("Bony", 20);

        assertThrows(IllegalArgumentException.class, () -> esqueleto.disparar(-1.0));
        assertEquals(16, esqueleto.getFlechas(), "un disparo rechazado no gasta flecha");
    }

    /**
     * Descripcion:
     * Sin flechas, las dos versiones avisan que ataca cuerpo a cuerpo y no dejan
     * la municion en negativo.
     */
    @Test
    void dispararSinFlechasAtaqueCuerpoACuerpo() {
        Esqueleto esqueleto = new Esqueleto("Bony", 20);
        for (int i = 0; i < 16; i++) {
            esqueleto.disparar();
        }

        assertEquals(0, esqueleto.getFlechas());
        assertTrue(esqueleto.disparar().contains("cuerpo a cuerpo"));
        assertTrue(esqueleto.disparar(3.0).contains("cuerpo a cuerpo"));
        assertEquals(0, esqueleto.getFlechas(), "no puede quedar con flechas negativas");
    }
}
