package py.edu.uc.lp3.clbasualdo.minecraft.service.escenario;

import java.util.List;

import py.edu.uc.lp3.clbasualdo.minecraft.dto.EmboscadaRequest;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CompaneroNoDisponibleException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperMuertoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperYaDetonadoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RechazoDeCompaneroException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RecursoNoEncontradoException;

/**
 * Descripción:
 * Escenario propio (A1): la emboscada del Creeper.
 *
 * Responsabilidad:
 * Un Creeper guardado se acerca a un Jugador guardado y detona. La jugada es legal solo si
 * mis dos reglas, la regla de A2 y la regla de A3 la aceptan. Si todas aceptan, se ejecuta la
 * explosión y se guarda la jugada; si una rechaza, H2 queda igual.
 */
public interface EmboscadaService {

    /**
     * Descripción:
     * Valida la emboscada con mis reglas, con A2 y con A3 y, si todas aceptan, la guarda.
     *
     * Parámetros:
     * pedido - Ids del Creeper y del Jugador y tiempo transcurrido.
     *
     * Retorno:
     * La jugada guardada.
     *
     * Errores:
     * DatosInvalidosException - Falta un id o el tiempo es negativo, o la entidad no es del tipo pedido.
     * RecursoNoEncontradoException - Un id no existe.
     * CreeperYaDetonadoException - Mi regla 2 rechaza.
     * CreeperMuertoException - Mi regla 1 rechaza.
     * RechazoDeCompaneroException - La regla de A2 o de A3 rechaza.
     * CompaneroNoDisponibleException - La API de A2 o de A3 no responde.
     */
    EmboscadaResultado emboscar(EmboscadaRequest pedido)
            throws DatosInvalidosException, RecursoNoEncontradoException, CreeperYaDetonadoException,
            CreeperMuertoException, RechazoDeCompaneroException, CompaneroNoDisponibleException;

    /**
     * Descripción:
     * Devuelve una jugada guardada.
     *
     * Parámetros:
     * id - Id de la jugada.
     *
     * Retorno:
     * La jugada.
     *
     * Errores:
     * RecursoNoEncontradoException - Si no existe.
     */
    EmboscadaResultado obtener(Long id) throws RecursoNoEncontradoException;

    /**
     * Descripción:
     * Lista las jugadas guardadas.
     *
     * Retorno:
     * Todas las emboscadas guardadas.
     */
    List<EmboscadaResultado> listar();
}
