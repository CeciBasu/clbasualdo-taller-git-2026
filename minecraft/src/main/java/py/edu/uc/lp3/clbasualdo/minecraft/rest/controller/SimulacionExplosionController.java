package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.clbasualdo.minecraft.constants.ApiPaths;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionResultado;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionService;

/**
 * Descripcion:
 * Controlador REST que ejecuta y consulta la simulacion de una explosion.
 *
 * Responsabilidad:
 * Recibir los parametros de la simulacion y delegar todo el comportamiento en
 * el servicio, para que las reglas del dominio no vivan en el controller.
 */
@RestController
public class SimulacionExplosionController {

    /** Servicio que ejecuta y consulta la simulacion. */
    private final SimulacionExplosionService simulacionExplosionService;

    /**
     * Descripcion:
     * Crea el controlador con el servicio que va a usar.
     *
     * Parametros:
     * simulacionExplosionService - El servicio de simulacion de explosiones.
     */
    public SimulacionExplosionController(SimulacionExplosionService simulacionExplosionService) {
        this.simulacionExplosionService = simulacionExplosionService;
    }

    /**
     * Descripcion:
     * Ejecuta y persiste la simulacion entre un Creeper, un Zombie y un Jugador.
     * Si el Creeper esta muerto o se intenta una segunda detonacion, la excepcion
     * de dominio viaja hasta el handler que responde el error HTTP correspondiente.
     *
     * Parametros:
     * vidaCreeper - Vida inicial del Creeper (por defecto 20).
     * repetirDetonacion - Si es true, simula un segundo intento de detonacion.
     *
     * Retorno:
     * El resultado completo de la simulacion, con su id persistido.
     */
    @PostMapping(ApiPaths.SIMULACION_EXPLOSION)
    public ResponseEntity<SimulacionExplosionResultado> simular(
            @RequestParam(defaultValue = "20") int vidaCreeper,
            @RequestParam(defaultValue = "false") boolean repetirDetonacion) {
        SimulacionExplosionResultado resultado = simulacionExplosionService.simular(vidaCreeper, repetirDetonacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    /**
     * Descripcion:
     * Devuelve la ultima simulacion guardada.
     *
     * Retorno:
     * El ultimo resultado persistido.
     */
    @GetMapping(ApiPaths.SIMULACION_EXPLOSION)
    public SimulacionExplosionResultado ultima() {
        return simulacionExplosionService.ultima();
    }

    /**
     * Descripcion:
     * Devuelve una simulacion persistida por su id.
     *
     * Parametros:
     * id - Id de la simulacion.
     *
     * Retorno:
     * El resultado guardado.
     */
    @GetMapping(ApiPaths.SIMULACION_EXPLOSION_ID)
    public SimulacionExplosionResultado obtener(@PathVariable Long id) {
        return simulacionExplosionService.obtener(id);
    }

    /**
     * Descripcion:
     * Lista todas las simulaciones persistidas.
     *
     * Retorno:
     * La lista de resultados guardados.
     */
    @GetMapping(ApiPaths.SIMULACION_EXPLOSIONES)
    public List<SimulacionExplosionResultado> listar() {
        return simulacionExplosionService.listar();
    }
}
