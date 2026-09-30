package py.edu.uc.lp3.clbasualdo.minecraft.service.impl;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.clbasualdo.minecraft.domain.Esqueleto;
import py.edu.uc.lp3.clbasualdo.minecraft.exceptions.DatosInvalidosException;
import py.edu.uc.lp3.clbasualdo.minecraft.repository.EntidadRepository;
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
        try {
            Esqueleto esqueleto = new Esqueleto(nombre, vida);
            esqueleto.teletransportar(x, y, z);
            entidadRepository.guardar(esqueleto);
            return esqueleto;
        } catch (IllegalArgumentException ex) {
            throw new DatosInvalidosException(ex.getMessage(), ex);
        }
    }
}
