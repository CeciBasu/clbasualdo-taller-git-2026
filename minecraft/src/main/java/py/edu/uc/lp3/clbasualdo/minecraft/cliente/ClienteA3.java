package py.edu.uc.lp3.clbasualdo.minecraft.cliente;

import org.springframework.stereotype.Component;

import py.edu.uc.lp3.clbasualdo.minecraft.entity.CreeperEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.JugadorEntity;

/**
 * Descripción:
 * Cliente de la regla de A3, el segundo compañero del grupo.
 *
 * Responsabilidad:
 * Armar el cuerpo que pide el OpenAPI de A3 con el Creeper y el Jugador de la emboscada y
 * consultarlo. La ruta y la URL salen de las propiedades companeros.a3.*.
 */
@Component
public class ClienteA3 extends ClienteReglaCompanero {

    /**
     * Descripción:
     * Crea el cliente con la configuración de A3.
     *
     * Parámetros:
     * config - Configuración de los compañeros.
     */
    public ClienteA3(CompanerosConfig config) {
        super(config.a3(), config.timeoutMs() == null ? 3000 : config.timeoutMs());
    }

    /**
     * Descripción:
     * Pregunta a A3 si la emboscada es legal según su regla.
     *
     * Parámetros:
     * creeper - Creeper guardado en mi H2.
     * jugador - Jugador guardado en mi H2.
     * tiempoTranscurrido - Segundos transcurridos desde que se activó la mecha.
     *
     * Retorno:
     * La aceptación de A3.
     */
    public AceptacionCompanero validar(CreeperEntity creeper, JugadorEntity jugador, int tiempoTranscurrido) {
        return consultar(CuerpoReglasCompanero.de(creeper, jugador, tiempoTranscurrido));
    }
}
