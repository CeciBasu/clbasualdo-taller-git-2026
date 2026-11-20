package py.edu.uc.lp3.clbasualdo.minecraft.service;

import java.util.List;

import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperMuertoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperYaDetonadoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RecursoNoEncontradoException;

/**
 * Descripción:
 * Contrato de la simulación de explosiones.
 *
 * Responsabilidad:
 * Detonar un Creeper guardado en H2 sobre las entidades guardadas, aplicando las dos reglas
 * (solo detona si está vivo y solo una vez), persistir el resultado y permitir consultarlo.
 */
public interface SimulacionExplosionService {

    /**
     * Descripción:
     * Carga el Creeper guardado, aplica las dos reglas, lo detona sobre las entidades vivas
     * guardadas y persiste el estado final de todas junto con la foto de la explosión.
     *
     * Parámetros:
     * creeperId - Id del Creeper en la tabla "entidades".
     *
     * Retorno:
     * El resultado persistido de la simulación.
     *
     * Errores:
     * DatosInvalidosException - Si falta el id o la entidad no es un Creeper.
     * RecursoNoEncontradoException - Si no existe una entidad con ese id.
     * CreeperYaDetonadoException - Si el Creeper ya detonó antes.
     * CreeperMuertoException - Si el Creeper no tiene vida.
     */
    SimulacionExplosionResultado simular(Long creeperId)
            throws DatosInvalidosException, RecursoNoEncontradoException,
            CreeperMuertoException, CreeperYaDetonadoException;

    /**
     * Descripción:
     * Devuelve una simulación guardada.
     *
     * Parámetros:
     * id - Id de la simulación.
     *
     * Retorno:
     * La simulación leída de H2.
     *
     * Errores:
     * RecursoNoEncontradoException - Si no existe.
     */
    SimulacionExplosionResultado obtener(Long id) throws RecursoNoEncontradoException;

    /**
     * Descripción:
     * Lista todas las simulaciones guardadas.
     *
     * Retorno:
     * Las simulaciones, de la más vieja a la más nueva.
     */
    List<SimulacionExplosionResultado> listar();

    /**
     * Descripción:
     * Devuelve la última simulación guardada.
     *
     * Retorno:
     * La simulación más reciente.
     *
     * Errores:
     * RecursoNoEncontradoException - Si todavía no hay ninguna.
     */
    SimulacionExplosionResultado ultima() throws RecursoNoEncontradoException;
}
