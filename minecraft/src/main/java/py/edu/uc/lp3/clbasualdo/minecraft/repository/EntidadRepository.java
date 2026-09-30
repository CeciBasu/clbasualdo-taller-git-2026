package py.edu.uc.lp3.clbasualdo.minecraft.repository;

import java.util.List;
import java.util.Optional;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.Entidad;

/**
 * Descripcion:
 * Contrato para guardar y recuperar las entidades que se van creando en el juego.
 *
 * Responsabilidad:
 * Definir las operaciones de almacenamiento sin decir como se implementan, para
 * que el servicio dependa de la interfaz y no de una base de datos concreta.
 */
public interface EntidadRepository {

    /**
     * Descripcion:
     * Guarda una entidad. Si ya habia una con el mismo nombre, la reemplaza.
     *
     * Parametros:
     * entidad - La entidad que se quiere guardar.
     *
     * Retorno:
     * La misma entidad que se guardo.
     */
    Entidad guardar(Entidad entidad);

    /**
     * Descripcion:
     * Devuelve todas las entidades guardadas.
     *
     * Retorno:
     * Una lista con las entidades, sin exponer la coleccion interna.
     */
    List<Entidad> listar();

    /**
     * Descripcion:
     * Busca una entidad por su nombre.
     *
     * Parametros:
     * nombre - El nombre que se quiere buscar.
     *
     * Retorno:
     * La entidad encontrada, o vacio si no existe.
     */
    Optional<Entidad> buscarPorNombre(String nombre);

    /**
     * Descripcion:
     * Borra la entidad con ese nombre, si existe.
     *
     * Parametros:
     * nombre - El nombre de la entidad que se quiere borrar.
     *
     * Retorno:
     * true si se borro alguna entidad, false si no habia ninguna con ese nombre.
     */
    boolean eliminar(String nombre);
}
