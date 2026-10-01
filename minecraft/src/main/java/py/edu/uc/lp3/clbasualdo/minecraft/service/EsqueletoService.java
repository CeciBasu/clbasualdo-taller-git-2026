package py.edu.uc.lp3.clbasualdo.minecraft.service;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.Esqueleto;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;

/**
 * Descripcion:
 * Contrato de las operaciones de negocio sobre el Esqueleto.
 *
 * Responsabilidad:
 * Definir como se crea un esqueleto a partir de los datos que llegan por la API y
 * como se obtiene el resultado de las dos versiones de disparar().
 */
public interface EsqueletoService {

    /**
     * Descripcion:
     * Crea un esqueleto con los datos recibidos, lo ubica en la posicion pedida
     * y lo deja guardado en el repositorio.
     *
     * Parametros:
     * nombre - Nombre del esqueleto.
     * vida - Vida inicial.
     * x - Posicion en X.
     * y - Posicion en Y.
     * z - Posicion en Z.
     *
     * Retorno:
     * El esqueleto creado.
     *
     * Errores:
     * DatosInvalidosException - Si el dominio rechaza el nombre o la vida.
     */
    Esqueleto crear(String nombre, int vida, double x, double y, double z) throws DatosInvalidosException;

    /**
     * Descripcion:
     * Crea un esqueleto con los datos recibidos, lo ubica en la posicion pedida
     * y ejecuta ambas variantes de disparar() del dominio para mostrar la
     * sobrecarga en la API. No guarda la instancia.
     *
     * Parametros:
     * nombre - Nombre del esqueleto.
     * vida - Vida inicial.
     * x - Posicion en X.
     * y - Posicion en Y.
     * z - Posicion en Z.
     * distancia - Distancia que se pasa a disparar(double), para ver el contexto.
     *
     * Retorno:
     * El resultado de los dos disparos (sin distancia y con distancia).
     *
     * Errores:
     * DatosInvalidosException - Si el dominio rechaza algun dato.
     */
    DisparoResultado disparar(String nombre, int vida, double x, double y, double z, double distancia)
            throws DatosInvalidosException;
}
