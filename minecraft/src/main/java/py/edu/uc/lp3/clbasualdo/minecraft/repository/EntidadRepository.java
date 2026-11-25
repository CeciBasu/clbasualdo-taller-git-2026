package py.edu.uc.lp3.clbasualdo.minecraft.repository;

import java.util.List;
import java.util.Optional;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.Entidad;

/**
 * Descripción:
 * Contrato para guardar y recuperar las entidades que se van creando en el juego.
 *
 * Responsabilidad:
 * Definir las operaciones de almacenamiento sin decir cómo se implementan, para
 * que el servicio dependa de la interfaz y no de una base de datos concreta.
 */
public interface EntidadRepository {

    /**
     * Descripción:
     * Guarda una entidad. Si ya había una con el mismo nombre, la reemplaza.
     *
     * Parámetros:
     * entidad - La entidad que se quiere guardar.
     *
     * Retorno:
     * La misma entidad que se guardó.
     */
    Entidad guardar(Entidad entidad);

    /**
     * Descripción:
     * Devuelve todas las entidades guardadas.
     *
     * Retorno:
     * Una lista con las entidades, sin exponer la colección interna.
     */
    List<Entidad> listar();

    /**
     * Descripción:
     * Busca una entidad por su nombre.
     *
     * Parámetros:
     * nombre - El nombre que se quiere buscar.
     *
     * Retorno:
     * La entidad encontrada, o vacío si no existe.
     */
    Optional<Entidad> buscarPorNombre(String nombre);

    /**
     * Descripción:
     * Borra la entidad con ese nombre, si existe.
     *
     * Parámetros:
     * nombre - El nombre de la entidad que se quiere borrar.
     *
     * Retorno:
     * true si se borró alguna entidad, false si no había ninguna con ese nombre.
     */
    boolean eliminar(String nombre);
}
