package py.edu.uc.lp3.clbasualdo.minecraft.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.Aldeano;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Animal;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Creeper;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Enderman;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Esqueleto;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Jugador;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.UICapable;
import py.edu.uc.lp3.clbasualdo.minecraft.domain.Zombie;
import py.edu.uc.lp3.clbasualdo.minecraft.service.UiService;

/**
 * Descripcion:
 * Implementacion del servicio de UI.
 *
 * Responsabilidad:
 * Armar la lista con todos los elementos representables del dominio (el jugador,
 * los cuatro enemigos y las dos entidades pasivas) tratados todos como
 * UICapable, para que cada uno responda sus datos a su modo.
 */
@Service
public class UiServiceImpl implements UiService {

    @Override
    public List<UICapable> entidadesUi() {
        Jugador jugador = new Jugador("Steve", 20, 1);
        jugador.teletransportar(0, 64, 0);

        Zombie zombie = new Zombie();
        zombie.teletransportar(1, 64, 0);

        Esqueleto esqueleto = new Esqueleto();
        esqueleto.teletransportar(5, 64, 2);

        Creeper creeper = new Creeper();
        creeper.teletransportar(2, 64, 1);

        Enderman enderman = new Enderman();
        enderman.teletransportar(7, 64, -3);

        Aldeano aldeano = new Aldeano("Herrero");
        aldeano.teletransportar(-3, 64, 0);

        Animal vaca = new Animal("Vaca", "Vaca");
        vaca.teletransportar(-1, 64, -2);

        return List.of(jugador, zombie, esqueleto, creeper, enderman, aldeano, vaca);
    }
}
