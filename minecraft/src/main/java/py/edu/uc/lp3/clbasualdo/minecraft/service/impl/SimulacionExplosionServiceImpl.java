package py.edu.uc.lp3.clbasualdo.minecraft.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.Creeper;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.CreeperEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.EntidadEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.ImpactoExplosion;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.SimulacionExplosionEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperMuertoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperYaDetonadoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.EntidadJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.SimulacionExplosionJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionResultado;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionResultado.EstadoCreeper;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionResultado.EstadoEntidad;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionService;

/**
 * Descripción:
 * Implementación de la simulación de explosiones sobre las entidades guardadas en H2.
 *
 * Responsabilidad:
 * Cargar el Creeper guardado, aplicar las dos reglas de la explosión, quitarle vida a las
 * entidades guardadas que estén dentro del radio y guardar todo en una sola transacción.
 * Si una regla falla, se lanza la excepción antes de tocar nada y la base queda igual.
 * El radio y el daño salen del modelo de dominio (Creeper), que es quien conoce los
 * números del juego.
 */
@Service
public class SimulacionExplosionServiceImpl implements SimulacionExplosionService {

    /** Repositorio de la familia de entidades. */
    private final EntidadJpaRepository entidadJpaRepository;

    /** Repositorio de las simulaciones guardadas. */
    private final SimulacionExplosionJpaRepository simulacionJpaRepository;

    /**
     * Descripción:
     * Recibe los repositorios por constructor.
     *
     * Parámetros:
     * entidadJpaRepository - Repositorio de entidades.
     * simulacionJpaRepository - Repositorio de simulaciones.
     */
    public SimulacionExplosionServiceImpl(EntidadJpaRepository entidadJpaRepository,
            SimulacionExplosionJpaRepository simulacionJpaRepository) {
        this.entidadJpaRepository = entidadJpaRepository;
        this.simulacionJpaRepository = simulacionJpaRepository;
    }

    @Override
    @Transactional
    public SimulacionExplosionResultado simular(Long creeperId) {
        CreeperEntity creeper = buscarCreeper(creeperId);
        validarQuePuedeDetonar(creeper);

        Creeper modelo = new Creeper();
        double radio = modelo.getRadioExplosion();
        int danio = modelo.getDanioAtaque();
        int vidaInicialCreeper = creeper.getVida();

        List<ImpactoExplosion> impactos = new ArrayList<>();
        for (EntidadEntity entidad : entidadJpaRepository.findAll()) {
            if (entidad.getId().equals(creeper.getId()) || !entidad.estaViva()) {
                continue;
            }
            double distancia = distancia(creeper, entidad);
            boolean alcanzada = distancia <= radio;
            int vidaInicial = entidad.getVida();
            if (alcanzada) {
                entidad.recibirDanio(danio);
            }
            impactos.add(new ImpactoExplosion(entidad.getId(), tipoDe(entidad), entidad.getNombre(),
                    vidaInicial, entidad.getVida(), distancia, alcanzada));
        }

        creeper.registrarDetonacion();
        entidadJpaRepository.flush();

        SimulacionExplosionEntity simulacion = simulacionJpaRepository.save(new SimulacionExplosionEntity(
                LocalDateTime.now(), radio, creeper, vidaInicialCreeper, impactos));
        return aResultado(simulacion);
    }

    @Override
    @Transactional(readOnly = true)
    public SimulacionExplosionResultado obtener(Long id) {
        return simulacionJpaRepository.findById(id)
                .map(this::aResultado)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una simulación con id " + id + "."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SimulacionExplosionResultado> listar() {
        return simulacionJpaRepository.findAll().stream()
                .map(this::aResultado)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SimulacionExplosionResultado ultima() {
        return simulacionJpaRepository.findFirstByOrderByIdDesc()
                .map(this::aResultado)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Todavía no hay simulaciones guardadas."));
    }

    /**
     * Descripción:
     * Busca la entidad por id y comprueba que sea un Creeper.
     *
     * Parámetros:
     * creeperId - Id pedido.
     *
     * Retorno:
     * El Creeper guardado.
     *
     * Errores:
     * DatosInvalidosException - Si falta el id o la entidad no es un Creeper.
     * RecursoNoEncontradoException - Si no existe.
     */
    private CreeperEntity buscarCreeper(Long creeperId) {
        if (creeperId == null) {
            throw new DatosInvalidosException("Falta el creeperId del Creeper que va a detonar.");
        }
        EntidadEntity entidad = entidadJpaRepository.findById(creeperId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una entidad con id " + creeperId + "."));
        if (!(entidad instanceof CreeperEntity creeper)) {
            throw new DatosInvalidosException(entidad.getNombre() + " no es un Creeper: solo un Creeper puede detonar.");
        }
        return creeper;
    }

    /**
     * Descripción:
     * Aplica las dos reglas de la explosión. Primero la de una sola detonación, porque un
     * Creeper detonado también está muerto y el error más preciso es que ya detonó.
     *
     * Parámetros:
     * creeper - Creeper que quiere detonar.
     *
     * Errores:
     * CreeperYaDetonadoException - Si ya detonó.
     * CreeperMuertoException - Si no tiene vida.
     */
    private void validarQuePuedeDetonar(CreeperEntity creeper) {
        if (creeper.isDetonado()) {
            throw new CreeperYaDetonadoException(creeper.getNombre() + " ya detonó y no puede volver a explotar.");
        }
        if (!creeper.estaViva()) {
            throw new CreeperMuertoException(creeper.getNombre() + " no puede detonar: está muerto.");
        }
    }

    /**
     * Descripción:
     * Calcula la distancia en línea recta entre dos entidades guardadas.
     *
     * Parámetros:
     * a - Primera entidad.
     * b - Segunda entidad.
     *
     * Retorno:
     * La distancia en bloques.
     */
    private double distancia(EntidadEntity a, EntidadEntity b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double dz = a.getZ() - b.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Descripción:
     * Devuelve el subtipo de la entidad tal como aparece en el JSON ("Creeper", "Zombie", "Jugador").
     *
     * Parámetros:
     * entidad - Entidad guardada.
     *
     * Retorno:
     * El nombre del subtipo.
     */
    private String tipoDe(EntidadEntity entidad) {
        return entidad.getClass().getSimpleName().replace("Entity", "");
    }

    /**
     * Descripción:
     * Convierte la fila guardada en el resultado que devuelve la API.
     *
     * Parámetros:
     * simulacion - Simulación guardada.
     *
     * Retorno:
     * El resultado con el Creeper y las entidades separadas en afectadas y no afectadas.
     */
    private SimulacionExplosionResultado aResultado(SimulacionExplosionEntity simulacion) {
        EstadoCreeper creeper = new EstadoCreeper(
                simulacion.getCreeperId(),
                simulacion.getCreeperNombre(),
                simulacion.getCreeperVidaInicial(),
                0,
                true,
                false,
                simulacion.getCreeperX(),
                simulacion.getCreeperY(),
                simulacion.getCreeperZ());
        List<EstadoEntidad> afectados = new ArrayList<>();
        List<EstadoEntidad> noAfectados = new ArrayList<>();
        for (ImpactoExplosion impacto : simulacion.getImpactos()) {
            EstadoEntidad estado = new EstadoEntidad(impacto.getEntidadId(), impacto.getTipo(), impacto.getNombre(),
                    impacto.getVidaInicial(), impacto.getVidaFinal(), impacto.getDistanciaAlCreeper(),
                    impacto.isAlcanzada());
            (impacto.isAlcanzada() ? afectados : noAfectados).add(estado);
        }
        return new SimulacionExplosionResultado(
                simulacion.getId(),
                simulacion.getFecha(),
                simulacion.getRadioExplosion(),
                creeper,
                afectados,
                noAfectados);
    }
}
