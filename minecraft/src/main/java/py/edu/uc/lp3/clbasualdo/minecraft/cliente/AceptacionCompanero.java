package py.edu.uc.lp3.clbasualdo.minecraft.cliente;

/**
 * Descripción:
 * Lo que respondió un compañero cuando su regla aceptó la jugada.
 *
 * Responsabilidad:
 * Guardar quién aceptó y su respuesta original, para mostrarla y persistirla con la jugada.
 *
 * Parámetros:
 * rol - "A2" o "A3".
 * companero - Nombre del compañero.
 * regla - Regla consultada.
 * estado - Código HTTP 2xx que devolvió.
 * respuesta - Cuerpo que devolvió, tal cual.
 */
public record AceptacionCompanero(String rol, String companero, String regla, int estado, String respuesta) {
}
