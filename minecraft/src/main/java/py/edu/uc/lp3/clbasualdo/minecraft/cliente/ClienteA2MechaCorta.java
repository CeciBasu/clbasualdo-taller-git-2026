package py.edu.uc.lp3.clbasualdo.minecraft.cliente;

import org.springframework.stereotype.Component;

import py.edu.uc.lp3.clbasualdo.minecraft.entity.CreeperEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.JugadorEntity;

/**
 * Descripción:
 * Cliente de la regla «mecha corta» de A2 (Irina Insfrán): un Creeper vivo solo enciende la
 * mecha si hay un Jugador vivo a 3 bloques o menos.
 *
 * Responsabilidad:
 * Armar el cuerpo que pide el OpenAPI de A2 y consultarlo. Comparte el cuerpo de
 * /explosiones/validar: A2 ignora los campos de tiempo y radio; la ruta sale de la propiedad
 * companeros.a2.ruta.
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
     * tiempoTranscurrido - Segundos transcurridos desde que se activó la mecha.
     *
     * Retorno:
     * La aceptación de A2.
     */
    public AceptacionCompanero validar(CreeperEntity creeper, JugadorEntity jugador, int tiempoTranscurrido) {
        return consultar(CuerpoReglasCompanero.de(creeper, jugador, tiempoTranscurrido));
    }
}
