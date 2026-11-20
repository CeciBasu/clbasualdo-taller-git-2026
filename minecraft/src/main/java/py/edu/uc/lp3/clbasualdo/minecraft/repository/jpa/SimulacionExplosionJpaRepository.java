package py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import py.edu.uc.lp3.clbasualdo.minecraft.entity.SimulacionExplosionEntity;

/**
 * Descripción:
 * Repositorio Spring Data JPA para las simulaciones de explosión persistidas.
 *
 * Responsabilidad:
 * Guardar y recuperar los resultados de las simulaciones, y ofrecer la última
 * simulación registrada para el GET de consulta rápida.
 */
public interface SimulacionExplosionJpaRepository extends JpaRepository<SimulacionExplosionEntity, Long> {

    /**
     * Descripción:
     * Devuelve la última simulación guardada, ordenada por id descendente.
     *
     * Retorno:
     * Optional con la última simulación, o vacío si todavía no hay ninguna.
     */
    Optional<SimulacionExplosionEntity> findFirstByOrderByIdDesc();
}
