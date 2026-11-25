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
import py.edu.uc.lp3.clbasualdo.minecraft.constants.ApiPaths;
import py.edu.uc.lp3.clbasualdo.minecraft.dto.EmboscadaRequest;
import py.edu.uc.lp3.clbasualdo.minecraft.dto.ErrorRespuesta;
import py.edu.uc.lp3.clbasualdo.minecraft.service.escenario.EmboscadaResultado;
import py.edu.uc.lp3.clbasualdo.minecraft.service.escenario.EmboscadaService;

/**
 * Descripción:
 * Escenario propio de POO-09: la emboscada del Creeper.
 *
 * Responsabilidad:
 * Recibir el pedido del cliente (Insomnia) y delegar en EmboscadaService, que consulta a A2
 * y a A3. El cliente no conoce a los compañeros: solo habla con este servicio.
 */
@RestController
@Tag(name = "Escenario", description = "Emboscada del Creeper: necesita la regla de A2 y la de A3 para guardarse.")
public class EmboscadaController {

    /** Servicio del escenario. */
    private final EmboscadaService emboscadaService;

    /**
     * Descripción:
     * Recibe el servicio por constructor.
     *
     * Parámetros:
     * emboscadaService - Servicio del escenario.
     */
    public EmboscadaController(EmboscadaService emboscadaService) {
        this.emboscadaService = emboscadaService;
    }

    /**
     * Descripción:
     * Ejecuta la emboscada: si mis reglas, A2 y A3 aceptan, detona el Creeper junto al Jugador
     * y guarda la jugada.
     *
     * Parámetros:
     * pedido - Ids del Creeper y del Jugador y tiempo transcurrido.
     *
     * Retorno:
     * 201 con la jugada guardada.
     */
    @PostMapping(ApiPaths.ESCENARIO_EMBOSCADA)
    @Operation(summary = "Emboscada del Creeper",
            description = "Un Creeper guardado se acerca a un Jugador guardado y detona. Orden: mis dos reglas "
                    + "(sin detonar, vivo), después A2 (mecha corta: un jugador vivo a 3 bloques o menos) y "
                    + "después A3. Solo si todas aceptan se ejecuta la explosión y se guarda la jugada; si una "
                    + "rechaza, H2 queda igual y la respuesta dice de quién fue el rechazo.")
    @ApiResponse(responseCode = "201", description = "A2 y A3 aceptaron: la jugada y la explosión quedaron guardadas.")
    @ApiResponse(responseCode = "400", description = "Falta un id, el tiempo es negativo o la entidad no es del tipo pedido.",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "404", description = "Un id no existe.",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "409", description = "Mi regla 2: el Creeper ya detonó.",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "422", description = "Mi regla 1 (Creeper muerto) o un rechazo de A2 o de A3 "
            + "(RechazoDeCompaneroException, con rechazadoPor, companero y la respuesta original).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "503", description = "La API de A2 o de A3 no respondió (CompaneroNoDisponibleException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    public ResponseEntity<EmboscadaResultado> emboscar(@RequestBody EmboscadaRequest pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emboscadaService.emboscar(pedido));
    }

    /**
     * Descripción:
     * Lista las emboscadas guardadas.
     *
     * Retorno:
     * Todas las jugadas.
     */
    @GetMapping(ApiPaths.ESCENARIO_EMBOSCADAS)
    @Operation(summary = "Lista las emboscadas guardadas")
    public List<EmboscadaResultado> listar() {
        return emboscadaService.listar();
    }

    /**
     * Descripción:
     * Devuelve una emboscada guardada.
     *
     * Parámetros:
     * id - Id de la jugada.
     *
     * Retorno:
     * La jugada con la explosión leída de H2.
     */
    @GetMapping(ApiPaths.ESCENARIO_EMBOSCADA_ID)
    @Operation(summary = "Emboscada guardada por id")
    @ApiResponse(responseCode = "200", description = "La jugada.")
    @ApiResponse(responseCode = "404", description = "No existe.",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    public EmboscadaResultado obtener(@PathVariable Long id) {
        return emboscadaService.obtener(id);
    }
}
