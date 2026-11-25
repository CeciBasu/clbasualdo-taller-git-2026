package py.edu.uc.lp3.clbasualdo.minecraft.cliente;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import py.edu.uc.lp3.clbasualdo.minecraft.entity.CreeperEntity;
import py.edu.uc.lp3.clbasualdo.minecraft.entity.JugadorEntity;

public final class CuerpoReglasCompanero {

    private static final int TIEMPO_EXPLOSION = 3;
    private static final double RADIO_EXPLOSION = 3.0;

    private CuerpoReglasCompanero() {
    }

    public static Map<String, Object> de(CreeperEntity creeper, JugadorEntity jugador, int tiempoTranscurrido) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("saludCreeper", creeper.getVida());
        cuerpo.put("tiempoExplosion", TIEMPO_EXPLOSION);
        cuerpo.put("tiempoTranscurrido", tiempoTranscurrido);
        cuerpo.put("radioExplosion", RADIO_EXPLOSION);
        cuerpo.put("creeper", posicion(creeper.getX(), creeper.getY(), creeper.getZ()));

        Map<String, Object> objetivo = new LinkedHashMap<>();
        objetivo.put("tipo", "jugador");
        objetivo.put("salud", jugador.getVida());
        objetivo.put("posicion", posicion(jugador.getX(), jugador.getY(), jugador.getZ()));
        cuerpo.put("objetivos", List.of(objetivo));
        return cuerpo;
    }

    private static Map<String, Object> posicion(double x, double y, double z) {
        Map<String, Object> posicion = new LinkedHashMap<>();
        posicion.put("x", x);
        posicion.put("y", y);
        posicion.put("z", z);
        return posicion;
    }
}
