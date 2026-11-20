package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.edu.uc.lp3.clbasualdo.minecraft.constants.ApiPaths;
import py.edu.uc.lp3.clbasualdo.minecraft.dto.ErrorRespuesta;
import py.edu.uc.lp3.clbasualdo.minecraft.dto.EstadoCreeperRequest;
import py.edu.uc.lp3.clbasualdo.minecraft.dto.ValidacionReglaResponse;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.service.reglas.ReglasExplosionService;

/**
 * Descripción:
 * Publica las dos reglas de la explosión como rutas que no modifican la base.
 *
 * Responsabilidad:
 * Permitir que el servicio de un compañero consulte mis reglas desde el OpenAPI: manda el
 * estado de un Creeper y recibe 200 si la regla acepta o el error de la regla si rechaza.
 * La lógica está en ReglasExplosionService, la misma que usa la simulación.
 */
@RestController
@Tag(name = "Reglas", description = "Las dos reglas de la explosión, para consultarlas sin modificar H2.")
public class ReglasController {

    /** Servicio con las dos reglas. */
    private final ReglasExplosionService reglasExplosionService;

    /**
     * Descripción:
     * Recibe el servicio de reglas por constructor.
     *
     * Parámetros:
     * reglasExplosionService - Las dos reglas de la explosión.
     */
    public ReglasController(ReglasExplosionService reglasExplosionService) {
        this.reglasExplosionService = reglasExplosionService;
    }

    /**
     * Descripción:
     * Regla 1: un Creeper solo puede detonar si está vivo (vida mayor a 0).
     *
     * Parámetros:
     * estado - Nombre y vida del Creeper.
     *
     * Retorno:
     * 200 si está vivo. Si no, 422 CreeperMuertoException (lo arma el handler).
     */
    @PostMapping(ApiPaths.REGLA_CREEPER_VIVO)
    @Operation(summary = "Regla 1: un Creeper solo detona si está vivo",
            description = "Recibe la vida de un Creeper y responde 200 si es mayor a 0. Con vida 0 o "
                    + "menos rechaza con 422 CreeperMuertoException. No guarda nada.")
    @ApiResponse(responseCode = "200", description = "El Creeper está vivo: puede detonar.")
    @ApiResponse(responseCode = "400", description = "Falta \"vida\" o el JSON no se puede leer.",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "422", description = "El Creeper está muerto: no puede detonar.",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = @ExampleObject(value = """
                            {"timestamp": "2026-11-19T21:10:02.512", "status": 422, "error": "Unprocessable Entity",
                             "tipo": "CreeperMuertoException", "mensaje": "Creeper del pozo no puede detonar: está muerto."}
                            """)))
    public ValidacionReglaResponse creeperVivo(@RequestBody EstadoCreeperRequest estado) {
        if (estado == null || estado.vida() == null) {
            throw new DatosInvalidosException("Falta el campo obligatorio \"vida\".");
        }
        String nombre = nombreDe(estado);
        reglasExplosionService.validarCreeperVivo(nombre, estado.vida());
        return new ValidacionReglaResponse("creeper-vivo", true, nombre + " está vivo: puede detonar.");
    }

    /**
     * Descripción:
     * Regla 2: un Creeper solo puede detonar una vez.
     *
     * Parámetros:
     * estado - Nombre del Creeper y si ya detonó.
     *
     * Retorno:
     * 200 si no detonó. Si ya detonó, 409 CreeperYaDetonadoException (lo arma el handler).
     */
    @PostMapping(ApiPaths.REGLA_DETONACION_UNICA)
    @Operation(summary = "Regla 2: un Creeper solo detona una vez",
            description = "Recibe si el Creeper ya detonó y responde 200 si todavía no. Si ya detonó "
                    + "rechaza con 409 CreeperYaDetonadoException. No guarda nada.")
    @ApiResponse(responseCode = "200", description = "El Creeper todavía no detonó: puede detonar.")
    @ApiResponse(responseCode = "400", description = "Falta \"detonado\" o el JSON no se puede leer.",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "409", description = "El Creeper ya detonó: no puede volver a explotar.",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = @ExampleObject(value = """
                            {"timestamp": "2026-11-19T21:10:02.512", "status": 409, "error": "Conflict",
                             "tipo": "CreeperYaDetonadoException", "mensaje": "Creeper del pozo ya detonó y no puede volver a explotar."}
                            """)))
    public ValidacionReglaResponse detonacionUnica(@RequestBody EstadoCreeperRequest estado) {
        if (estado == null || estado.detonado() == null) {
            throw new DatosInvalidosException("Falta el campo obligatorio \"detonado\".");
        }
        String nombre = nombreDe(estado);
        reglasExplosionService.validarDetonacionUnica(nombre, estado.detonado());
        return new ValidacionReglaResponse("detonacion-unica", true, nombre + " todavía no detonó: puede detonar.");
    }

    /**
     * Descripción:
     * Devuelve el nombre pedido o "Creeper" si no vino.
     *
     * Parámetros:
     * estado - Pedido recibido.
     *
     * Retorno:
     * El nombre para los mensajes.
     */
    private String nombreDe(EstadoCreeperRequest estado) {
        return estado.nombre() == null || estado.nombre().isBlank() ? "Creeper" : estado.nombre();
    }
}
