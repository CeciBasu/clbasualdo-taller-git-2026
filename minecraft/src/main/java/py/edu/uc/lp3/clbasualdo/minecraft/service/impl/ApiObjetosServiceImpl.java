package py.edu.uc.lp3.clbasualdo.minecraft.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.ServicioExternoException;
import py.edu.uc.lp3.clbasualdo.minecraft.rest.dto.ObjetoApiDto;
import py.edu.uc.lp3.clbasualdo.minecraft.service.ApiObjetosService;

@Service
public class ApiObjetosServiceImpl implements ApiObjetosService {

    private final WebClient webClient;

    public ApiObjetosServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<ObjetoApiDto> listarObjetos() {
        try {
            return Objects.requireNonNullElse(webClient.get()
                    .uri("/objects")
                    .retrieve()
                    .bodyToFlux(ObjetoApiDto.class)
                    .collectList()
                    .block(), List.of());
        } catch (WebClientException ex) {
            throw new ServicioExternoException("No se pudo consultar el listado de objetos de la API externa.", ex);
        }
    }

    @Override
    public ObjetoApiDto obtenerObjeto(String id) {
        try {
            ObjetoApiDto objeto = webClient.get()
                    .uri("/objects/{id}", id)
                    .retrieve()
                    .bodyToMono(ObjetoApiDto.class)
                    .block();
            if (objeto == null) {
                throw new RecursoNoEncontradoException("No existe el objeto con id " + id + " en la API externa.");
            }
            return objeto;
        } catch (WebClientResponseException.NotFound ex) {
            throw new RecursoNoEncontradoException("No existe el objeto con id " + id + " en la API externa.", ex);
        } catch (WebClientException ex) {
            throw new ServicioExternoException("No se pudo consultar el objeto con id " + id + " en la API externa.", ex);
        }
    }
}
