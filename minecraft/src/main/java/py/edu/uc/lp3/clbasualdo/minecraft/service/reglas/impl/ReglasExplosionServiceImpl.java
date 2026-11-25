package py.edu.uc.lp3.clbasualdo.minecraft.service.reglas.impl;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperMuertoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CreeperYaDetonadoException;
import py.edu.uc.lp3.clbasualdo.minecraft.service.reglas.ReglasExplosionService;

/**
 * Descripción:
 * Implementación de las dos reglas de la explosión.
 *
 * Responsabilidad:
 * Lanzar la excepción propia de cada regla cuando el Creeper no puede detonar. No toca la
 * base: recibe el estado y responde.
 */
@Service
public class ReglasExplosionServiceImpl implements ReglasExplosionService {

    @Override
    public void validarCreeperVivo(String nombre, int vida) {
        if (vida <= 0) {
            throw new CreeperMuertoException(nombre + " no puede detonar: está muerto.");
        }
    }

    @Override
    public void validarDetonacionUnica(String nombre, boolean detonado) {
        if (detonado) {
            throw new CreeperYaDetonadoException(nombre + " ya detonó y no puede volver a explotar.");
        }
    }
}
