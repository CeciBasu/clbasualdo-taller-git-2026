package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import org.springframework.http.MediaType;
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
import py.edu.uc.lp3.clbasualdo.minecraft.dto.ValidacionExplosionRequest;
import py.edu.uc.lp3.clbasualdo.minecraft.dto.ValidacionExplosionResponse;
import py.edu.uc.lp3.clbasualdo.minecraft.service.reglas.ReglasCreeperService;

@RestController
@Tag(name = "Validación de explosión", description = "Las dos reglas juntas, sin leer ni escribir H2.")
public class ValidacionExplosionController {

    private final ReglasCreeperService reglasCreeperService;

    public ValidacionExplosionController(ReglasCreeperService reglasCreeperService) {
        this.reglasCreeperService = reglasCreeperService;
    }

    @PostMapping(ApiPaths.EXPLOSIONES_VALIDAR)
    @Operation(summary = "Valida las dos reglas de la explosión sin persistir nada",
            description = """
                    Evalúa con los datos del cuerpo que el Creeper esté vivo y que todavía no haya detonado. \
                    No lee ni escribe H2: 200 si acepta, 422 si está muerto y 409 si ya detonó. \
                    Los campos que no sean "saludCreeper" ni "yaDetonado" se ignoran, así que sirve \
                    para el formato propio de cada compañera.""")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Estado del Creeper. Ambos campos son opcionales; se ignoran los demás.",
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ValidacionExplosionRequest.class),
                    examples = @ExampleObject(name = "Cuerpo con campos extra de otra API", value = """
                            {
                              "saludCreeper": 20,
                              "yaDetonado": false,
                              "tiempoExplosion": 3,
                              "tiempoTranscurrido": 3,
                              "radioExplosion": 5,
                              "creeper": {"x": 0, "y": 64, "z": 0},
                              "objetivos": [{"tipo": "jugador", "salud": 20,
                                             "posicion": {"x": 2, "y": 64, "z": 0}}]
                            }
                            """)))
    @ApiResponse(responseCode = "200", description = "El Creeper está vivo y todavía no detonó.",
            content = @Content(schema = @Schema(implementation = ValidacionExplosionResponse.class),
                    examples = @ExampleObject(value = """
                            {"aceptada": true, "mensaje": "El Creeper está vivo y todavía no detonó."}
                            """)))
    @ApiResponse(responseCode = "409", description = "Regla 2: ya detonó (CreeperYaDetonadoException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "422", description = "Regla 1: está muerto (CreeperMuertoException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    public ValidacionExplosionResponse validar(@RequestBody ValidacionExplosionRequest pedido) {
        return reglasCreeperService.validar(pedido);
    }
}
