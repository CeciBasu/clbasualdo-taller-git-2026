package py.edu.uc.lp3.clbasualdo.minecraft.service.entidad.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.clbasualdo.minecraft.dto.EntidadRequest;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.CreeperEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.EntidadEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.JugadorEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.ZombieEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.EntidadJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.service.entidad.EntidadCrudService;

/**
 * Descripción:
 * Implementación de la capa de negocios para el CRUD de la familia de entidades.
 *
 * Responsabilidad:
 * Aplicar los invariantes antes de guardar. Los datos sueltos: campos obligatorios,
 * nombre único, vida entre 0 y la vida máxima, nivel y experiencia válidos. Y las
 * combinaciones que el juego no permite: un Creeper detonado tiene vida 0 y no vuelve
 * a detonar; un Creeper o un Zombie muerto no revive; una entidad no cambia de tipo.
 * Las Entity no tienen setters públicos: este servicio es el único que decide qué
 * combinación se guarda.
 */
@Service
public class EntidadCrudServiceImpl implements EntidadCrudService {

    /** Experiencia con la que un jugador sube de nivel (la acumulada nunca llega a este valor). */
    private static final int EXPERIENCIA_POR_NIVEL = 100;

    /** Repositorio JPA para acceder a la base de datos H2. */
    private final EntidadJpaRepository entidadJpaRepository;

    /**
     * Descripción:
     * Crea el servicio con el repositorio JPA.
     *
     * Parámetros:
     * entidadJpaRepository - Repositorio de entidades.
     */
    public EntidadCrudServiceImpl(EntidadJpaRepository entidadJpaRepository) {
        this.entidadJpaRepository = entidadJpaRepository;
    }

    @Override
    @Transactional
    public EntidadEntity crear(EntidadRequest pedido) {
        EntidadEntity nueva = construir(pedido);
        if (nueva.getVida() <= 0) {
            throw new DatosInvalidosException("La vida inicial debe ser mayor a 0.");
        }
        if (nueva instanceof CreeperEntity creeper && creeper.isDetonado()) {
            throw new DatosInvalidosException(
                    "Un Creeper no se puede crear detonado: al detonar explota una sola vez y queda con vida 0.");
        }
        if (entidadJpaRepository.existsByNombre(nueva.getNombre())) {
            throw new DatosInvalidosException("Ya existe una entidad con ese nombre.");
        }
        return entidadJpaRepository.save(nueva);
    }

    @Override
    @Transactional
    public EntidadEntity actualizar(Long id, EntidadRequest pedido) {
        return actualizarExistente(obtenerPorId(id), pedido);
    }

    @Override
    @Transactional
    public EntidadEntity actualizarPorNombre(String nombre, EntidadRequest pedido) {
        return actualizarExistente(obtenerPorNombre(nombre), pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public EntidadEntity obtenerPorId(Long id) {
        return entidadJpaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una entidad con id " + id + "."));
    }

    @Override
    @Transactional(readOnly = true)
    public EntidadEntity obtenerPorNombre(String nombre) {
        return entidadJpaRepository.findByNombre(nombre)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una entidad con el nombre " + nombre + "."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntidadEntity> listar() {
        return entidadJpaRepository.findAll();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        entidadJpaRepository.delete(obtenerPorId(id));
    }

    @Override
    @Transactional
    public void eliminarPorNombre(String nombre) {
        entidadJpaRepository.delete(obtenerPorNombre(nombre));
    }

    /**
     * Descripción:
     * Valida un PUT contra el estado guardado y, si es legal, copia los datos nuevos.
     *
     * Parámetros:
     * existente - Entidad guardada.
     * pedido - Datos nuevos.
     *
     * Retorno:
     * La entidad actualizada.
     */
    private EntidadEntity actualizarExistente(EntidadEntity existente, EntidadRequest pedido) {
        EntidadEntity nueva = construir(pedido);
        if (existente.getClass() != nueva.getClass()) {
            throw new DatosInvalidosException("No se puede cambiar el tipo de una entidad existente.");
        }
        validarTransicion(existente, nueva);
        entidadJpaRepository.findByNombre(nueva.getNombre())
                .filter(otra -> !otra.getId().equals(existente.getId()))
                .ifPresent(otra -> {
                    throw new DatosInvalidosException("Ya existe otra entidad con ese nombre.");
                });
        existente.copiarDatosDesde(nueva);
        return entidadJpaRepository.save(existente);
    }

    /**
     * Descripción:
     * Revisa que el paso del estado guardado al nuevo sea posible en el juego.
     *
     * Parámetros:
     * antes - Estado guardado.
     * despues - Estado pedido.
     */
    private void validarTransicion(EntidadEntity antes, EntidadEntity despues) {
        if (antes instanceof CreeperEntity creeperAntes && despues instanceof CreeperEntity creeperDespues) {
            if (creeperAntes.isDetonado() && !creeperDespues.isDetonado()) {
                throw new DatosInvalidosException("Un Creeper detonado no puede volver a estar sin detonar.");
            }
            if (!creeperAntes.isDetonado() && creeperDespues.isDetonado()) {
                throw new DatosInvalidosException(
                        "Un Creeper solo detona con la simulación de explosión, no con un PUT.");
            }
        }
        boolean puedeReaparecer = antes instanceof JugadorEntity;
        if (!puedeReaparecer && !antes.estaViva() && despues.estaViva()) {
            throw new DatosInvalidosException(antes.getNombre() + " está muerto y no puede volver a la vida.");
        }
    }

    /**
     * Descripción:
     * Valida los datos del pedido y construye la Entity del subtipo pedido.
     *
     * Parámetros:
     * pedido - Datos recibidos.
     *
     * Retorno:
     * La Entity armada, todavía sin guardar.
     */
    private EntidadEntity construir(EntidadRequest pedido) {
        if (pedido == null) {
            throw new DatosInvalidosException("Falta el cuerpo con los datos de la entidad.");
        }
        String tipo = obligatorio(pedido.tipo(), "tipo");
        String nombre = obligatorio(pedido.nombre(), "nombre");
        if (nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre no puede estar vacío.");
        }
        int vida = obligatorio(pedido.vida(), "vida");
        int vidaMaxima = pedido.vidaMaxima() == null ? vida : pedido.vidaMaxima();
        double x = obligatorio(pedido.x(), "x");
        double y = obligatorio(pedido.y(), "y");
        double z = obligatorio(pedido.z(), "z");

        if (vida < 0) {
            throw new DatosInvalidosException("La vida no puede ser negativa.");
        }
        if (vidaMaxima <= 0) {
            throw new DatosInvalidosException("La vida máxima debe ser mayor a 0.");
        }
        if (vida > vidaMaxima) {
            throw new DatosInvalidosException("La vida no puede superar la vida máxima.");
        }

        return switch (tipo) {
            case "Creeper" -> {
                boolean detonado = Boolean.TRUE.equals(pedido.detonado());
                if (detonado && vida > 0) {
                    throw new DatosInvalidosException(
                            "Un Creeper detonado no puede tener vida: explota una sola vez y muere.");
                }
                yield new CreeperEntity(nombre, vida, vidaMaxima, x, y, z, detonado);
            }
            case "Zombie" -> new ZombieEntity(nombre, vida, vidaMaxima, x, y, z,
                    Boolean.TRUE.equals(pedido.puertaRota()));
            case "Jugador" -> {
                int nivel = pedido.nivel() == null ? 0 : pedido.nivel();
                int experiencia = pedido.experiencia() == null ? 0 : pedido.experiencia();
                if (nivel < 0) {
                    throw new DatosInvalidosException("El nivel no puede ser negativo.");
                }
                if (experiencia < 0) {
                    throw new DatosInvalidosException("La experiencia no puede ser negativa.");
                }
                if (experiencia >= EXPERIENCIA_POR_NIVEL) {
                    throw new DatosInvalidosException(
                            "La experiencia acumulada va de 0 a 99: al llegar a 100 el jugador sube de nivel.");
                }
                yield new JugadorEntity(nombre, vida, vidaMaxima, x, y, z, nivel, experiencia);
            }
            default -> throw new DatosInvalidosException(
                    "Tipo desconocido: " + tipo + ". Los tipos válidos son Creeper, Zombie y Jugador.");
        };
    }

    /**
     * Descripción:
     * Comprueba que un campo obligatorio haya venido en el JSON.
     *
     * Parámetros:
     * valor - Valor recibido.
     * campo - Nombre del campo, para el mensaje.
     *
     * Retorno:
     * El mismo valor, si no es null.
     */
    private <T> T obligatorio(T valor, String campo) {
        if (valor == null) {
            throw new DatosInvalidosException("Falta el campo obligatorio \"" + campo + "\".");
        }
        return valor;
    }
}
