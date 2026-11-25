package py.edu.uc.lp3.clbasualdo.minecraft.cliente;

/**
 * Descripción:
 * Configuración de la API de un compañero, leída de application.properties.
 *
 * Responsabilidad:
 * Guardar en un solo lugar quién es, dónde está su servicio y qué ruta de su OpenAPI se
 * consulta. Si el compañero cambia una ruta, se actualiza la propiedad, no el código.
 *
 * Parámetros:
 * rol - "A2" o "A3".
 * nombre - Nombre del compañero.
 * baseUrl - URL base de su servicio (por ejemplo http://localhost:8082).
 * ruta - Ruta de la regla, tal como figura en su OpenAPI.
 * regla - Nombre corto de la regla, para los mensajes.
 * openapi - URL de su documento OpenAPI.
 */
public record CompaneroProperties(
        String rol,
        String nombre,
        String baseUrl,
        String ruta,
        String regla,
        String openapi) {
}
