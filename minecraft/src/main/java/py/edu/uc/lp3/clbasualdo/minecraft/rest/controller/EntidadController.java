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
import py.edu.uc.lp3.clbasualdo.minecraft.entity.EntidadEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.service.entidad.EntidadCrudService;

/**
 * Descripcion:
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
     * Descripcion:
     * Crea el controlador con el servicio de entidades.
     *
     * Parametros:
     * entidadCrudService - Servicio que contiene las reglas de negocio.
     */
    public EntidadController(EntidadCrudService entidadCrudService) {
        this.entidadCrudService = entidadCrudService;
    }

    /**
     * Descripcion:
     * Crea una nueva entidad y devuelve HTTP 201 con el recurso y su id.
     *
     * Parametros:
     * entidad - Datos de la entidad a crear, con "tipo" para elegir el subtipo.
     *
     * Retorno:
     * Respuesta con la entidad creada.
     */
    @PostMapping
    public ResponseEntity<EntidadEntity> crear(@RequestBody EntidadEntity entidad) {
        EntidadEntity creada = entidadCrudService.crear(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    /**
     * Descripcion:
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
     * Descripcion:
     * Obtiene una entidad por su id.
     *
     * Parametros:
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
     * Descripcion:
     * Obtiene una entidad por su nombre.
     *
     * Parametros:
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
     * Descripcion:
     * Actualiza una entidad por su id.
     *
     * Parametros:
     * id - Id de la entidad a actualizar.
     * entidad - Nuevos datos de la entidad.
     *
     * Retorno:
     * La entidad actualizada.
     */
    @PutMapping("/{id}")
    public EntidadEntity actualizar(@PathVariable Long id, @RequestBody EntidadEntity entidad) {
        return entidadCrudService.actualizar(id, entidad);
    }

    /**
     * Descripcion:
     * Actualiza una entidad por su nombre.
     *
     * Parametros:
     * nombre - Nombre de la entidad a actualizar.
     * entidad - Nuevos datos de la entidad.
     *
     * Retorno:
     * La entidad actualizada.
     */
    @PutMapping("/nombre/{nombre}")
    public EntidadEntity actualizarPorNombre(@PathVariable String nombre, @RequestBody EntidadEntity entidad) {
        return entidadCrudService.actualizarPorNombre(nombre, entidad);
    }

    /**
     * Descripcion:
     * Elimina una entidad por su id y responde HTTP 204.
     *
     * Parametros:
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
     * Descripcion:
     * Elimina una entidad por su nombre y responde HTTP 204.
     *
     * Parametros:
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
