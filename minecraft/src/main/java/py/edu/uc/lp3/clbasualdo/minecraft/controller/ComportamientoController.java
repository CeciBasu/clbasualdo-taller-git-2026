package py.edu.uc.lp3.clbasualdo.minecraft.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.clbasualdo.minecraft.Entidad;
import py.edu.uc.lp3.clbasualdo.minecraft.Zombie;
import py.edu.uc.lp3.clbasualdo.minecraft.Esqueleto;
import py.edu.uc.lp3.clbasualdo.minecraft.Creeper;
import py.edu.uc.lp3.clbasualdo.minecraft.Aldeano;
import py.edu.uc.lp3.clbasualdo.minecraft.Animal;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Descripcion:
 * Controlador que muestra como reacciona cada tipo de entidad del juego.
 *
 * Responsabilidad:
 * Se encarga de crear una entidad de cada tipo y devolver sus datos y su reaccion por la API.
 */
@RestController
public class ComportamientoController {

    /**
     * Descripcion:
     * Responde a la ruta "/comportamiento". Crea un Zombie, un Esqueleto, un Creeper, un Aldeano y una Vaca,
     * y por cada uno arma un mapa con su tipo, su reaccion, su vida y su posicion.
     * Todas las entidades se tratan como Entidad, y cada una responde con su propia reaccion.
     *
     * Retorno:
     * Una lista con los datos de cada entidad.
     */
    @GetMapping("/comportamiento")
    public List<Map<String, Object>> comportamiento() {
        List<Entidad> entidades = List.of(
                new Zombie(), new Esqueleto(), new Creeper(), new Aldeano(), new Animal("Vaca", "Vaca")
        );

        return entidades.stream()
                .map(e -> Map.<String, Object>of(
                        "tipo", e.getClass().getSimpleName(),
                        "reaccion", e.reaccionar(),
                        "vida", e.getVida(),
                        "posicion", Map.of("x", e.getX(), "y", e.getY(), "z", e.getZ())
                ))
                .collect(Collectors.toList());
    }
}
