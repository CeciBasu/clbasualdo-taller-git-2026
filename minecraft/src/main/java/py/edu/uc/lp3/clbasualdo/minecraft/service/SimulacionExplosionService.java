package py.edu.uc.lp3.clbasualdo.minecraft.service;

import java.util.List;

import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperMuertoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperYaDetonadoException;

/**
 * Descripcion:
 * Contrato de la simulacion de explosiones del dominio Minecraft.
 *
 * Responsabilidad:
 * Ejecutar la explosion entre un Creeper, un Zombie y un Jugador, persistir el
 * resultado en H2 y permitir consultar despues lo que quedo guardado.
 */
public interface SimulacionExplosionService {

    /**
     * Descripcion:
     * Ejecuta y persiste una simulacion de explosion con un Creeper, un Zombie
     * dentro del radio y un Jugador fuera del radio.
     *
     * Parametros:
     * vidaCreeper - Vida con la que empieza el Creeper. Si es 0 o menos, esta muerto.
     * repetirDetonacion - Si es true, intenta una segunda detonacion sobre el mismo Creeper.
     *
     * Retorno:
     * El resultado persistido de la simulacion.
     *
     * Errores:
     * CreeperMuertoException - Si el Creeper no esta vivo al intentar detonar.
     * CreeperYaDetonadoException - Si se intenta detonar dos veces el mismo Creeper.
     */
    SimulacionExplosionResultado simular(int vidaCreeper, boolean repetirDetonacion)
            throws CreeperMuertoException, CreeperYaDetonadoException;

    /**
     * Descripcion:
     * Busca una simulacion persistida por su id.
     *
     * Parametros:
     * id - Id de la simulacion.
     *
     * Retorno:
     * El resultado guardado.
     */
    SimulacionExplosionResultado obtener(Long id);

    /**
     * Descripcion:
     * Lista todas las simulaciones persistidas.
     *
     * Retorno:
     * La lista de resultados guardados.
     */
    List<SimulacionExplosionResultado> listar();

    /**
     * Descripcion:
     * Devuelve la ultima simulacion persistida.
     *
     * Retorno:
     * El ultimo resultado guardado.
     */
    SimulacionExplosionResultado ultima();
}
