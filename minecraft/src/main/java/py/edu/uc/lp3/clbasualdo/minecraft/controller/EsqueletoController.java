package py.edu.uc.lp3.clbasualdo.minecraft.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.clbasualdo.minecraft.Esqueleto;

import java.util.Map;

@RestController
public class EsqueletoController {

    @GetMapping("/esqueleto")
    public ResponseEntity<Map<String, Object>> crear(
            @RequestParam(defaultValue = "Esqueleto") String nombre,
            @RequestParam(defaultValue = "20") int vida,
            @RequestParam(defaultValue = "0") double x,
            @RequestParam(defaultValue = "64") double y,
            @RequestParam(defaultValue = "0") double z) {

        try {
            Esqueleto esqueleto = new Esqueleto(nombre, vida);
            esqueleto.teletransportar(x, y, z);

            return ResponseEntity.ok(Map.of(
                    "nombre", esqueleto.getNombre(),
                    "vida", esqueleto.getVida(),
                    "vivo", esqueleto.estaVivo(),
                    "flechas", esqueleto.getFlechas(),
                    "posicion", Map.of("x", esqueleto.getX(), "y", esqueleto.getY(), "z", esqueleto.getZ())
            ));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
