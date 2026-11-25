package py.edu.uc.lp3.clbasualdo.minecraft.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import py.edu.uc.lp3.clbasualdo.minecraft.entity.CreeperEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.EntidadEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.JugadorEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.ZombieEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperMuertoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperYaDetonadoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RecursoNoEncontradoException;
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
 * Descripción:
 * Pruebas de la simulación de explosiones sobre las entidades guardadas en H2.
 *
 * Responsabilidad:
 * Verificar que la simulación carga el Creeper guardado, daña a las entidades guardadas
 * que están dentro del radio y que las dos reglas (Creeper vivo y una sola detonación)
 * se aplican sobre el estado guardado, sin dejar cambios cuando hay error.
 */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:serviciotest;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class SimulacionExplosionServiceImplTest {

    /** Servicio bajo prueba. */
    @Autowired
    private SimulacionExplosionService servicio;

    /** Repositorio de entidades para preparar y revisar el estado persistido. */
    @Autowired
    private EntidadJpaRepository entidadJpaRepository;

    /** Repositorio de simulaciones para contar lo guardado. */
    @Autowired
    private SimulacionExplosionJpaRepository simulacionExplosionJpaRepository;

    /**
     * Descripción:
     * Cada prueba empieza con la base vacía.
     */
    @BeforeEach
    void limpiar() {
        simulacionExplosionJpaRepository.deleteAll();
        entidadJpaRepository.deleteAll();
    }

    /**
     * Descripción:
     * El Creeper guardado detona, mata al Zombie guardado a 2 bloques y deja intacto al
     * Jugador guardado a 5 bloques. El estado final y la foto quedan en H2.
     */
    @Test
    void laExplosionUsaLasEntidadesGuardadas() {
        CreeperEntity creeper = entidadJpaRepository.save(new CreeperEntity("Creeper A", 20, 0, 64, 0));
        ZombieEntity zombie = entidadJpaRepository.save(new ZombieEntity("Zombie A", 20, 2, 64, 0));
        JugadorEntity jugador = entidadJpaRepository.save(new JugadorEntity("Steve A", 20, 1, 5, 64, 0));

        SimulacionExplosionResultado resultado = servicio.simular(creeper.getId());

        assertNotNull(resultado.simulacionId());
        assertEquals(3.0, resultado.radioExplosion(), 0.0001);
        assertEquals(creeper.getId(), resultado.creeper().id());
        assertEquals(20, resultado.creeper().vidaInicial());
        assertTrue(resultado.creeper().detonado());
        assertEquals(1, resultado.afectados().size());
        assertEquals("Zombie A", resultado.afectados().get(0).nombre());
        assertEquals(0, resultado.afectados().get(0).vidaFinal());
        assertEquals(1, resultado.noAfectados().size());
        assertEquals("Steve A", resultado.noAfectados().get(0).nombre());
        assertEquals(20, resultado.noAfectados().get(0).vidaFinal());

        CreeperEntity creeperGuardado = (CreeperEntity) entidadJpaRepository.findById(creeper.getId()).orElseThrow();
        assertTrue(creeperGuardado.isDetonado());
        assertEquals(0, creeperGuardado.getVida());
        assertEquals(0, entidadJpaRepository.findById(zombie.getId()).orElseThrow().getVida());
        assertEquals(20, entidadJpaRepository.findById(jugador.getId()).orElseThrow().getVida());

        SimulacionExplosionResultado leido = servicio.obtener(resultado.simulacionId());
        assertEquals(resultado.afectados(), leido.afectados());
        assertEquals(resultado.noAfectados(), leido.noAfectados());
    }

    /**
     * Descripción:
     * Un segundo intento sobre el mismo Creeper guardado da CreeperYaDetonadoException y no
     * guarda otra simulación: la regla se aplica sobre lo que hay en H2.
     */
    @Test
    void elMismoCreeperNoDetonaDosVeces() {
        CreeperEntity creeper = entidadJpaRepository.save(new CreeperEntity("Creeper B", 20, 0, 64, 0));
        servicio.simular(creeper.getId());
        long simulaciones = simulacionExplosionJpaRepository.count();

        CreeperYaDetonadoException error = assertThrows(CreeperYaDetonadoException.class,
                () -> servicio.simular(creeper.getId()));

        assertTrue(error.getMessage().contains("ya detonó"));
        assertEquals(simulaciones, simulacionExplosionJpaRepository.count());
    }

    /**
     * Descripción:
     * Un Creeper guardado sin vida (que murió por daño, sin detonar) no puede detonar. Las
     * demás entidades no reciben daño y no se guarda ninguna simulación.
     */
    @Test
    void unCreeperMuertoNoDetona() {
        CreeperEntity creeper = entidadJpaRepository.save(new CreeperEntity("Creeper C", 0, 20, 0, 64, 0, false));
        ZombieEntity zombie = entidadJpaRepository.save(new ZombieEntity("Zombie C", 20, 1, 64, 0));

        CreeperMuertoException error = assertThrows(CreeperMuertoException.class,
                () -> servicio.simular(creeper.getId()));

        assertTrue(error.getMessage().contains("muerto"));
        assertEquals(0, simulacionExplosionJpaRepository.count());
        EntidadEntity zombieGuardado = entidadJpaRepository.findById(zombie.getId()).orElseThrow();
        assertEquals(20, zombieGuardado.getVida());
        CreeperEntity creeperGuardado = (CreeperEntity) entidadJpaRepository.findById(creeper.getId()).orElseThrow();
        assertFalse(creeperGuardado.isDetonado());
    }

    /**
     * Descripción:
     * Solo un Creeper detona, y un id inexistente es 404.
     */
    @Test
    void soloUnCreeperExistentePuedeDetonar() {
        ZombieEntity zombie = entidadJpaRepository.save(new ZombieEntity("Zombie D", 20, 0, 64, 0));

        assertThrows(DatosInvalidosException.class, () -> servicio.simular(zombie.getId()));
        assertThrows(DatosInvalidosException.class, () -> servicio.simular(null));
        assertThrows(RecursoNoEncontradoException.class, () -> servicio.simular(999999L));
        assertEquals(0, simulacionExplosionJpaRepository.count());
    }
}
