package py.edu.uc.lp3.clbasualdo.minecraft.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.Creeper;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Entidad;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Jugador;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Zombie;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.CreeperEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.EntidadEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.JugadorEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.SimulacionExplosionEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.ZombieEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.EntidadJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.SimulacionExplosionJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionResultado;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionResultado.EstadoCreeper;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionResultado.EstadoEntidad;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionService;

/**
 * Descripcion:
 * Implementacion de la simulacion de explosiones con persistencia en H2.
 *
 * Responsabilidad:
 * Armar el escenario con Creeper, Zombie y Jugador, ejecutar la detonacion en el
 * dominio, guardar el estado final de las tres entidades y registrar la
 * simulacion para que un GET posterior muestre el resultado.
 */
@Service
public class SimulacionExplosionServiceImpl implements SimulacionExplosionService {

    /** Altura comun del escenario. */
    private static final double ALTURA = 64.0;
    /** Posicion del Zombie en X: queda dentro del radio de explosion. */
    private static final double X_ZOMBIE = 2.0;
    /** Posicion del Jugador en X: queda fuera del radio de explosion. */
    private static final double X_JUGADOR = 5.0;
    /** Vida inicial del Jugador del escenario. */
    private static final int VIDA_JUGADOR = 20;
    /** Nombre fijo con el que se persiste el Creeper de la simulacion. */
    private static final String NOMBRE_CREEPER = "Creeper Simulado";
    /** Nombre fijo con el que se persiste el Zombie de la simulacion. */
    private static final String NOMBRE_ZOMBIE = "Zombie Simulado";
    /** Nombre fijo con el que se persiste el Jugador de la simulacion. */
    private static final String NOMBRE_JUGADOR = "Steve Simulado";

    /** Repositorio de la familia de entidades. */
    private final EntidadJpaRepository entidadJpaRepository;
    /** Repositorio de las simulaciones. */
    private final SimulacionExplosionJpaRepository simulacionJpaRepository;

    /**
     * Descripcion:
     * Crea el servicio con los dos repositorios que necesita.
     *
     * Parametros:
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
    public SimulacionExplosionResultado simular(int vidaCreeper, boolean repetirDetonacion) {
        Creeper creeper = new Creeper();
        creeper.teletransportar(0, ALTURA, 0);
        int vidaInicialCreeper = ajustarVida(creeper, vidaCreeper);

        Zombie zombie = new Zombie();
        zombie.teletransportar(X_ZOMBIE, ALTURA, 0);

        Jugador jugador = new Jugador("Steve", VIDA_JUGADOR, 1);
        jugador.teletransportar(X_JUGADOR, ALTURA, 0);

        int vidaInicialZombie = zombie.getVida();
        int vidaInicialJugador = jugador.getVida();
        double distanciaZombie = creeper.distanciaHacia(zombie);
        double distanciaJugador = creeper.distanciaHacia(jugador);
        double radioExplosion = creeper.getRadioExplosion();

        List<Entidad> cercanas = List.of(zombie, jugador);
        creeper.detonar(cercanas);
        if (repetirDetonacion) {
            creeper.detonar(cercanas);
        }

        EstadoCreeper estadoCreeper = new EstadoCreeper(
                creeper.getNombre(),
                vidaInicialCreeper,
                creeper.getVida(),
                creeper.isDetonado(),
                creeper.estaVivo(),
                creeper.getX(),
                creeper.getY(),
                creeper.getZ());

        EstadoEntidad estadoZombie = new EstadoEntidad(
                zombie.getClass().getSimpleName(),
                zombie.getNombre(),
                vidaInicialZombie,
                zombie.getVida(),
                distanciaZombie,
                distanciaZombie <= radioExplosion);

        EstadoEntidad estadoJugador = new EstadoEntidad(
                jugador.getClass().getSimpleName(),
                jugador.getNombre(),
                vidaInicialJugador,
                jugador.getVida(),
                distanciaJugador,
                distanciaJugador <= radioExplosion);

        List<EstadoEntidad> afectados = new ArrayList<>();
        List<EstadoEntidad> noAfectados = new ArrayList<>();
        clasificar(estadoZombie, afectados, noAfectados);
        clasificar(estadoJugador, afectados, noAfectados);

        CreeperEntity creeperEntity = guardarCreeper(estadoCreeper);
        ZombieEntity zombieEntity = guardarZombie(estadoZombie);
        JugadorEntity jugadorEntity = guardarJugador(estadoJugador);

        SimulacionExplosionEntity simulacion = new SimulacionExplosionEntity();
        simulacion.setFecha(LocalDateTime.now());
        simulacion.setRadioExplosion(radioExplosion);
        simulacion.setCreeperId(creeperEntity.getId());
        simulacion.setZombieId(zombieEntity.getId());
        simulacion.setJugadorId(jugadorEntity.getId());
        simulacion.setCreeperNombre(estadoCreeper.nombre());
        simulacion.setCreeperVidaInicial(estadoCreeper.vidaInicial());
        simulacion.setCreeperVidaFinal(estadoCreeper.vidaFinal());
        simulacion.setCreeperDetonado(estadoCreeper.detonado());
        simulacion.setCreeperX(estadoCreeper.x());
        simulacion.setCreeperY(estadoCreeper.y());
        simulacion.setCreeperZ(estadoCreeper.z());
        simulacion.setZombieNombre(estadoZombie.nombre());
        simulacion.setZombieVidaInicial(estadoZombie.vidaInicial());
        simulacion.setZombieVidaFinal(estadoZombie.vidaFinal());
        simulacion.setZombieDistancia(estadoZombie.distanciaAlCreeper());
        simulacion.setZombieAlcanzado(estadoZombie.alcanzada());
        simulacion.setJugadorNombre(estadoJugador.nombre());
        simulacion.setJugadorVidaInicial(estadoJugador.vidaInicial());
        simulacion.setJugadorVidaFinal(estadoJugador.vidaFinal());
        simulacion.setJugadorDistancia(estadoJugador.distanciaAlCreeper());
        simulacion.setJugadorAlcanzado(estadoJugador.alcanzada());
        simulacion = simulacionJpaRepository.save(simulacion);

        return new SimulacionExplosionResultado(
                simulacion.getId(),
                simulacion.getFecha(),
                radioExplosion,
                estadoCreeper,
                afectados,
                noAfectados);
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
     * Descripcion:
     * Convierte una simulacion persistida en el resultado que devuelve la API.
     *
     * Parametros:
     * simulacion - La fila guardada en H2.
     *
     * Retorno:
     * El resultado con el mismo formato que devuelve el POST.
     */
    private SimulacionExplosionResultado aResultado(SimulacionExplosionEntity simulacion) {
        EstadoCreeper creeper = new EstadoCreeper(
                simulacion.getCreeperNombre(),
                simulacion.getCreeperVidaInicial(),
                simulacion.getCreeperVidaFinal(),
                simulacion.isCreeperDetonado(),
                simulacion.getCreeperVidaFinal() > 0,
                simulacion.getCreeperX(),
                simulacion.getCreeperY(),
                simulacion.getCreeperZ());

        EstadoEntidad zombie = new EstadoEntidad(
                "Zombie",
                simulacion.getZombieNombre(),
                simulacion.getZombieVidaInicial(),
                simulacion.getZombieVidaFinal(),
                simulacion.getZombieDistancia(),
                simulacion.isZombieAlcanzado());

        EstadoEntidad jugador = new EstadoEntidad(
                "Jugador",
                simulacion.getJugadorNombre(),
                simulacion.getJugadorVidaInicial(),
                simulacion.getJugadorVidaFinal(),
                simulacion.getJugadorDistancia(),
                simulacion.isJugadorAlcanzado());

        List<EstadoEntidad> afectados = new ArrayList<>();
        List<EstadoEntidad> noAfectados = new ArrayList<>();
        clasificar(zombie, afectados, noAfectados);
        clasificar(jugador, afectados, noAfectados);

        return new SimulacionExplosionResultado(
                simulacion.getId(),
                simulacion.getFecha(),
                simulacion.getRadioExplosion(),
                creeper,
                afectados,
                noAfectados);
    }

    /**
     * Descripcion:
     * Busca una entidad del subtipo pedido por nombre. Si existe una fila con el
     * mismo nombre pero de otro subtipo, la borra para poder recrearla.
     *
     * Parametros:
     * nombre - Nombre a buscar.
     * tipo - Clase concreta esperada.
     *
     * Retorno:
     * Optional con la entidad del tipo pedido.
     */
    private <T extends EntidadEntity> Optional<T> buscar(String nombre, Class<T> tipo) {
        Optional<EntidadEntity> existente = entidadJpaRepository.findByNombre(nombre);
        if (existente.isPresent() && !tipo.isInstance(existente.get())) {
            entidadJpaRepository.delete(existente.get());
            return Optional.empty();
        }
        return existente.map(tipo::cast);
    }

    /**
     * Descripcion:
     * Guarda o actualiza la fila del Creeper de la simulacion.
     *
     * Parametros:
     * estado - Estado final del Creeper.
     *
     * Retorno:
     * La entidad persistida.
     */
    private CreeperEntity guardarCreeper(EstadoCreeper estado) {
        CreeperEntity entidad = buscar(NOMBRE_CREEPER, CreeperEntity.class)
                .orElseGet(() -> new CreeperEntity(
                        NOMBRE_CREEPER,
                        Math.max(1, estado.vidaInicial()),
                        estado.x(),
                        estado.y(),
                        estado.z()));
        entidad.setNombre(NOMBRE_CREEPER);
        entidad.setVida(estado.vidaFinal());
        entidad.setVidaMaxima(Math.max(1, estado.vidaInicial()));
        entidad.setX(estado.x());
        entidad.setY(estado.y());
        entidad.setZ(estado.z());
        entidad.setDetonado(estado.detonado());
        return entidadJpaRepository.save(entidad);
    }

    /**
     * Descripcion:
     * Guarda o actualiza la fila del Zombie de la simulacion.
     *
     * Parametros:
     * estado - Estado final del Zombie.
     *
     * Retorno:
     * La entidad persistida.
     */
    private ZombieEntity guardarZombie(EstadoEntidad estado) {
        ZombieEntity entidad = buscar(NOMBRE_ZOMBIE, ZombieEntity.class)
                .orElseGet(() -> new ZombieEntity(
                        NOMBRE_ZOMBIE,
                        Math.max(1, estado.vidaInicial()),
                        X_ZOMBIE,
                        ALTURA,
                        0));
        entidad.setNombre(NOMBRE_ZOMBIE);
        entidad.setVida(estado.vidaFinal());
        entidad.setVidaMaxima(Math.max(1, estado.vidaInicial()));
        entidad.setX(X_ZOMBIE);
        entidad.setY(ALTURA);
        entidad.setZ(0);
        entidad.setPuertaRota(false);
        return entidadJpaRepository.save(entidad);
    }

    /**
     * Descripcion:
     * Guarda o actualiza la fila del Jugador de la simulacion.
     *
     * Parametros:
     * estado - Estado final del Jugador.
     *
     * Retorno:
     * La entidad persistida.
     */
    private JugadorEntity guardarJugador(EstadoEntidad estado) {
        JugadorEntity entidad = buscar(NOMBRE_JUGADOR, JugadorEntity.class)
                .orElseGet(() -> new JugadorEntity(
                        NOMBRE_JUGADOR,
                        Math.max(1, estado.vidaInicial()),
                        1,
                        X_JUGADOR,
                        ALTURA,
                        0));
        entidad.setNombre(NOMBRE_JUGADOR);
        entidad.setVida(estado.vidaFinal());
        entidad.setVidaMaxima(Math.max(1, estado.vidaInicial()));
        entidad.setX(X_JUGADOR);
        entidad.setY(ALTURA);
        entidad.setZ(0);
        entidad.setNivel(1);
        entidad.setExperiencia(0);
        return entidadJpaRepository.save(entidad);
    }

    /**
     * Descripcion:
     * Ajusta la vida del Creeper al valor pedido sin permitir valores fuera del
     * rango valido. Si se pide 0 o menos, el Creeper queda muerto.
     *
     * Parametros:
     * creeper - El Creeper a ajustar.
     * vidaPedida - La vida que se quiere simular.
     *
     * Retorno:
     * La vida inicial que quedo para el Creeper.
     */
    private int ajustarVida(Creeper creeper, int vidaPedida) {
        int vidaMaxima = creeper.getVida();
        int vidaObjetivo = Math.max(0, Math.min(vidaPedida, vidaMaxima));
        creeper.recibirDanio(vidaMaxima - vidaObjetivo);
        return vidaObjetivo;
    }

    /**
     * Descripcion:
     * Agrega el estado de una entidad a la lista que corresponda segun si la
     * explosion la alcanzo o no.
     *
     * Parametros:
     * estado - Estado de la entidad.
     * afectados - Lista de entidades alcanzadas.
     * noAfectados - Lista de entidades fuera del radio.
     */
    private void clasificar(EstadoEntidad estado, List<EstadoEntidad> afectados, List<EstadoEntidad> noAfectados) {
        if (estado.alcanzada()) {
            afectados.add(estado);
        } else {
            noAfectados.add(estado);
        }
    }
}
