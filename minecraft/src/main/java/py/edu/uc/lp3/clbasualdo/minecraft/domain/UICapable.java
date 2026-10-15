package py.edu.uc.lp3.clbasualdo.minecraft.domain;

/**
 * Descripcion:
 * Capacidad que tienen todos los elementos representables del dominio (jugador,
 * enemigos, aldeanos y animales) para ser dibujados en una interfaz grafica (UI).
 *
 * Responsabilidad:
 * Exponer los cuatro datos que la UI necesita de una entidad: su imagen, su
 * posicion, su sonido y su velocidad. El sonido queda con un comportamiento por
 * defecto, que las entidades con sonido propio reemplazan usando @Override.
 */
public interface UICapable {

    /**
     * Descripcion:
     * Devuelve el nombre del archivo de imagen (textura) con el que se dibuja la entidad.
     *
     * Retorno:
     * El nombre de la imagen de la entidad.
     */
    String imagen();

    /**
     * Descripcion:
     * Devuelve la posicion de la entidad formateada para mostrarla en la UI.
     *
     * Retorno:
     * Un texto con las coordenadas x, y, z.
     */
    String posicion();

    /**
     * Descripcion:
     * Devuelve la velocidad a la que se mueve la entidad.
     *
     * Retorno:
     * La velocidad de la entidad.
     */
    double velocidad();

    /**
     * Descripcion:
     * Devuelve el sonido de la entidad. Es el comportamiento por defecto: las
     * entidades con un sonido propio lo reemplazan usando @Override.
     *
     * Retorno:
     * Un texto que describe el sonido.
     */
    default String sonido() {
        return "Emite un sonido generico de criatura.";
    }
}
