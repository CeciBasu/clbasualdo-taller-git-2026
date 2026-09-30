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

@RestController
public class ComportamientoController {

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
