package py.edu.uc.lp3.clbasualdo.minecraft.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.Entidad;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.EntidadRepository;

/**
 * Descripcion:
 * Implementacion del repositorio que guarda las entidades en memoria.
 *
 * Responsabilidad:
 * Mantener la lista de entidades durante la vida de la aplicacion, sin
 * persistencia en disco. Se aisla aca para poder cambiar a una base de datos
 * mas adelante sin tocar los servicios.
 */
@Repository
public class EntidadRepositoryImpl implements EntidadRepository {

    /** Lista de entidades cargadas en memoria. */
    private final List<Entidad> entidades = new ArrayList<>();

    @Override
    public synchronized Entidad guardar(Entidad entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula.");
        }
        buscarPorNombre(entidad.getNombre()).ifPresent(entidades::remove);
        entidades.add(entidad);
        return entidad;
    }

    @Override
    public synchronized List<Entidad> listar() {
        return List.copyOf(entidades);
    }

    @Override
    public synchronized Optional<Entidad> buscarPorNombre(String nombre) {
        return entidades.stream()
                .filter(entidad -> entidad.getNombre().equals(nombre))
                .findFirst();
    }

    @Override
    public synchronized boolean eliminar(String nombre) {
        return entidades.removeIf(entidad -> entidad.getNombre().equals(nombre));
    }
}
