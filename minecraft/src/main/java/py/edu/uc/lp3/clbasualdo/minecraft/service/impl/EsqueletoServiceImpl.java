package py.edu.uc.lp3.clbasualdo.minecraft.service.impl;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.Esqueleto;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.EntidadRepository;
import py.edu.uc.lp3.clbasualdo.minecraft.service.DisparoResultado;
import py.edu.uc.lp3.clbasualdo.minecraft.service.EsqueletoService;

/**
 * Descripción:
 * Implementación del servicio del Esqueleto.
 *
 * Responsabilidad:
 * Validar los datos que llegan (nombre, vida y distancia), crear el esqueleto con esos
 * datos y guardarlo en el repositorio. El dominio ya no valida: si un dato no sirve,
 * este servicio lanza DatosInvalidosException antes de crear el objeto.
 */
@Service
public class EsqueletoServiceImpl implements EsqueletoService {

    /** Repositorio donde se guardan las entidades. */
    private final EntidadRepository entidadRepository;

    /**
     * Descripción:
     * Crea el servicio con el repositorio que va a usar.
     *
     * Parámetros:
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
        if (distancia < 0) {
            throw new DatosInvalidosException("La distancia no puede ser negativa.");
        }
        int flechasIniciales = esqueleto.getFlechas();

        // Se llaman las dos versiones del mismo mensaje del dominio, una sin distancia
        // y otra con distancia, para que la diferencia se vea en la respuesta.
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
    }

    /**
     * Descripción:
     * Valida el nombre y la vida, arma un esqueleto y lo ubica en la posición pedida.
     *
     * Parámetros:
     * nombre - Nombre del esqueleto.
     * vida - Vida inicial.
     * x - Posición en X.
     * y - Posición en Y.
     * z - Posición en Z.
     *
     * Retorno:
     * El esqueleto recién creado y ubicado.
     *
     * Errores:
     * DatosInvalidosException - Si el nombre está vacío o la vida no es mayor a 0.
     */
    private Esqueleto construir(String nombre, int vida, double x, double y, double z) throws DatosInvalidosException {
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre no puede estar vacío.");
        }
        if (vida <= 0) {
            throw new DatosInvalidosException("La vida inicial debe ser mayor a 0.");
        }
        Esqueleto esqueleto = new Esqueleto(nombre, vida);
        esqueleto.teletransportar(x, y, z);
        return esqueleto;
    }
}
