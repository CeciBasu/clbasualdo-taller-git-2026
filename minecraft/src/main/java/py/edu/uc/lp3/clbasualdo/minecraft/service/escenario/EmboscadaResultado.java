package py.edu.uc.lp3.clbasualdo.minecraft.service.escenario;

import java.time.LocalDateTime;

import py.edu.uc.lp3.clbasualdo.minecraft.service.SimulacionExplosionResultado;

/**
 * Descripción:
 * Resultado de una emboscada guardada.
 *
 * Responsabilidad:
 * Mostrar la jugada con lo que respondió cada compañero y la explosión que se guardó.
 *
 * Parámetros:
 * id - Id de la jugada en la tabla "emboscadas".
 * fecha - Momento en que se guardó.
 * creeperId - Id del Creeper.
 * jugadorId - Id del Jugador.
 * a2 - Lo que respondió A2.
 * a3 - Lo que respondió A3.
 * simulacion - La explosión guardada, leída de H2.
 */
public record EmboscadaResultado(
        Long id,
        LocalDateTime fecha,
        Long creeperId,
        Long jugadorId,
        RespuestaCompanero a2,
        RespuestaCompanero a3,
        SimulacionExplosionResultado simulacion) {

    /**
     * Descripción:
     * Lo que respondió un compañero que aceptó.
     *
     * Parámetros:
     * companero - Nombre del compañero.
     * respuesta - Cuerpo que devolvió, tal cual.
     */
    public record RespuestaCompanero(String companero, String respuesta) {
    }
}
