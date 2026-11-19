package py.edu.uc.lp3.clbasualdo.minecraft.service;

import java.util.List;

import py.edu.uc.lp3.clbasualdo.minecraft.rest.dto.ObjetoApiDto;

public interface ApiObjetosService {

    List<ObjetoApiDto> listarObjetos();

    ObjetoApiDto obtenerObjeto(String id);
}
