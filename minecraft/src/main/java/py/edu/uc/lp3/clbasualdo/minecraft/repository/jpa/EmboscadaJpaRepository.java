package py.edu.uc.lp3.clbasualdo.minecraft.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import py.edu.uc.lp3.clbasualdo.minecraft.entity.EmboscadaEntity;

/**
 * Descripción:
 * Repositorio Spring Data de las jugadas del escenario «emboscada».
 *
 * Responsabilidad:
 * Guardar y leer las filas de la tabla "emboscadas".
 */
public interface EmboscadaJpaRepository extends JpaRepository<EmboscadaEntity, Long> {
}
