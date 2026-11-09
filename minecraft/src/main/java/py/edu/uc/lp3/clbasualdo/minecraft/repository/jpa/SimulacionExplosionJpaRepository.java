package py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import py.edu.uc.lp3.clbasualdo.minecraft.entity.SimulacionExplosionEntity;

/**
 * Descripcion:
 * Repositorio Spring Data JPA para las simulaciones de explosion persistidas.
 *
 * Responsabilidad:
 * Guardar y recuperar los resultados de las simulaciones, y ofrecer la ultima
 * simulacion registrada para el GET de consulta rapida.
 */
public interface SimulacionExplosionJpaRepository extends JpaRepository<SimulacionExplosionEntity, Long> {

    /**
     * Descripcion:
     * Devuelve la ultima simulacion guardada, ordenada por id descendente.
     *
     * Retorno:
     * Optional con la ultima simulacion, o vacio si todavia no hay ninguna.
     */
    Optional<SimulacionExplosionEntity> findFirstByOrderByIdDesc();
}
