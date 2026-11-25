package py.edu.uc.lp3.clbasualdo.minecraft.service.entidad;

import java.util.List;

import py.edu.uc.lp3.clbasualdo.minecraft.dto.EntidadRequest;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.EntidadEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RecursoNoEncontradoException;

/**
 * Descripción:
 * Contrato de la capa de negocios para el CRUD de la familia de entidades.
 *
 * Responsabilidad:
 * Validar los datos y las combinaciones posibles en el juego antes de guardar, y
 * ofrecer las consultas y bajas por id y por nombre.
 */
public interface EntidadCrudService {

    /**
     * Descripción:
     * Valida el pedido, construye la entidad del subtipo pedido y la guarda.
     *
     * Parámetros:
     * pedido - Datos recibidos.
     *
     * Retorno:
     * La entidad guardada, con su id.
     *
     * Errores:
     * DatosInvalidosException - Si un dato falta, es inválido o la combinación es imposible.
     */
    EntidadEntity crear(EntidadRequest pedido) throws DatosInvalidosException;

    /**
     * Descripción:
     * Reemplaza los datos de una entidad buscada por id.
     *
     * Parámetros:
     * id - Id de la entidad.
     * pedido - Datos nuevos.
     *
     * Retorno:
     * La entidad actualizada.
     *
     * Errores:
     * RecursoNoEncontradoException - Si no existe.
     * DatosInvalidosException - Si el cambio no es legal.
     */
    EntidadEntity actualizar(Long id, EntidadRequest pedido)
            throws RecursoNoEncontradoException, DatosInvalidosException;

    /**
     * Descripción:
     * Reemplaza los datos de una entidad buscada por nombre.
     *
     * Parámetros:
     * nombre - Nombre de la entidad.
     * pedido - Datos nuevos.
     *
     * Retorno:
     * La entidad actualizada.
     *
     * Errores:
     * RecursoNoEncontradoException - Si no existe.
     * DatosInvalidosException - Si el cambio no es legal.
     */
    EntidadEntity actualizarPorNombre(String nombre, EntidadRequest pedido)
            throws RecursoNoEncontradoException, DatosInvalidosException;

    /**
     * Descripción:
     * Busca una entidad por id.
     *
     * Parámetros:
     * id - Id de la entidad.
     *
     * Retorno:
     * La entidad encontrada.
     *
     * Errores:
     * RecursoNoEncontradoException - Si no existe.
     */
    EntidadEntity obtenerPorId(Long id) throws RecursoNoEncontradoException;

    /**
     * Descripción:
     * Busca una entidad por nombre.
     *
     * Parámetros:
     * nombre - Nombre de la entidad.
     *
     * Retorno:
     * La entidad encontrada.
     *
     * Errores:
     * RecursoNoEncontradoException - Si no existe.
     */
    EntidadEntity obtenerPorNombre(String nombre) throws RecursoNoEncontradoException;

    /**
     * Descripción:
     * Lista todas las entidades guardadas.
     *
     * Retorno:
     * La lista de entidades.
     */
    List<EntidadEntity> listar();

    /**
     * Descripción:
     * Elimina una entidad por id.
     *
     * Parámetros:
     * id - Id de la entidad.
     *
     * Errores:
     * RecursoNoEncontradoException - Si no existe.
     */
    void eliminar(Long id) throws RecursoNoEncontradoException;

    /**
     * Descripción:
     * Elimina una entidad por nombre.
     *
     * Parámetros:
     * nombre - Nombre de la entidad.
     *
     * Errores:
     * RecursoNoEncontradoException - Si no existe.
     */
    void eliminarPorNombre(String nombre) throws RecursoNoEncontradoException;
}
