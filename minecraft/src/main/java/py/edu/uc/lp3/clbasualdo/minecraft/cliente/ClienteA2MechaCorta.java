package py.edu.uc.lp3.clbasualdo.minecraft.cliente;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import py.edu.uc.lp3.clbasualdo.minecraft.entity.CreeperEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.JugadorEntity;

/**
 * Descripción:
 * Cliente de la regla «mecha corta» de A2 (Irina Insfrán): un Creeper vivo solo enciende la
 * mecha si hay un Jugador vivo a 3 bloques o menos.
 *
 * Responsabilidad:
 * Armar el cuerpo que pide el OpenAPI de A2 (posiciones y salud del Creeper y del Jugador)
 * y consultarlo. Los nombres de campos (posicionX, salud) son los del dominio de A2; la ruta
 * sale de la propiedad companeros.a2.ruta.
 */
@Component
public class ClienteA2MechaCorta extends ClienteReglaCompanero {

    /**
     * Descripción:
     * Crea el cliente con la configuración de A2.
     *
     * Parámetros:
     * config - Configuración de los compañeros.
     */
    public ClienteA2MechaCorta(CompanerosConfig config) {
        super(config.a2(), config.timeoutMs() == null ? 3000 : config.timeoutMs());
    }

    /**
     * Descripción:
     * Pregunta a A2 si el Creeper enciende la mecha con ese Jugador cerca.
     *
     * Parámetros:
     * creeper - Creeper guardado en mi H2.
     * jugador - Jugador guardado en mi H2.
     *
     * Retorno:
     * La aceptación de A2.
     */
    public AceptacionCompanero validar(CreeperEntity creeper, JugadorEntity jugador) {
        Map<String, Object> cuerpo = Map.of(
                "creeper", Map.of(
                        "posicionX", creeper.getX(),
                        "posicionY", creeper.getY(),
                        "posicionZ", creeper.getZ(),
                        "salud", creeper.getVida()),
                "jugadores", List.of(Map.of(
                        "nombre", jugador.getNombre(),
                        "posicionX", jugador.getX(),
                        "posicionY", jugador.getY(),
                        "posicionZ", jugador.getZ(),
                        "salud", jugador.getVida())));
        return consultar(cuerpo);
    }
}
