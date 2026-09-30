package py.edu.uc.lp3.clbasualdo.minecraft;

public class Esqueleto extends EntidadHostil {
    private int flechas;

    public Esqueleto() { this("Esqueleto", 20); }

    public Esqueleto(String nombre, int vida) {
        super(nombre, vida, 4, 8.0);
        this.flechas = 16;
    }

    public int getFlechas() { return flechas; }

    public void recargar(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad a recargar no puede ser negativa.");
        }
        flechas += cantidad;
        System.out.println(getNombre() + " recarga, ahora tiene " + flechas + " flechas.");
    }

    @Override
    public void atacar() {
        if (flechas <= 0) {
            System.out.println("El Esqueleto no tiene flechas y ataca cuerpo a cuerpo.");
            return;
        }
        flechas--;
        System.out.println("El Esqueleto dispara una flecha. Le quedan " + flechas + ".");
    }

    @Override
    public String reaccionar() {
        return getNombre() + " retrocede y dispara una flecha.";
    }
}
