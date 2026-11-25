package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.edu.uc.lp3.clbasualdo.minecraft.dto.ErrorRespuesta;
import py.edu.uc.lp3.clbasualdo.minecraft.constants.ApiPaths;
import py.edu.uc.lp3.clbasualdo.minecraft.dto.EntidadRequest;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.EntidadEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.service.entidad.EntidadCrudService;

/**
 * Descripción:
 * Controlador REST del CRUD de la familia de entidades persistidas en H2.
 *
 * Responsabilidad:
 * Recibir las peticiones HTTP, delegar en el servicio de negocios y devolver las
 * respuestas. Las excepciones de dominio las traduce el handler, no este
 * controller.
 */
@RestController
@Tag(name = "Entidades", description = "CRUD de la familia de entidades guardada en H2 (Creeper, Zombie, Jugador).")
@RequestMapping(ApiPaths.ENTIDADES)
public class EntidadController {

    /** Servicio de negocios para el CRUD de entidades. */
    private final EntidadCrudService entidadCrudService;

    /**
     * Descripción:
     * Crea el controlador con el servicio de entidades.
     *
     * Parámetros:
     * entidadCrudService - Servicio que contiene las reglas de negocio.
     */
    public EntidadController(EntidadCrudService entidadCrudService) {
        this.entidadCrudService = entidadCrudService;
    }

    /**
     * Descripción:
     * Crea una nueva entidad y devuelve HTTP 201 con el recurso y su id.
     *
     * Parámetros:
     * entidad - Datos de la entidad a crear, con "tipo" para elegir el subtipo.
     *
     * Retorno:
     * Respuesta con la entidad creada.
     */
    @Operation(summary = "Crea una entidad", description = "El JSON trae tipo (Creeper, Zombie o Jugador). Rechaza con 400 un Creeper detonado y con vida, un nombre repetido o un dato faltante.")
    @ApiResponse(responseCode = "201", description = "Entidad creada, con su id.")
    @ApiResponse(responseCode = "400", description = "Dato faltante, inválido o combinación imposible (DatosInvalidosException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @PostMapping
    public ResponseEntity<EntidadEntity> crear(@RequestBody EntidadRequest entidad) {
        EntidadEntity creada = entidadCrudService.crear(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    /**
     * Descripción:
     * Lista todas las entidades persistidas.
     *
     * Retorno:
     * Lista con todas las entidades y su "tipo".
     */
    @Operation(summary = "Lista las entidades guardadas", description = "Devuelve todas las filas de la tabla entidades con su tipo.")
    @ApiResponse(responseCode = "200", description = "Lista de entidades.")
    @GetMapping
    public List<EntidadEntity> listar() {
        return entidadCrudService.listar();
    }

    /**
     * Descripción:
     * Obtiene una entidad por su id.
     *
     * Parámetros:
     * id - Id de la entidad a buscar.
     *
     * Retorno:
     * La entidad encontrada.
     */
    @Operation(summary = "Busca una entidad por id", description = "")
    @ApiResponse(responseCode = "200", description = "La entidad.")
    @ApiResponse(responseCode = "404", description = "No existe (RecursoNoEncontradoException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "400", description = "El id no es un número (DatosInvalidosException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @GetMapping("/{id}")
    public EntidadEntity obtenerPorId(@PathVariable Long id) {
        return entidadCrudService.obtenerPorId(id);
    }

    /**
     * Descripción:
     * Obtiene una entidad por su nombre.
     *
     * Parámetros:
     * nombre - Nombre de la entidad a buscar.
     *
     * Retorno:
     * La entidad encontrada.
     */
    @Operation(summary = "Busca una entidad por nombre", description = "")
    @ApiResponse(responseCode = "200", description = "La entidad.")
    @ApiResponse(responseCode = "404", description = "No existe (RecursoNoEncontradoException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @GetMapping("/nombre/{nombre}")
    public EntidadEntity obtenerPorNombre(@PathVariable String nombre) {
        return entidadCrudService.obtenerPorNombre(nombre);
    }

    /**
     * Descripción:
     * Actualiza una entidad por su id.
     *
     * Parámetros:
     * id - Id de la entidad a actualizar.
     * entidad - Nuevos datos de la entidad.
     *
     * Retorno:
     * La entidad actualizada.
     */
    @Operation(summary = "Reemplaza una entidad por id", description = "No permite cambiar el tipo, detonar un Creeper (eso lo hace la simulación), deshacer una detonación ni revivir a un Creeper o Zombie muerto.")
    @ApiResponse(responseCode = "200", description = "La entidad actualizada.")
    @ApiResponse(responseCode = "400", description = "Dato faltante, inválido o combinación imposible (DatosInvalidosException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "404", description = "No existe (RecursoNoEncontradoException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @PutMapping("/{id}")
    public EntidadEntity actualizar(@PathVariable Long id, @RequestBody EntidadRequest entidad) {
        return entidadCrudService.actualizar(id, entidad);
    }

    /**
     * Descripción:
     * Actualiza una entidad por su nombre.
     *
     * Parámetros:
     * nombre - Nombre de la entidad a actualizar.
     * entidad - Nuevos datos de la entidad.
     *
     * Retorno:
     * La entidad actualizada.
     */
    @Operation(summary = "Reemplaza una entidad por nombre", description = "Mismas reglas que el PUT por id.")
    @ApiResponse(responseCode = "200", description = "La entidad actualizada.")
    @ApiResponse(responseCode = "400", description = "Dato faltante, inválido o combinación imposible (DatosInvalidosException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @ApiResponse(responseCode = "404", description = "No existe (RecursoNoEncontradoException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @PutMapping("/nombre/{nombre}")
    public EntidadEntity actualizarPorNombre(@PathVariable String nombre, @RequestBody EntidadRequest entidad) {
        return entidadCrudService.actualizarPorNombre(nombre, entidad);
    }

    /**
     * Descripción:
     * Elimina una entidad por su id y responde HTTP 204.
     *
     * Parámetros:
     * id - Id de la entidad a eliminar.
     *
     * Retorno:
     * Respuesta sin contenido.
     */
    @Operation(summary = "Elimina una entidad por id", description = "")
    @ApiResponse(responseCode = "204", description = "Eliminada.")
    @ApiResponse(responseCode = "404", description = "No existe (RecursoNoEncontradoException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        entidadCrudService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Descripción:
     * Elimina una entidad por su nombre y responde HTTP 204.
     *
     * Parámetros:
     * nombre - Nombre de la entidad a eliminar.
     *
     * Retorno:
     * Respuesta sin contenido.
     */
    @Operation(summary = "Elimina una entidad por nombre", description = "")
    @ApiResponse(responseCode = "204", description = "Eliminada.")
    @ApiResponse(responseCode = "404", description = "No existe (RecursoNoEncontradoException).",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class)))
    @DeleteMapping("/nombre/{nombre}")
    public ResponseEntity<Void> eliminarPorNombre(@PathVariable String nombre) {
        entidadCrudService.eliminarPorNombre(nombre);
        return ResponseEntity.noContent().build();
    }
}
