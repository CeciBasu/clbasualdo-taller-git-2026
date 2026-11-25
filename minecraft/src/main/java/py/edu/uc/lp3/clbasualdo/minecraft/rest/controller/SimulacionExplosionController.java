package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.edu.uc.lp3.clbasualdo.minecraft.dto.ErrorRespuesta;
import py.edu.uc.lp3.clbasualdo.minecraft.constants.ApiPaths;
import py.edu.uc.lp3.clbasualdo.minecraft.dto.SimulacionExplosionRequest;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionResultado;
import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionService;

/**
 * Descripción:
 * Controlador REST que ejecuta y consulta la simulación de una explosión.
 *
 * Responsabilidad:
 * Recibir los parámetros de la simulación y delegar todo el comportamiento en
 * el servicio, para que las reglas del dominio no vivan en el controller.
 */
@RestController
@Tag(name = "Simulación", description = "Explosión de un Creeper guardado sobre las entidades guardadas.")
public class SimulacionExplosionController {

    /** Servicio que ejecuta y consulta la simulación. */
    private final SimulacionExplosionService simulacionExplosionService;

    /**
     * Descripción:
     * Crea el controlador con el servicio que va a usar.
     *
     * Parámetros:
     * simulacionExplosionService - El servicio de simulación de explosiones.
     */
    public SimulacionExplosionController(SimulacionExplosionService simulacionExplosionService) {
        this.simulacionExplosionService = simulacionExplosionService;
    }

    /**
     * Descripción:
     * Detona un Creeper guardado sobre las entidades guardadas y persiste el resultado.
     * Si el Creeper está muerto o ya detonó, la excepción del service viaja hasta el
     * handler, que responde el error HTTP correspondiente.
     *
     * Parámetros:
     * pedido - JSON con el creeperId del Creeper guardado.
     *
     * Retorno:
     * El resultado completo de la simulación, con su id persistido.
     */
    @Operation(summary = "Detona un Creeper guardado", description = "Carga el Creeper por creeperId, aplica las dos reglas, quita 25 de vida a cada entidad viva a 3 bloques o menos y guarda el resultado. El Creeper queda detonado y con vida 0.")
    @ApiResponse(responseCode = "201", description = "Simulación guardada.")
    @ApiResponse(responseCode = "400", description = "Falta creeperId o la entidad no es un Creeper (DatosInvalidosException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "404", description = "No existe una entidad con ese id (RecursoNoEncontradoException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "409", description = "Regla 2: el Creeper ya detonó (CreeperYaDetonadoException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "422", description = "Regla 1: el Creeper está muerto (CreeperMuertoException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @PostMapping(ApiPaths.SIMULACION_EXPLOSION)
    public ResponseEntity<SimulacionExplosionResultado> simular(
            @RequestBody SimulacionExplosionRequest pedido) {
        SimulacionExplosionResultado resultado = simulacionExplosionService.simular(pedido.creeperId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    /**
     * Descripción:
     * Devuelve la última simulación guardada.
     *
     * Retorno:
     * El último resultado persistido.
     */
    @Operation(summary = "Última simulación guardada", description = "")
    @ApiResponse(responseCode = "200", description = "La simulación más reciente.")
    @ApiResponse(responseCode = "404", description = "Todavía no hay simulaciones (RecursoNoEncontradoException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @GetMapping(ApiPaths.SIMULACION_EXPLOSION)
    public SimulacionExplosionResultado ultima() {
        return simulacionExplosionService.ultima();
    }

    /**
     * Descripción:
     * Devuelve una simulación persistida por su id.
     *
     * Parámetros:
     * id - Id de la simulación.
     *
     * Retorno:
     * El resultado guardado.
     */
    @Operation(summary = "Simulación guardada por id", description = "")
    @ApiResponse(responseCode = "200", description = "La simulación.")
    @ApiResponse(responseCode = "404", description = "No existe (RecursoNoEncontradoException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @GetMapping(ApiPaths.SIMULACION_EXPLOSION_ID)
    public SimulacionExplosionResultado obtener(@PathVariable Long id) {
        return simulacionExplosionService.obtener(id);
    }

    /**
     * Descripción:
     * Lista todas las simulaciones persistidas.
     *
     * Retorno:
     * La lista de resultados guardados.
     */
    @Operation(summary = "Lista las simulaciones guardadas", description = "")
    @ApiResponse(responseCode = "200", description = "Lista de simulaciones.")
    @GetMapping(ApiPaths.SIMULACION_EXPLOSIONES)
    public List<SimulacionExplosionResultado> listar() {
        return simulacionExplosionService.listar();
    }
}
