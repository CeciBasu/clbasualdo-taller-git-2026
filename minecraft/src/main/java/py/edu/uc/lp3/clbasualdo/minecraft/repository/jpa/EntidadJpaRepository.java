package py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import py.edu.uc.lp3.clbasualdo.minecraft.entity.EntidadEntity;

/**
 * Descripcion:
 * Repositorio Spring Data JPA para la familia de entidades persistidas.
 *
 * Responsabilidad:
 * Proveer operaciones CRUD sobre la tabla "entidades" y consultas por nombre o
 * por vida, sin que tengamos que escribir la implementacion a mano.
 */
public interface EntidadJpaRepository extends JpaRepository<EntidadEntity, Long> {

    /**
     * Descripcion:
     * Busca una entidad por su nombre.
     *
     * Parametros:
     * nombre - Nombre de la entidad a buscar.
     *
     * Retorno:
     * Optional con la entidad encontrada, o vacio si no existe.
     */
    Optional<EntidadEntity> findByNombre(String nombre);

    /**
     * Descripcion:
     * Verifica si existe una entidad con el nombre indicado.
     *
     * Parametros:
     * nombre - Nombre a verificar.
     *
     * Retorno:
     * true si existe, false si no.
     */
    boolean existsByNombre(String nombre);

    /**
     * Descripcion:
     * Elimina una entidad por su nombre.
     *
     * Parametros:
     * nombre - Nombre de la entidad a eliminar.
     */
    void deleteByNombre(String nombre);

    /**
     * Descripcion:
     * Lista las entidades cuya vida actual supera el valor indicado, ordenadas por
     * nombre. Es una consulta propia del dominio para ver, por ejemplo, quienes
     * siguen vivos despues de una simulacion.
     *
     * Parametros:
     * vida - Vida minima que debe superar la entidad.
     *
     * Retorno:
     * La lista de entidades que cumplen la condicion.
     */
    List<EntidadEntity> findByVidaGreaterThanOrderByNombreAsc(int vida);
}
