package py.edu.uc.lp3.clbasualdo.minecraft.cliente;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.CompaneroNoDisponibleException;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.RechazoDeCompaneroException;

/**
 * Descripción:
 * Cliente HTTP de la regla publicada por un compañero (A2 o A3).
 *
 * Responsabilidad:
 * Mandar un POST con JSON a la ruta de la regla que figura en el OpenAPI del compañero e
 * interpretar la respuesta: 2xx es que la regla aceptó; 4xx es un rechazo de esa regla;
 * 5xx, conexión rechazada o tiempo agotado es que la API del compañero no está disponible.
 * Cada caso queda atribuido al rol y al nombre del compañero. Las subclases solo arman el
 * cuerpo que pide cada OpenAPI.
 */
public abstract class ClienteReglaCompanero {

    /** Configuración del compañero (rol, nombre, URL, ruta). */
    private final CompaneroProperties companero;

    /** Cliente HTTP de Spring apuntado a la URL base del compañero. */
    private final RestClient restClient;

    /**
     * Descripción:
     * Arma el cliente HTTP con la URL base y el tiempo máximo de espera.
     *
     * Parámetros:
     * companero - Configuración del compañero.
     * timeoutMs - Tiempo máximo de conexión y de lectura, en milisegundos.
     */
    protected ClienteReglaCompanero(CompaneroProperties companero, int timeoutMs) {
        this.companero = companero;
        SimpleClientHttpRequestFactory fabrica = new SimpleClientHttpRequestFactory();
        fabrica.setConnectTimeout(Duration.ofMillis(timeoutMs));
        fabrica.setReadTimeout(Duration.ofMillis(timeoutMs));
        this.restClient = RestClient.builder()
                .baseUrl(companero.baseUrl())
                .requestFactory(fabrica)
                .build();
    }

    /**
     * Descripción:
     * Devuelve la configuración del compañero.
     *
     * Retorno:
     * Rol, nombre, URL y ruta.
     */
    public CompaneroProperties getCompanero() {
        return companero;
    }

    /**
     * Descripción:
     * Consulta la regla del compañero con el cuerpo dado.
     *
     * Parámetros:
     * cuerpo - JSON que pide el OpenAPI del compañero.
     *
     * Retorno:
     * La aceptación, con el código y el cuerpo que respondió.
     *
     * Errores:
     * RechazoDeCompaneroException - Si la regla respondió 4xx.
     * CompaneroNoDisponibleException - Si respondió 5xx o no respondió.
     */
    protected AceptacionCompanero consultar(Map<String, Object> cuerpo) {
        String url = companero.baseUrl() + companero.ruta();
        RespuestaHttp respuesta;
        try {
            respuesta = restClient.post()
                    .uri(companero.ruta())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(cuerpo)
                    .exchange((pedido, res) -> new RespuestaHttp(res.getStatusCode().value(),
                            new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8)));
        } catch (ResourceAccessException ex) {
            throw new CompaneroNoDisponibleException(companero.rol(), companero.nombre(), url, null, null, ex);
        }
        if (respuesta.estado() >= 500) {
            throw new CompaneroNoDisponibleException(companero.rol(), companero.nombre(), url,
                    respuesta.estado(), respuesta.cuerpo(), null);
        }
        if (respuesta.estado() >= 400) {
            throw new RechazoDeCompaneroException(companero.rol(), companero.nombre(), companero.regla(),
                    respuesta.estado(), respuesta.cuerpo());
        }
        return new AceptacionCompanero(companero.rol(), companero.nombre(), companero.regla(),
                respuesta.estado(), respuesta.cuerpo());
    }

    /**
     * Descripción:
     * Código y cuerpo crudos de una respuesta HTTP.
     *
     * Parámetros:
     * estado - Código HTTP.
     * cuerpo - Cuerpo como texto.
     */
    private record RespuestaHttp(int estado, String cuerpo) {
    }
}
