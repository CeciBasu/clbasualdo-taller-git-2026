package py.edu.uc.lp3.clbasualdo.minecraft.service;

import java.util.List;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.Entidad;

/**
 * Descripción:
 * Contrato de las operaciones de negocio sobre el comportamiento de las entidades.
 *
 * Responsabilidad:
 * Armar la lista de una entidad de cada tipo para mostrar cómo reaccionan.
 */
public interface ComportamientoService {

    /**
     * Descripción:
     * Devuelve una entidad de cada tipo, tratadas todas como Entidad.
     *
     * Retorno:
     * La lista con una entidad de cada tipo.
     */
    List<Entidad> entidadesDeMuestra();
}
