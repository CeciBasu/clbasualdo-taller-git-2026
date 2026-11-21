package py.edu.uc.lp3.clbasualdo.minecraft.service.reglas;

import py.edu.uc.lp3.clbasualdo.minecraft.dto.ValidacionExplosionRequest;
import py.edu.uc.lp3.clbasualdo.minecraft.dto.ValidacionExplosionResponse;

public interface ReglasCreeperService {

    ValidacionExplosionResponse validar(ValidacionExplosionRequest pedido);
}
