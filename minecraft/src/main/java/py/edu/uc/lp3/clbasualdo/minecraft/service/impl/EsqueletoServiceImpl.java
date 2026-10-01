package py.edu.uc.lp3.clbasualdo.minecraft.service.impl;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.Esqueleto;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.EntidadRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.service.DisparoResultado;
import py.edu.uc.lp3.clbasualdo.minecraft.service.EsqueletoService;

/**
 * Descripcion:
 * Implementacion del servicio del Esqueleto.
 *
 * Responsabilidad:
 * Crear el esqueleto con las reglas del dominio, traducir los errores de datos
 * a una excepcion propia y guardarlo en el repositorio.
 */
@Service
public class EsqueletoServiceImpl implements EsqueletoService {

    /** Repositorio donde se guardan las entidades. */
    private final EntidadRepository entidadRepository;

    /**
     * Descripcion:
     * Crea el servicio con el repositorio que va a usar.
     *
     * Parametros:
     * entidadRepository - El repositorio de entidades.
     */
    public EsqueletoServiceImpl(EntidadRepository entidadRepository) {
        this.entidadRepository = entidadRepository;
    }

    @Override
    public Esqueleto crear(String nombre, int vida, double x, double y, double z) throws DatosInvalidosException {
        Esqueleto esqueleto = construir(nombre, vida, x, y, z);
        entidadRepository.guardar(esqueleto);
        return esqueleto;
    }

    @Override
    public DisparoResultado disparar(String nombre, int vida, double x, double y, double z, double distancia)
            throws DatosInvalidosException {
        Esqueleto esqueleto = construir(nombre, vida, x, y, z);
        int flechasIniciales = esqueleto.getFlechas();

        // Se llaman las dos versiones del mismo mensaje del dominio, una con distancia
        // y otra sin distancia, para que la diferencia se vea en la respuesta.
        // Los dos disparos van dentro del try porque disparar(double) tambien puede
        // rechazar un dato (una distancia negativa), y eso tambien es un error del
        // usuario que hay que terminar en un 400, no en un 500.
        try {
            String sinDistancia = esqueleto.disparar();
            String conDistancia = esqueleto.disparar(distancia);

            return new DisparoResultado(
                    esqueleto.getNombre(),
                    flechasIniciales,
                    esqueleto.getFlechas(),
                    esqueleto.getRangoAtaque(),
                    distancia,
                    sinDistancia,
                    conDistancia);
        } catch (IllegalArgumentException ex) {
            throw new DatosInvalidosException(ex.getMessage(), ex);
        }
    }

    /**
     * Descripcion:
     * Arma un esqueleto con los datos recibidos y lo ubica en la posicion pedida.
     * Traduce los errores del dominio a una excepcion propia.
     *
     * Parametros:
     * nombre - Nombre del esqueleto.
     * vida - Vida inicial.
     * x - Posicion en X.
     * y - Posicion en Y.
     * z - Posicion en Z.
     *
     * Retorno:
     * El esqueleto recien creado y ubicado.
     *
     * Errores:
     * DatosInvalidosException - Si el dominio rechaza algun dato.
     */
    private Esqueleto construir(String nombre, int vida, double x, double y, double z) throws DatosInvalidosException {
        try {
            Esqueleto esqueleto = new Esqueleto(nombre, vida);
            esqueleto.teletransportar(x, y, z);
            return esqueleto;
        } catch (IllegalArgumentException ex) {
            throw new DatosInvalidosException(ex.getMessage(), ex);
        }
    }
}
