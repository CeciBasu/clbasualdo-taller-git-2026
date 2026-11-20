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
    @DeleteMapping("/nombre/{nombre}")
    public ResponseEntity<Void> eliminarPorNombre(@PathVariable String nombre) {
        entidadCrudService.eliminarPorNombre(nombre);
        return ResponseEntity.noContent().build();
    }
}
