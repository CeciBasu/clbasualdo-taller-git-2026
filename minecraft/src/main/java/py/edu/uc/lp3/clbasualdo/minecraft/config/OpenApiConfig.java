package py.edu.uc.lp3.clbasualdo.minecraft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Descripción:
 * Datos generales del documento OpenAPI que publica springdoc.
 *
 * Responsabilidad:
 * Darle título, versión y una descripción con las dos reglas, para que un compañero
 * entienda la API sin abrir el código. El documento queda en /v3/api-docs (JSON),
 * /v3/api-docs.yaml (YAML) y /swagger-ui.html (navegador).
 */
@Configuration
public class OpenApiConfig {

    /**
     * Descripción:
     * Arma la cabecera del documento OpenAPI.
     *
     * Retorno:
     * El objeto OpenAPI con la información de la API.
     */
    @Bean
    public OpenAPI minecraftOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Minecraft API — clbasualdo (Cecilia Basualdo)")
                .version("poo-09")
                .description("""
                        CRUD de entidades de Minecraft (Creeper, Zombie, Jugador) guardadas en H2, \
                        simulación de explosiones sobre las entidades guardadas y las dos reglas de \
                        la explosión.

                        **Regla 1 — Creeper vivo:** un Creeper solo detona si tiene vida mayor a 0. \
                        Si no, 422 `CreeperMuertoException`.

                        **Regla 2 — Detonación única:** un Creeper solo detona una vez. Un segundo \
                        intento da 409 `CreeperYaDetonadoException`.

                        Las dos reglas se pueden consultar sin modificar la base con \
                        `POST /reglas/creeper-vivo` y `POST /reglas/detonacion-unica`. \
                        Todos los errores tienen la forma `ErrorRespuesta`.""")
                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
