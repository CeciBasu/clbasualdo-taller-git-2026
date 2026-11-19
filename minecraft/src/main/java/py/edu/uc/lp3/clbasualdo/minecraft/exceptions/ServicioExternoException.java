package py.edu.uc.lp3.clbasualdo.minecraft.exceptions;

public class ServicioExternoException extends MinecraftException {

    public ServicioExternoException(String mensaje) {
        super(mensaje);
    }

    public ServicioExternoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
