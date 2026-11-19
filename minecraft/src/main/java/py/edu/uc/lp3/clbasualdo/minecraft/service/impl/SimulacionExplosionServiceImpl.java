package py.edu.uc.lp3.clbasualdo.minecraft.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.Creeper;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Entidad;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.CreeperEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.EntidadEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.JugadorEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.SimulacionExplosionEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.ZombieEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperMuertoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperYaDetonadoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.EntidadJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.SimulacionExplosionJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionResultado;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionResultado.EstadoCreeper;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionResultado.EstadoEntidad;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionService;

/**
 * Implementación de la simulación de explosiones usando entidades guardadas en H2.
 */
@Service
public class SimulacionExplosionServiceImpl implements SimulacionExplosionService {

	private static final double ALTURA = 64.0;
	private static final double X_ZOMBIE = 2.0;
	private static final double X_JUGADOR = 5.0;
	private static final int VIDA_JUGADOR = 20;
	private static final String NOMBRE_CREEPER = "Creeper";
	private static final String NOMBRE_ZOMBIE = "Zombie";
	private static final String NOMBRE_JUGADOR = "Steve";

	private final EntidadJpaRepository entidadJpaRepository;
	private final SimulacionExplosionJpaRepository simulacionJpaRepository;

	public SimulacionExplosionServiceImpl(EntidadJpaRepository entidadJpaRepository,
			SimulacionExplosionJpaRepository simulacionJpaRepository) {
		this.entidadJpaRepository = entidadJpaRepository;
		this.simulacionJpaRepository = simulacionJpaRepository;
	}

	@Override
	@Transactional
	public SimulacionExplosionResultado simular(int vidaCreeperParam, boolean repetirDetonacion) {
		CreeperEntity creeperEntity = obtenerOCrearCreeper(vidaCreeperParam);
		ZombieEntity zombieEntity = obtenerOCrearZombie();
		JugadorEntity jugadorEntity = obtenerOCrearJugador();

		if (creeperEntity.getVida() <= 0 || creeperEntity.isDetonado()) {
			throw new CreeperMuertoException(NOMBRE_CREEPER + " no puede detonar: está muerto.");
		}
		if (repetirDetonacion) {
			throw new CreeperYaDetonadoException(NOMBRE_CREEPER + " ya detonó y no puede volver a explotar.");
		}

		Creeper creeper = new Creeper();
		creeper.teletransportar(creeperEntity.getX(), creeperEntity.getY(), creeperEntity.getZ());
		int vidaAntes = creeperEntity.getVida();
		// sincronizar vida del creeper de dominio con la persistida
		while (creeper.getVida() > vidaAntes && creeper.getVida() > 0) {
			creeper.recibirDanio(1);
		}
		// si la vida persistida es menor a la inicial del dominio, bajarla
		int diff = new Creeper().getVida() - vidaAntes;
		if (diff > 0) {
			creeper.recibirDanio(diff);
		}

		py.edu.uc.lp3.clbasualdo.minecraft.domain.Zombie zombie = new py.edu.uc.lp3.clbasualdo.minecraft.domain.Zombie();
		zombie.teletransportar(zombieEntity.getX(), zombieEntity.getY(), zombieEntity.getZ());
		int vidaInicialZombie = zombieEntity.getVida();
		// ajustar vida del zombie de dominio
		while (zombie.getVida() > vidaInicialZombie && zombie.getVida() > 0) {
			zombie.recibirDanio(1);
		}

		py.edu.uc.lp3.clbasualdo.minecraft.domain.Jugador jugador = new py.edu.uc.lp3.clbasualdo.minecraft.domain.Jugador(
				jugadorEntity.getNombre(), Math.max(1, jugadorEntity.getVida()), Math.max(1, jugadorEntity.getNivel()));
		jugador.teletransportar(jugadorEntity.getX(), jugadorEntity.getY(), jugadorEntity.getZ());
		int vidaInicialJugador = jugadorEntity.getVida();

		double distanciaZombie = creeper.distanciaHacia(zombie);
		double distanciaJugador = creeper.distanciaHacia(jugador);
		double radioExplosion = creeper.getRadioExplosion();

		List<Entidad> cercanas = List.of(zombie, jugador);
		creeper.detonar(cercanas);

		creeperEntity.setVida(creeper.getVida());
		creeperEntity.setVidaMaxima(Math.max(creeper.getVidaMaxima(), creeper.getVida()));
		creeperEntity.setDetonado(creeper.isDetonado());
		creeperEntity.setX(creeper.getX());
		creeperEntity.setY(creeper.getY());
		creeperEntity.setZ(creeper.getZ());
		entidadJpaRepository.save(creeperEntity);

		zombieEntity.setVida(zombie.getVida());
		entidadJpaRepository.save(zombieEntity);
		jugadorEntity.setVida(jugador.getVida());
		entidadJpaRepository.save(jugadorEntity);

		EstadoCreeper estadoCreeper = new EstadoCreeper(creeper.getNombre(), vidaAntes, creeper.getVida(), creeper.isDetonado(),
				creeper.estaVivo(), creeper.getX(), creeper.getY(), creeper.getZ());
		EstadoEntidad estadoZombie = new EstadoEntidad("Zombie", zombie.getNombre(), vidaInicialZombie, zombie.getVida(),
				distanciaZombie, distanciaZombie <= radioExplosion);
		EstadoEntidad estadoJugador = new EstadoEntidad("Jugador", jugador.getNombre(), vidaInicialJugador, jugador.getVida(),
				distanciaJugador, distanciaJugador <= radioExplosion);

		List<EstadoEntidad> afectados = new ArrayList<>();
		List<EstadoEntidad> noAfectados = new ArrayList<>();
		if (estadoZombie.alcanzada())
			afectados.add(estadoZombie);
		else
			noAfectados.add(estadoZombie);
		if (estadoJugador.alcanzada())
			afectados.add(estadoJugador);
		else
			noAfectados.add(estadoJugador);

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

		return new SimulacionExplosionResultado(simulacion.getId(), simulacion.getFecha(), radioExplosion, estadoCreeper, afectados,
				noAfectados);
	}

	private CreeperEntity obtenerOCrearCreeper(int vidaParam) {
		EntidadEntity existente = entidadJpaRepository.findByNombre(NOMBRE_CREEPER).orElse(null);
		if (existente instanceof CreeperEntity c) {
			// Para tests: si piden vida 0, marcar como muerto/detonado incoherente? Mejor alinear con tests
			if (vidaParam <= 0) {
				c.setVida(0);
				c.setDetonado(false);
			} else {
				c.setVida(vidaParam);
				c.setVidaMaxima(Math.max(c.getVidaMaxima(), vidaParam));
				c.setDetonado(false);
			}
			return entidadJpaRepository.save(c);
		}
		if (existente != null)
			entidadJpaRepository.delete(existente);
		int vida = vidaParam > 0 ? vidaParam : 20;
		CreeperEntity c = new CreeperEntity(NOMBRE_CREEPER, vida, 0, ALTURA, 0);
		c.setDetonado(false);
		if (vidaParam <= 0) c.setVida(0);
		return entidadJpaRepository.save(c);
	}

	private ZombieEntity obtenerOCrearZombie() {
		EntidadEntity existente = entidadJpaRepository.findByNombre(NOMBRE_ZOMBIE).orElse(null);
		if (existente instanceof ZombieEntity z)
			return z;
		if (existente != null)
			entidadJpaRepository.delete(existente);
		ZombieEntity z = new ZombieEntity(NOMBRE_ZOMBIE, 20, X_ZOMBIE, ALTURA, 0);
		return entidadJpaRepository.save(z);
	}

	private JugadorEntity obtenerOCrearJugador() {
		EntidadEntity existente = entidadJpaRepository.findByNombre(NOMBRE_JUGADOR).orElse(null);
		if (existente instanceof JugadorEntity j)
			return j;
		if (existente != null)
			entidadJpaRepository.delete(existente);
		JugadorEntity j = new JugadorEntity(NOMBRE_JUGADOR, VIDA_JUGADOR, 1, X_JUGADOR, ALTURA, 0);
		return entidadJpaRepository.save(j);
	}

	@Override
	@Transactional(readOnly = true)
	public SimulacionExplosionResultado obtener(Long id) {
		return simulacionJpaRepository.findById(id).map(this::aResultado)
				.orElseThrow(() -> new RecursoNoEncontradoException("No existe una simulación con id " + id + "."));
	}

	@Override
	@Transactional(readOnly = true)
	public List<SimulacionExplosionResultado> listar() {
		return simulacionJpaRepository.findAll().stream().map(this::aResultado).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public SimulacionExplosionResultado ultima() {
		return simulacionJpaRepository.findFirstByOrderByIdDesc().map(this::aResultado)
				.orElseThrow(() -> new RecursoNoEncontradoException("Todavía no hay simulaciones guardadas."));
	}

	private SimulacionExplosionResultado aResultado(SimulacionExplosionEntity s) {
		EstadoCreeper creeper = new EstadoCreeper(s.getCreeperNombre(), s.getCreeperVidaInicial(), s.getCreeperVidaFinal(),
				s.isCreeperDetonado(), s.getCreeperVidaFinal() > 0, s.getCreeperX(), s.getCreeperY(), s.getCreeperZ());
		EstadoEntidad zombie = new EstadoEntidad("Zombie", s.getZombieNombre(), s.getZombieVidaInicial(), s.getZombieVidaFinal(),
				s.getZombieDistancia(), s.isZombieAlcanzado());
		EstadoEntidad jugador = new EstadoEntidad("Jugador", s.getJugadorNombre(), s.getJugadorVidaInicial(),
				s.getJugadorVidaFinal(), s.getJugadorDistancia(), s.isJugadorAlcanzado());
		List<EstadoEntidad> afectados = new ArrayList<>();
		List<EstadoEntidad> noAfectados = new ArrayList<>();
		if (zombie.alcanzada())
			afectados.add(zombie);
		else
			noAfectados.add(zombie);
		if (jugador.alcanzada())
			afectados.add(jugador);
		else
			noAfectados.add(jugador);
		return new SimulacionExplosionResultado(s.getId(), s.getFecha(), s.getRadioExplosion(), creeper, afectados, noAfectados);
	}
}
