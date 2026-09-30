package py.edu.uc.lp3.clbasualdo.minecraft.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.Aldeano;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Animal;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Creeper;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Enderman;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Entidad;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Esqueleto;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Zombie;
import py.edu.uc.lp3.clbasualdo.minecraft.service.ComportamientoService;

/**
 * Descripcion:
 * Implementacion del servicio de comportamiento.
 *
 * Responsabilidad:
 * Armar la lista con una entidad de cada tipo, todas como Entidad, para que
 * cada una responda su propia reaccion.
 */
@Service
public class ComportamientoServiceImpl implements ComportamientoService {

    @Override
    public List<Entidad> entidadesDeMuestra() {
        return List.of(
                new Zombie(), new Esqueleto(), new Creeper(), new Enderman(),
                new Aldeano(), new Animal("Vaca", "Vaca")
        );
    }
}
