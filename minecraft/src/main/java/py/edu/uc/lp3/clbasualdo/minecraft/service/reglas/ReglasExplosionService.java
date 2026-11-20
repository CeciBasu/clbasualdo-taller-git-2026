package py.edu.uc.lp3.clbasualdo.minecraft.service.reglas;

import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperMuertoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperYaDetonadoException;

/**
 * Descripción:
 * Las dos reglas de la explosión de un Creeper, en un solo lugar.
 *
 * Responsabilidad:
 * Decidir si un Creeper puede detonar. La usa la simulación (antes de tocar H2) y la usan
 * los endpoints de /reglas, que publican las dos reglas para que un compañero las consulte
 * sin modificar mi base.
 */
public interface ReglasExplosionService {

    /**
     * Descripción:
     * Regla 1: un Creeper solo puede detonar si está vivo.
     *
     * Parámetros:
     * nombre - Nombre del Creeper, para el mensaje.
     * vida - Vida actual del Creeper.
     *
     * Errores:
     * CreeperMuertoException - Si la vida es 0 o menos.
     */
    void validarCreeperVivo(String nombre, int vida) throws CreeperMuertoException;

    /**
     * Descripción:
     * Regla 2: un Creeper solo puede detonar una vez.
     *
     * Parámetros:
     * nombre - Nombre del Creeper, para el mensaje.
     * detonado - Si ya detonó antes.
     *
     * Errores:
     * CreeperYaDetonadoException - Si ya detonó.
     */
    void validarDetonacionUnica(String nombre, boolean detonado) throws CreeperYaDetonadoException;
}
