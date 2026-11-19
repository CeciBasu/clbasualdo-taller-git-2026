package py.edu.uc.lp3.clbasualdo.minecraft.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.ConnectException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import reactor.core.publisher.Mono;

import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.ServicioExternoException;
import py.edu.uc.lp3.clbasualdo.minecraft.rest.dto.ObjetoApiDto;

class ApiObjetosServiceImplTest {

    private static final String LISTA_JSON = """
            [
              {"id":"1","name":"Google Pixel 6 Pro","data":{"color":"Cloudy White"}},
              {"id":"2","name":"Apple iPhone 12 Mini","data":null}
            ]
            """;

    private static final String OBJETO_JSON = """
            {"id":"7","name":"Apple MacBook Pro 16","data":{"year":2019,"price":1849.99}}
            """;

    private ApiObjetosServiceImpl servicio(HttpStatus status, String json) {
        ExchangeFunction exchangeFunction = request -> Mono.just(ClientResponse.create(status)
                .header("Content-Type", "application/json")
                .body(json)
                .build());
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.restful-api.dev")
                .exchangeFunction(exchangeFunction)
                .build();
        return new ApiObjetosServiceImpl(webClient);
    }

    private ApiObjetosServiceImpl servicioConErrorDeConexion() {
        ExchangeFunction exchangeFunction = request -> Mono.error(new WebClientRequestException(
                new ConnectException("Connection refused"),
                request.method(), request.url(), request.headers()));
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.restful-api.dev")
                .exchangeFunction(exchangeFunction)
                .build();
        return new ApiObjetosServiceImpl(webClient);
    }

    @Test
    void listaLosObjetosDeLaApiExterna() {
        List<ObjetoApiDto> objetos = servicio(HttpStatus.OK, LISTA_JSON).listarObjetos();

        assertEquals(2, objetos.size());
        assertEquals("1", objetos.get(0).id());
        assertEquals("Google Pixel 6 Pro", objetos.get(0).name());
        assertEquals("Cloudy White", objetos.get(0).data().get("color"));
        assertNull(objetos.get(1).data());
    }

    @Test
    void obtieneUnObjetoPorId() {
        ObjetoApiDto objeto = servicio(HttpStatus.OK, OBJETO_JSON).obtenerObjeto("7");

        assertEquals("7", objeto.id());
        assertEquals("Apple MacBook Pro 16", objeto.name());
        assertEquals(2019, ((Number) objeto.data().get("year")).intValue());
    }

    @Test
    void unIdInexistenteTerminaEnNoEncontrado() {
        assertThrows(RecursoNoEncontradoException.class,
                () -> servicio(HttpStatus.NOT_FOUND, "Object not found.").obtenerObjeto("999"));
    }

    @Test
    void unErrorDeLaApiTerminaEnServicioExterno() {
        assertThrows(ServicioExternoException.class,
                () -> servicio(HttpStatus.INTERNAL_SERVER_ERROR, "Error").listarObjetos());
    }

    @Test
    void unErrorDeConexionTerminaEnServicioExterno() {
        assertThrows(ServicioExternoException.class,
                () -> servicioConErrorDeConexion().listarObjetos());
    }
}
