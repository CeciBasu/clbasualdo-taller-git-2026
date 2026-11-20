package py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import py.edu.uc.lp3.clbasualdo.minecraft.entity.EntidadEntity;

/**
 * Descripción:
 * Repositorio Spring Data JPA para la familia de entidades persistidas.
 *
 * Responsabilidad:
 * Proveer operaciones CRUD sobre la tabla "entidades" y consultas por nombre o
 * por vida, sin que tengamos que escribir la implementación a mano.
 */
public interface EntidadJpaRepository extends JpaRepository<EntidadEntity, Long> {

    /**
     * Descripción:
     * Busca una entidad por su nombre.
     *
     * Parámetros:
     * nombre - Nombre de la entidad a buscar.
     *
     * Retorno:
     * Optional con la entidad encontrada, o vacío si no existe.
     */
    Optional<EntidadEntity> findByNombre(String nombre);

    /**
     * Descripción:
     * Verifica si existe una entidad con el nombre indicado.
     *
     * Parámetros:
     * nombre - Nombre a verificar.
     *
     * Retorno:
     * true si existe, false si no.
     */
    boolean existsByNombre(String nombre);

    /**
     * Descripción:
     * Elimina una entidad por su nombre.
     *
     * Parámetros:
     * nombre - Nombre de la entidad a eliminar.
     */
    void deleteByNombre(String nombre);

    /**
     * Descripción:
     * Lista las entidades cuya vida actual supera el valor indicado, ordenadas por
     * nombre. Es una consulta propia del dominio para ver, por ejemplo, quiénes
     * siguen vivos después de una simulación.
     *
     * Parámetros:
     * vida - Vida mínima que debe superar la entidad.
     *
     * Retorno:
     * La lista de entidades que cumplen la condición.
     */
    List<EntidadEntity> findByVidaGreaterThanOrderByNombreAsc(int vida);
}
