package py.edu.uc.lp3.clbasualdo.minecraft.service;

import java.util.List;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.UICapable;

/**
 * Descripcion:
 * Contrato de las operaciones de negocio sobre los datos de UI de las entidades.
 *
 * Responsabilidad:
 * Armar la lista de entidades que comparten la capacidad UICapable, tratadas
 * todas como UICapable para que el controller no dependa de cada clase.
 */
public interface UiService {

    /**
     * Descripcion:
     * Devuelve las entidades que se pueden dibujar en la UI, de distintas
     * familias, tratadas todas como UICapable.
     *
     * Retorno:
     * La lista de entidades UICapable.
     */
    List<UICapable> entidadesUi();
}
