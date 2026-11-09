package py.edu.uc.lp3.clbasualdo.minecraft.service.entidad.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.clbasualdo.minecraft.entity.EntidadEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.JugadorEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa.EntidadJpaRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.service.entidad.EntidadCrudService;

/**
 * Descripcion:
 * Implementacion de la capa de negocios para el CRUD de la familia de entidades.
 *
 * Responsabilidad:
 * Aplicar los invariantes de construccion y actualizacion (nombre obligatorio,
 * vida dentro del rango, nombres unicos y tipo estable) y delegar la persistencia
 * a EntidadJpaRepository.
 */
@Service
public class EntidadCrudServiceImpl implements EntidadCrudService {

    /** Repositorio JPA para acceder a la base de datos H2. */
    private final EntidadJpaRepository entidadJpaRepository;

    /**
     * Descripcion:
     * Crea el servicio con el repositorio JPA.
     *
     * Parametros:
     * entidadJpaRepository - Repositorio de entidades.
     */
    public EntidadCrudServiceImpl(EntidadJpaRepository entidadJpaRepository) {
        this.entidadJpaRepository = entidadJpaRepository;
    }

    /**
     * Descripcion:
     * Crea una nueva entidad. Exige nombre no vacio, vida inicial mayor a 0,
     * vida maxima positiva y que el nombre no exista. Si la vida maxima no viene,
     * se toma igual a la vida inicial.
     *
     * Parametros:
     * entidad - Datos de la entidad a crear.
     *
     * Retorno:
     * La entidad creada y persistida.
     */
    @Override
    @Transactional
    public EntidadEntity crear(EntidadEntity entidad) {
        if (entidad == null) {
            throw new DatosInvalidosException("La entidad no puede ser nula.");
        }
        normalizarVidaMaxima(entidad);
        validarComun(entidad);
        if (entidad.getVida() <= 0) {
            throw new DatosInvalidosException("La vida inicial debe ser mayor a 0.");
        }
        if (entidadJpaRepository.existsByNombre(entidad.getNombre())) {
            throw new DatosInvalidosException("Ya existe una entidad con ese nombre.");
        }
        return entidadJpaRepository.save(entidad);
    }

    /**
     * Descripcion:
     * Actualiza una entidad existente buscada por su id. No permite cambiar el
     * subtipo (por ejemplo, convertir un Zombie en un Creeper) ni repetir el
     * nombre de otra fila.
     *
     * Parametros:
     * id - Id de la entidad a actualizar.
     * entidad - Nuevos datos de la entidad.
     *
     * Retorno:
     * La entidad actualizada y persistida.
     */
    @Override
    @Transactional
    public EntidadEntity actualizar(Long id, EntidadEntity entidad) {
        EntidadEntity existente = obtenerPorId(id);
        return actualizarExistente(existente, entidad);
    }

    /**
     * Descripcion:
     * Actualiza una entidad existente buscada por su nombre.
     *
     * Parametros:
     * nombre - Nombre de la entidad a actualizar.
     * entidad - Nuevos datos de la entidad.
     *
     * Retorno:
     * La entidad actualizada y persistida.
     */
    @Override
    @Transactional
    public EntidadEntity actualizarPorNombre(String nombre, EntidadEntity entidad) {
        EntidadEntity existente = obtenerPorNombre(nombre);
        return actualizarExistente(existente, entidad);
    }

    /**
     * Descripcion:
     * Obtiene una entidad por su id o lanza una excepcion de recurso inexistente.
     *
     * Parametros:
     * id - Id de la entidad a buscar.
     *
     * Retorno:
     * La entidad encontrada.
     */
    @Override
    @Transactional(readOnly = true)
    public EntidadEntity obtenerPorId(Long id) {
        return entidadJpaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una entidad con id " + id + "."));
    }

    /**
     * Descripcion:
     * Obtiene una entidad por su nombre o lanza una excepcion de recurso inexistente.
     *
     * Parametros:
     * nombre - Nombre de la entidad a buscar.
     *
     * Retorno:
     * La entidad encontrada.
     */
    @Override
    @Transactional(readOnly = true)
    public EntidadEntity obtenerPorNombre(String nombre) {
        return entidadJpaRepository.findByNombre(nombre)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una entidad con el nombre " + nombre + "."));
    }

    /**
     * Descripcion:
     * Lista todas las entidades persistidas en la base de datos.
     *
     * Retorno:
     * Lista con todas las entidades.
     */
    @Override
    @Transactional(readOnly = true)
    public List<EntidadEntity> listar() {
        return entidadJpaRepository.findAll();
    }

    /**
     * Descripcion:
     * Elimina una entidad por su id.
     *
     * Parametros:
     * id - Id de la entidad a eliminar.
     */
    @Override
    @Transactional
    public void eliminar(Long id) {
        EntidadEntity existente = obtenerPorId(id);
        entidadJpaRepository.delete(existente);
    }

    /**
     * Descripcion:
     * Elimina una entidad por su nombre.
     *
     * Parametros:
     * nombre - Nombre de la entidad a eliminar.
     */
    @Override
    @Transactional
    public void eliminarPorNombre(String nombre) {
        EntidadEntity existente = obtenerPorNombre(nombre);
        entidadJpaRepository.delete(existente);
    }

    /**
     * Descripcion:
     * Aplica las validaciones y copia los datos sobre la entidad ya persistida.
     *
     * Parametros:
     * existente - Entidad que ya esta en la base.
     * entidad - Datos nuevos que llegan por la API.
     *
     * Retorno:
     * La entidad actualizada y persistida.
     */
    private EntidadEntity actualizarExistente(EntidadEntity existente, EntidadEntity entidad) {
        if (entidad == null) {
            throw new DatosInvalidosException("La entidad no puede ser nula.");
        }
        if (existente.getClass() != entidad.getClass()) {
            throw new DatosInvalidosException("No se puede cambiar el tipo de una entidad existente.");
        }
        normalizarVidaMaxima(entidad);
        validarComun(entidad);
        entidadJpaRepository.findByNombre(entidad.getNombre())
                .filter(otra -> !otra.getId().equals(existente.getId()))
                .ifPresent(otra -> {
                    throw new DatosInvalidosException("Ya existe otra entidad con ese nombre.");
                });
        existente.copiarDatosDesde(entidad);
        return entidadJpaRepository.save(existente);
    }

    /**
     * Descripcion:
     * Si la vida maxima no vino o no es positiva, la iguala a la vida actual.
     *
     * Parametros:
     * entidad - Entidad a normalizar.
     */
    private void normalizarVidaMaxima(EntidadEntity entidad) {
        if (entidad.getVidaMaxima() <= 0) {
            entidad.setVidaMaxima(entidad.getVida());
        }
    }

    /**
     * Descripcion:
     * Valida los invariantes comunes a todos los subtipos.
     *
     * Parametros:
     * entidad - Entidad a validar.
     */
    private void validarComun(EntidadEntity entidad) {
        if (entidad.getNombre() == null || entidad.getNombre().isBlank()) {
            throw new DatosInvalidosException("El nombre no puede estar vacío.");
        }
        if (entidad.getVidaMaxima() <= 0) {
            throw new DatosInvalidosException("La vida máxima debe ser mayor a 0.");
        }
        if (entidad.getVida() < 0) {
            throw new DatosInvalidosException("La vida no puede ser negativa.");
        }
        if (entidad.getVida() > entidad.getVidaMaxima()) {
            throw new DatosInvalidosException("La vida no puede superar la vida máxima.");
        }
        if (entidad instanceof JugadorEntity jugador) {
            if (jugador.getNivel() < 0) {
                throw new DatosInvalidosException("El nivel no puede ser negativo.");
            }
            if (jugador.getExperiencia() < 0) {
                throw new DatosInvalidosException("La experiencia no puede ser negativa.");
            }
        }
    }
}
