package py.edu.uc.lp3.clbasualdo.minecraft.rest.dto;

import java.util.Map;

public record ObjetoApiDto(String id, String name, Map<String, Object> data) {
}
