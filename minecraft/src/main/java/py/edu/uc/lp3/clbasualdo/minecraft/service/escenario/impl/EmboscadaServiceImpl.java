package py.edu.uc.lp3.clbasualdo.minecraft.service.escenario.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import py.edu.uc.lp3.clbasualdo.minecraft.cliente.AceptacionCompanero;
import py.edu.uc.lp3.clbasualdo.minecraft.cliente.ClienteA2MechaCorta;
import py.edu.uc.lp3.clbasualdo.minecraft.cliente.ClienteA3;
import py.edu.uc.lp3.clbasualdo.minecraft.dto.EmboscadaRequest;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.CreeperEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.EmboscadaEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.EntidadEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.JugadorEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.EmboscadaJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.EntidadJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionResultado;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionService;
import py.edu.uc.lp3.clbasualdo.minecraft.service.escenario.EmboscadaResultado;
import py.edu.uc.lp3.clbasualdo.minecraft.service.escenario.EmboscadaResultado.RespuestaCompanero;
import py.edu.uc.lp3.clbasualdo.minecraft.service.escenario.EmboscadaService;
import py.edu.uc.lp3.clbasualdo.minecraft.service.reglas.ReglasExplosionService;

/**
 * Descripción:
 * Implementación del escenario «emboscada del Creeper».
 *
 * Responsabilidad:
 * Orquestar las validaciones en este orden y guardar solo si todas aceptan:
 * 1. Mis reglas (Creeper sin detonar y vivo): no salen de la máquina, son las más baratas.
 * 2. A2, «mecha corta»: es lo primero que pasa en el juego; si ningún jugador está a 3
 *    bloques, el Creeper ni enciende la mecha y no tiene sentido preguntar nada más.
 * 3. A3: se consulta solo con la mecha ya encendida.
 * Las llamadas HTTP van fuera de la transacción. Recién cuando las dos aceptaron se abre una
 * transacción que ejecuta la explosión (que vuelve a aplicar mis reglas sobre H2) y guarda
 * la jugada; si algo falla ahí, se deshace todo.
 */
@Service
public class EmboscadaServiceImpl implements EmboscadaService {

    /** Repositorio de entidades. */
    private final EntidadJpaRepository entidadJpaRepository;

    /** Repositorio de jugadas del escenario. */
    private final EmboscadaJpaRepository emboscadaJpaRepository;

    /** Simulación de explosiones, que persiste la explosión. */
    private final SimulacionExplosionService simulacionExplosionService;

    /** Mis dos reglas. */
    private final ReglasExplosionService reglasExplosionService;

    /** Cliente de la regla de A2. */
    private final ClienteA2MechaCorta clienteA2;

    /** Cliente de la regla de A3. */
    private final ClienteA3 clienteA3;

    /** Transacción para guardar la jugada después de las llamadas HTTP. */
    private final TransactionTemplate transaccion;

    /**
     * Descripción:
     * Recibe las dependencias por constructor.
     *
     * Parámetros:
     * entidadJpaRepository - Repositorio de entidades.
     * emboscadaJpaRepository - Repositorio de jugadas.
     * simulacionExplosionService - Simulación de explosiones.
     * reglasExplosionService - Mis dos reglas.
     * clienteA2 - Cliente de A2.
     * clienteA3 - Cliente de A3.
     * transactionManager - Administrador de transacciones de Spring.
     */
    public EmboscadaServiceImpl(EntidadJpaRepository entidadJpaRepository,
            EmboscadaJpaRepository emboscadaJpaRepository,
            SimulacionExplosionService simulacionExplosionService,
            ReglasExplosionService reglasExplosionService,
            ClienteA2MechaCorta clienteA2,
            ClienteA3 clienteA3,
            PlatformTransactionManager transactionManager) {
        this.entidadJpaRepository = entidadJpaRepository;
        this.emboscadaJpaRepository = emboscadaJpaRepository;
        this.simulacionExplosionService = simulacionExplosionService;
        this.reglasExplosionService = reglasExplosionService;
        this.clienteA2 = clienteA2;
        this.clienteA3 = clienteA3;
        this.transaccion = new TransactionTemplate(transactionManager);
    }

    @Override
    public EmboscadaResultado emboscar(EmboscadaRequest pedido) {
        if (pedido == null || pedido.creeperId() == null || pedido.jugadorId() == null) {
            throw new DatosInvalidosException("Faltan creeperId y jugadorId.");
        }
        CreeperEntity creeper = buscar(pedido.creeperId(), CreeperEntity.class, "un Creeper");
        JugadorEntity jugador = buscar(pedido.jugadorId(), JugadorEntity.class, "un Jugador");

        reglasExplosionService.validarDetonacionUnica(creeper.getNombre(), creeper.isDetonado());
        reglasExplosionService.validarCreeperVivo(creeper.getNombre(), creeper.getVida());

        AceptacionCompanero a2 = clienteA2.validar(creeper, jugador);
        AceptacionCompanero a3 = clienteA3.validar(creeper, jugador);

        EmboscadaEntity guardada = transaccion.execute(estado -> {
            SimulacionExplosionResultado simulacion = simulacionExplosionService.simular(creeper.getId());
            return emboscadaJpaRepository.save(new EmboscadaEntity(LocalDateTime.now(), creeper.getId(),
                    jugador.getId(), simulacion.simulacionId(), a2.companero(), a2.respuesta(),
                    a3.companero(), a3.respuesta()));
        });
        return aResultado(guardada);
    }

    @Override
    public EmboscadaResultado obtener(Long id) {
        return emboscadaJpaRepository.findById(id)
                .map(this::aResultado)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una emboscada con id " + id + "."));
    }

    @Override
    public List<EmboscadaResultado> listar() {
        return emboscadaJpaRepository.findAll().stream().map(this::aResultado).toList();
    }

    /**
     * Descripción:
     * Busca una entidad por id y comprueba su tipo.
     *
     * Parámetros:
     * id - Id de la entidad.
     * tipo - Clase esperada.
     * descripcion - Texto del tipo para el mensaje.
     *
     * Retorno:
     * La entidad con el tipo pedido.
     */
    private <T extends EntidadEntity> T buscar(Long id, Class<T> tipo, String descripcion) {
        EntidadEntity entidad = entidadJpaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una entidad con id " + id + "."));
        if (!tipo.isInstance(entidad)) {
            throw new DatosInvalidosException(entidad.getNombre() + " no es " + descripcion + ".");
        }
        return tipo.cast(entidad);
    }

    /**
     * Descripción:
     * Convierte la jugada guardada en el resultado de la API, con la explosión leída de H2.
     *
     * Parámetros:
     * emboscada - Jugada guardada.
     *
     * Retorno:
     * El resultado.
     */
    private EmboscadaResultado aResultado(EmboscadaEntity emboscada) {
        return new EmboscadaResultado(
                emboscada.getId(),
                emboscada.getFecha(),
                emboscada.getCreeperId(),
                emboscada.getJugadorId(),
                new RespuestaCompanero(emboscada.getCompaneroA2(), emboscada.getRespuestaA2()),
                new RespuestaCompanero(emboscada.getCompaneroA3(), emboscada.getRespuestaA3()),
                simulacionExplosionService.obtener(emboscada.getSimulacionId()));
    }
}
