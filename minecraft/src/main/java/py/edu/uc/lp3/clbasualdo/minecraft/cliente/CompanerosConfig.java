package py.edu.uc.lp3.clbasualdo.minecraft.cliente;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Descripción:
 * Agrupa la configuración de A2 y A3 (prefijo "companeros" en application.properties).
 *
 * Responsabilidad:
 * Que Spring arme los datos de los dos compañeros desde las propiedades o desde las
 * variables de entorno A2_BASE_URL y A3_BASE_URL.
 *
 * Parámetros:
 * a2 - Configuración del primer compañero.
 * a3 - Configuración del segundo compañero.
 * timeoutMs - Tiempo máximo de espera de cada llamada, en milisegundos.
 */
@ConfigurationProperties(prefix = "companeros")
public record CompanerosConfig(CompaneroProperties a2, CompaneroProperties a3, Integer timeoutMs) {
}
