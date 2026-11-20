package py.edu.uc.lp3.clbasualdo.minecraft.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Descripción:
 * Pruebas de que el modelo se usa polimórficamente.
 *
 * Responsabilidad:
 * Verificar que una lista de Entidad homogénea hace que cada objeto ejecute su
 * propio comportamiento sin que el código tenga que preguntar por el tipo.
 */
class PolimorfismoTest {

    /**
     * Descripción:
     * Una lista de Entidad con hijas de distintos tipos se recorre sin instanceof
     * ni switch: cada objeto responde su propio reaccionar().
     */
    @Test
    void laListaDeEntidadesSeComportaPorHija() {
        List<Entidad> entidades = List.of(
                new Zombie(), new Esqueleto(), new Creeper(), new Enderman(),
                new Aldeano(), new Animal("Vaca", "Vaca"), new Jugador("Steve", 20, 1)
        );

        for (Entidad e : entidades) {
            String reaccion = e.reaccionar();
            assertFalse(reaccion.isBlank(), e.getClass().getSimpleName() + " no debe reaccionar en blanco");
            assertTrue(reaccion.contains(e.getNombre()) || reaccion.contains("Steve"),
                    "la reaccion deberia mencionar a la entidad");
        }
        assertEquals(7, entidades.size());
    }

    /**
     * Descripción:
     * Los dos métodos abstractos de la jerarquía se cumplen en todas las hijas:
     * Entidad.reaccionar() y EntidadHostil.atacar().
     */
    @Test
    void todasLasHijasCumplenLosAbstractos() {
        List<Entidad> hostiles = List.of(new Zombie(), new Esqueleto(), new Creeper(), new Enderman());
        for (Entidad e : hostiles) {
            assertTrue(e instanceof EntidadHostil);
            ((EntidadHostil) e).atacar();
        }
    }

    /**
     * Descripción:
     * Un enemigo golpea a un objetivo tratado como Entidad, sin conocer su clase
     * concreta: da igual que sea pasivo, hostil o el jugador. Cada uno pierde los
     * 4 de daño del esqueleto, partiendo de la vida con la que nazca.
     */
    @Test
    void unHostilGolpeaACualquierEntidad() {
        Esqueleto esqueleto = new Esqueleto();
        esqueleto.teletransportar(0, 0, 0);

        Entidad[] objetivos = {new Aldeano(), new Animal("Vaca", "Vaca"), new Jugador("Steve", 20, 1)};
        for (Entidad objetivo : objetivos) {
            objetivo.teletransportar(1, 0, 0);
            int vidaAntes = objetivo.getVida();
            esqueleto.golpear(objetivo);
            assertEquals(vidaAntes - 4, objetivo.getVida(),
                    objetivo.getClass().getSimpleName() + " deberia haber perdido 4 de vida");
        }
    }
}
