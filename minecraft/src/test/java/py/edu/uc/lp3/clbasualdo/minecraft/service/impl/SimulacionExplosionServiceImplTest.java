package py.edu.uc.lp3.clbasualdo.minecraft.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import py.edu.uc.lp3.clbasualdo.minecraft.entity.CreeperEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.EntidadEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperMuertoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperYaDetonadoException;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.EntidadJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.SimulacionExplosionJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionResultado;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Descripcion:
 * Pruebas de la simulacion de explosiones con persistencia en H2.
 *
 * Responsabilidad:
 * Verificar el caso exitoso, el estado guardado de las tres entidades y cada una
 * de las dos reglas del dominio sin dejar filas nuevas cuando hay error.
 */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:serviciotest;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class SimulacionExplosionServiceImplTest {

    /** Servicio bajo prueba. */
    @Autowired
    private SimulacionExplosionService servicio;

    /** Repositorio de entidades para revisar el estado persistido. */
    @Autowired
    private EntidadJpaRepository entidadJpaRepository;

    /** Repositorio de simulaciones para contar lo guardado. */
    @Autowired
    private SimulacionExplosionJpaRepository simulacionExplosionJpaRepository;

    /**
     * Descripcion:
     * El Creeper detona, mata al Zombie que esta dentro del radio y deja intacto
     * al Jugador que esta fuera. El resultado y las tres entidades quedan en H2.
     */
    @Test
    void laExplosionAlcanzaAlZombieYNoAlJugadorYSePersiste() {
        SimulacionExplosionResultado resultado = servicio.simular(20, false);

        assertNotNull(resultado.simulacionId());
        assertNotNull(resultado.fecha());
        assertEquals(3.0, resultado.radioExplosion(), 0.0001);
        assertTrue(resultado.creeper().detonado());
        assertFalse(resultado.creeper().vivo());
        assertEquals(20, resultado.creeper().vidaInicial());
        assertEquals(0, resultado.creeper().vidaFinal());

        assertEquals(1, resultado.afectados().size());
        SimulacionExplosionResultado.EstadoEntidad zombie = resultado.afectados().get(0);
        assertEquals("Zombie", zombie.tipo());
        assertEquals(2.0, zombie.distanciaAlCreeper(), 0.0001);
        assertEquals(20, zombie.vidaInicial());
        assertEquals(0, zombie.vidaFinal());
        assertTrue(zombie.alcanzada());

        assertEquals(1, resultado.noAfectados().size());
        SimulacionExplosionResultado.EstadoEntidad jugador = resultado.noAfectados().get(0);
        assertEquals("Jugador", jugador.tipo());
        assertEquals(5.0, jugador.distanciaAlCreeper(), 0.0001);
        assertEquals(20, jugador.vidaInicial());
        assertEquals(20, jugador.vidaFinal());
        assertFalse(jugador.alcanzada());

        SimulacionExplosionResultado guardado = servicio.obtener(resultado.simulacionId());
        assertEquals(resultado.simulacionId(), guardado.simulacionId());
        assertEquals(resultado.radioExplosion(), guardado.radioExplosion(), 0.0001);

        EntidadEntity creeper = entidadJpaRepository.findByNombre("Creeper Simulado").orElseThrow();
        assertTrue(creeper instanceof CreeperEntity);
        assertEquals(0, creeper.getVida());
        assertTrue(((CreeperEntity) creeper).isDetonado());

        EntidadEntity zombieGuardado = entidadJpaRepository.findByNombre("Zombie Simulado").orElseThrow();
        assertEquals(0, zombieGuardado.getVida());

        EntidadEntity jugadorGuardado = entidadJpaRepository.findByNombre("Steve Simulado").orElseThrow();
        assertEquals(20, jugadorGuardado.getVida());
    }

    /**
     * Descripcion:
     * Un Creeper muerto no puede detonar, el dominio lo informa con su excepcion y
     * no se guarda una simulacion nueva.
     */
    @Test
    void unCreeperMuertoNoPuedeDetonar() {
        long antes = simulacionExplosionJpaRepository.count();

        CreeperMuertoException error = assertThrows(
                CreeperMuertoException.class,
                () -> servicio.simular(0, false));

        assertTrue(error.getMessage().contains("muerto"));
        assertEquals(antes, simulacionExplosionJpaRepository.count());
    }

    /**
     * Descripcion:
     * Un segundo intento de detonacion sobre el mismo Creeper viola la regla y no
     * se guarda una simulacion nueva.
     */
    @Test
    void unCreeperNoPuedeDetonarDosVeces() {
        long antes = simulacionExplosionJpaRepository.count();

        CreeperYaDetonadoException error = assertThrows(
                CreeperYaDetonadoException.class,
                () -> servicio.simular(20, true));

        assertTrue(error.getMessage().contains("ya detonó"));
        assertEquals(antes, simulacionExplosionJpaRepository.count());
    }
}
