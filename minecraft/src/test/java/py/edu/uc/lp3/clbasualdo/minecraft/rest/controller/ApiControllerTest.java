package py.edu.uc.lp3.clbasualdo.minecraft.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Descripcion:
 * Pruebas de la entrada HTTP: las cuatro rutas que expone la API.
 *
 * Responsabilidad:
 * Verificar que el IndexController confirme que el servicio esta vivo, que el
 * EsqueletoController construya desde la URL y arme el JSON, que la sobrecarga de
 * disparar() se vea en la respuesta, y que un dato invalido termine en un 400 con
 * el mensaje del dominio y no en un 500.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerTest {

    /** Cliente HTTP de prueba que habla con los controllers sin levantar un puerto. */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Descripcion:
     * GET / responde 200 con un texto que dice de que trata la API. Como devuelve
     * texto plano y no JSON, se verifica el cuerpo y no una ruta de JSON.
     */
    @Test
    void laRaizConfirmaQueElServicioEstaVivo() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Minecraft")));
    }

    /**
     * Descripcion:
     * /esqueleto construye el esqueleto con los parametros de la URL y devuelve su
     * estado en JSON.
     */
    @Test
    void esqueletoSeConstruyeDesdeLaUrl() throws Exception {
        mockMvc.perform(get("/esqueleto").param("nombre", "Bony").param("vida", "15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Bony"))
                .andExpect(jsonPath("$.vida").value(15))
                .andExpect(jsonPath("$.vivo").value(true))
                .andExpect(jsonPath("$.flechas").value(16));
    }

    /**
     * Descripcion:
     * Una vida de 0 la rechaza el dominio, y la API lo informa con un 400 y el
     * mensaje del dominio, no con un error de servidor.
     */
    @Test
    void unaVidaInvalidaDevuelve400() throws Exception {
        mockMvc.perform(get("/esqueleto").param("vida", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("La vida inicial debe ser mayor a 0."));
    }

    /**
     * Descripcion:
     * /comportamiento devuelve el JSON de las entidades hijas, todas tratadas como
     * Entidad, y cada una con su propia reaccion.
     */
    @Test
    void comportamientoDevuelveLaReaccionDeCadaHija() throws Exception {
        mockMvc.perform(get("/comportamiento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$[0].tipo").value("Zombie"))
                .andExpect(jsonPath("$[0].reaccion").value("Zombie gruñe y persigue al jugador."))
                .andExpect(jsonPath("$[2].tipo").value("Creeper"))
                .andExpect(jsonPath("$[3].tipo").value("Enderman"));
    }

    /**
     * Descripcion:
     * /esqueleto/disparar muestra las dos versiones de disparar() en el mismo JSON:
     * la que no lleva distancia y la que si la lleva. Con una distancia dentro del
     * rango, las dos gastan flecha.
     */
    @Test
    void disparoMuestraLasDosVersionesDelMensaje() throws Exception {
        mockMvc.perform(get("/esqueleto/disparar").param("nombre", "Bony").param("distancia", "5.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Bony"))
                .andExpect(jsonPath("$.flechasIniciales").value(16))
                .andExpect(jsonPath("$.flechasRestantes").value(14))
                .andExpect(jsonPath("$.distanciaPedida").value(5.0))
                .andExpect(jsonPath("$.disparoSinDistancia").value(containsString("dispara una flecha")))
                .andExpect(jsonPath("$.disparoConDistancia").value(containsString("dispara una flecha")));
    }

    /**
     * Descripcion:
     * La diferencia entre las dos versiones se ve con una distancia fuera del rango:
     * el disparo sin distancia igual gasta la flecha, el que lleva distancia no.
     */
    @Test
    void disparoFueraDeRangoNoGastaLaSegundaFlecha() throws Exception {
        mockMvc.perform(get("/esqueleto/disparar").param("distancia", "20.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flechasRestantes").value(15))
                .andExpect(jsonPath("$.disparoConDistancia").value(containsString("no alcanza")));
    }

    /**
     * Descripcion:
     * Una distancia negativa la rechaza el dominio y la API la informa con un 400,
     * no con un error de servidor.
     */
    @Test
    void unaDistanciaNegativaDevuelve400() throws Exception {
        mockMvc.perform(get("/esqueleto/disparar").param("distancia", "-3.0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("La distancia no puede ser negativa."));
    }
}
