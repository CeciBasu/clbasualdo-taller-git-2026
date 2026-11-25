package py.edu.uc.lp3.clbasualdo.minecraft.service.reglas.impl;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.clbasualdo.minecraft.dto.ValidacionExplosionRequest;
import py.edu.uc.lp3.clbasualdo.minecraft.dto.ValidacionExplosionResponse;
import py.edu.uc.lp3.clbasualdo.minecraft.service.reglas.ReglasCreeperService;
import py.edu.uc.lp3.clbasualdo.minecraft.service.reglas.ReglasExplosionService;

@Service
public class ReglasCreeperServiceImpl implements ReglasCreeperService {

    private static final String NOMBRE_CREEPER = "Creeper";
    private static final int SALUD_POR_DEFECTO = 20;
    private static final String MENSAJE_ACEPTADA = "El Creeper está vivo y todavía no detonó.";

    private final ReglasExplosionService reglasExplosionService;

    public ReglasCreeperServiceImpl(ReglasExplosionService reglasExplosionService) {
        this.reglasExplosionService = reglasExplosionService;
    }

    @Override
    public ValidacionExplosionResponse validar(ValidacionExplosionRequest pedido) {
        int salud = pedido.saludCreeper() == null ? SALUD_POR_DEFECTO : pedido.saludCreeper();
        boolean yaDetonado = Boolean.TRUE.equals(pedido.yaDetonado());

        reglasExplosionService.validarCreeperVivo(NOMBRE_CREEPER, salud);
        reglasExplosionService.validarDetonacionUnica(NOMBRE_CREEPER, yaDetonado);

        return new ValidacionExplosionResponse(true, MENSAJE_ACEPTADA);
    }
}
