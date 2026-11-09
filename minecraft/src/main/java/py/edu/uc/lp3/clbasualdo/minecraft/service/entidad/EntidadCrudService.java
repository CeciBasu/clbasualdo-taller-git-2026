package py.edu.uc.lp3.clbasualdo.minecraft.service.entidad;

import java.util.List;

import py.edu.uc.lp3.clbasualdo.minecraft.entity.EntidadEntity;

/**
 * Descripcion:
 * Contrato de la capa de negocios para el CRUD de la familia de entidades.
 *
 * Responsabilidad:
 * Definir las operaciones CRUD con validaciones de negocio, delegando la
 * persistencia al repositorio Spring Data JPA.
 */
public interface EntidadCrudService {

    /**
     * Descripcion:
     * Crea una nueva entidad aplicando validaciones de negocio.
     *
     * Parametros:
     * entidad - Datos de la entidad a crear.
     *
     * Retorno:
     * La entidad creada y persistida.
     */
    EntidadEntity crear(EntidadEntity entidad);

    /**
     * Descripcion:
     * Actualiza una entidad existente buscada por su id.
     *
     * Parametros:
     * id - Id de la entidad a actualizar.
     * entidad - Nuevos datos de la entidad.
     *
     * Retorno:
     * La entidad actualizada y persistida.
     */
    EntidadEntity actualizar(Long id, EntidadEntity entidad);

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
    EntidadEntity actualizarPorNombre(String nombre, EntidadEntity entidad);

    /**
     * Descripcion:
     * Obtiene una entidad por su id.
     *
     * Parametros:
     * id - Id de la entidad a buscar.
     *
     * Retorno:
     * La entidad encontrada.
     */
    EntidadEntity obtenerPorId(Long id);

    /**
     * Descripcion:
     * Obtiene una entidad por su nombre.
     *
     * Parametros:
     * nombre - Nombre de la entidad a buscar.
     *
     * Retorno:
     * La entidad encontrada.
     */
    EntidadEntity obtenerPorNombre(String nombre);

    /**
     * Descripcion:
     * Lista todas las entidades persistidas.
     *
     * Retorno:
     * Lista con todas las entidades.
     */
    List<EntidadEntity> listar();

    /**
     * Descripcion:
     * Elimina una entidad por su id.
     *
     * Parametros:
     * id - Id de la entidad a eliminar.
     */
    void eliminar(Long id);

    /**
     * Descripcion:
     * Elimina una entidad por su nombre.
     *
     * Parametros:
     * nombre - Nombre de la entidad a eliminar.
     */
    void eliminarPorNombre(String nombre);
}
