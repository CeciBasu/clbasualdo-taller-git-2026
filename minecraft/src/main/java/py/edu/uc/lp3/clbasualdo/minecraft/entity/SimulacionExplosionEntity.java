package py.edu.uc.lp3.clbasualdo.minecraft.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Descripcion:
 * Entidad de persistencia que guarda el resultado de una simulacion de explosion.
 *
 * Responsabilidad:
 * Conservar en H2 una foto del estado del Creeper, del Zombie y del Jugador
 * despues de aplicar las dos reglas del dominio, junto con los ids de las filas
 * que se actualizaron. Asi un GET posterior puede mostrar el resultado sin
 * volver a simular.
 */
@Entity
@Table(name = "simulaciones_explosion")
public class SimulacionExplosionEntity {

    /** Identificador unico autogenerado por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Momento en que se ejecuto la simulacion. */
    private LocalDateTime fecha;

    /** Radio de la explosion del Creeper. */
    private double radioExplosion;

    /** Id de la fila del Creeper que participo. */
    private Long creeperId;

    /** Id de la fila del Zombie que participo. */
    private Long zombieId;

    /** Id de la fila del Jugador que participo. */
    private Long jugadorId;

    /** Nombre del Creeper simulado. */
    private String creeperNombre;

    /** Vida del Creeper antes de detonar. */
    private int creeperVidaInicial;

    /** Vida del Creeper despues de detonar. */
    private int creeperVidaFinal;

    /** Indica si el Creeper detono. */
    private boolean creeperDetonado;

    /** Posicion en X del Creeper. */
    private double creeperX;

    /** Posicion en Y del Creeper. */
    private double creeperY;

    /** Posicion en Z del Creeper. */
    private double creeperZ;

    /** Nombre del Zombie simulado. */
    private String zombieNombre;

    /** Vida del Zombie antes de la explosion. */
    private int zombieVidaInicial;

    /** Vida del Zombie despues de la explosion. */
    private int zombieVidaFinal;

    /** Distancia del Zombie al Creeper. */
    private double zombieDistancia;

    /** Indica si la explosion alcanzo al Zombie. */
    private boolean zombieAlcanzado;

    /** Nombre del Jugador simulado. */
    private String jugadorNombre;

    /** Vida del Jugador antes de la explosion. */
    private int jugadorVidaInicial;

    /** Vida del Jugador despues de la explosion. */
    private int jugadorVidaFinal;

    /** Distancia del Jugador al Creeper. */
    private double jugadorDistancia;

    /** Indica si la explosion alcanzo al Jugador. */
    private boolean jugadorAlcanzado;

    /**
     * Descripcion:
     * Constructor vacio requerido por JPA.
     */
    public SimulacionExplosionEntity() {
    }

    /**
     * Descripcion:
     * Devuelve el id de la simulacion.
     *
     * Retorno:
     * El id autogenerado.
     */
    public Long getId() {
        return id;
    }

    /**
     * Descripcion:
     * Asigna el id de la simulacion.
     *
     * Parametros:
     * id - Id a asignar.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Descripcion:
     * Devuelve la fecha de la simulacion.
     *
     * Retorno:
     * La fecha y hora en que se ejecuto.
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Descripcion:
     * Asigna la fecha de la simulacion.
     *
     * Parametros:
     * fecha - Fecha a asignar.
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    /**
     * Descripcion:
     * Devuelve el radio de la explosion.
     *
     * Retorno:
     * El radio usado.
     */
    public double getRadioExplosion() {
        return radioExplosion;
    }

    /**
     * Descripcion:
     * Asigna el radio de la explosion.
     *
     * Parametros:
     * radioExplosion - Radio a asignar.
     */
    public void setRadioExplosion(double radioExplosion) {
        this.radioExplosion = radioExplosion;
    }

    /**
     * Descripcion:
     * Devuelve el id del Creeper persistido.
     *
     * Retorno:
     * El id del Creeper.
     */
    public Long getCreeperId() {
        return creeperId;
    }

    /**
     * Descripcion:
     * Asigna el id del Creeper persistido.
     *
     * Parametros:
     * creeperId - Id a asignar.
     */
    public void setCreeperId(Long creeperId) {
        this.creeperId = creeperId;
    }

    /**
     * Descripcion:
     * Devuelve el id del Zombie persistido.
     *
     * Retorno:
     * El id del Zombie.
     */
    public Long getZombieId() {
        return zombieId;
    }

    /**
     * Descripcion:
     * Asigna el id del Zombie persistido.
     *
     * Parametros:
     * zombieId - Id a asignar.
     */
    public void setZombieId(Long zombieId) {
        this.zombieId = zombieId;
    }

    /**
     * Descripcion:
     * Devuelve el id del Jugador persistido.
     *
     * Retorno:
     * El id del Jugador.
     */
    public Long getJugadorId() {
        return jugadorId;
    }

    /**
     * Descripcion:
     * Asigna el id del Jugador persistido.
     *
     * Parametros:
     * jugadorId - Id a asignar.
     */
    public void setJugadorId(Long jugadorId) {
        this.jugadorId = jugadorId;
    }

    /**
     * Descripcion:
     * Devuelve el nombre del Creeper simulado.
     *
     * Retorno:
     * El nombre del Creeper.
     */
    public String getCreeperNombre() {
        return creeperNombre;
    }

    /**
     * Descripcion:
     * Asigna el nombre del Creeper simulado.
     *
     * Parametros:
     * creeperNombre - Nombre a asignar.
     */
    public void setCreeperNombre(String creeperNombre) {
        this.creeperNombre = creeperNombre;
    }

    /**
     * Descripcion:
     * Devuelve la vida inicial del Creeper.
     *
     * Retorno:
     * La vida antes de detonar.
     */
    public int getCreeperVidaInicial() {
        return creeperVidaInicial;
    }

    /**
     * Descripcion:
     * Asigna la vida inicial del Creeper.
     *
     * Parametros:
     * creeperVidaInicial - Vida a asignar.
     */
    public void setCreeperVidaInicial(int creeperVidaInicial) {
        this.creeperVidaInicial = creeperVidaInicial;
    }

    /**
     * Descripcion:
     * Devuelve la vida final del Creeper.
     *
     * Retorno:
     * La vida despues de detonar.
     */
    public int getCreeperVidaFinal() {
        return creeperVidaFinal;
    }

    /**
     * Descripcion:
     * Asigna la vida final del Creeper.
     *
     * Parametros:
     * creeperVidaFinal - Vida a asignar.
     */
    public void setCreeperVidaFinal(int creeperVidaFinal) {
        this.creeperVidaFinal = creeperVidaFinal;
    }

    /**
     * Descripcion:
     * Dice si el Creeper detono.
     *
     * Retorno:
     * true si detono, false si no.
     */
    public boolean isCreeperDetonado() {
        return creeperDetonado;
    }

    /**
     * Descripcion:
     * Asigna si el Creeper detono.
     *
     * Parametros:
     * creeperDetonado - Estado a asignar.
     */
    public void setCreeperDetonado(boolean creeperDetonado) {
        this.creeperDetonado = creeperDetonado;
    }

    /**
     * Descripcion:
     * Devuelve la posicion X del Creeper.
     *
     * Retorno:
     * La posicion en X.
     */
    public double getCreeperX() {
        return creeperX;
    }

    /**
     * Descripcion:
     * Asigna la posicion X del Creeper.
     *
     * Parametros:
     * creeperX - Valor a asignar.
     */
    public void setCreeperX(double creeperX) {
        this.creeperX = creeperX;
    }

    /**
     * Descripcion:
     * Devuelve la posicion Y del Creeper.
     *
     * Retorno:
     * La posicion en Y.
     */
    public double getCreeperY() {
        return creeperY;
    }

    /**
     * Descripcion:
     * Asigna la posicion Y del Creeper.
     *
     * Parametros:
     * creeperY - Valor a asignar.
     */
    public void setCreeperY(double creeperY) {
        this.creeperY = creeperY;
    }

    /**
     * Descripcion:
     * Devuelve la posicion Z del Creeper.
     *
     * Retorno:
     * La posicion en Z.
     */
    public double getCreeperZ() {
        return creeperZ;
    }

    /**
     * Descripcion:
     * Asigna la posicion Z del Creeper.
     *
     * Parametros:
     * creeperZ - Valor a asignar.
     */
    public void setCreeperZ(double creeperZ) {
        this.creeperZ = creeperZ;
    }

    /**
     * Descripcion:
     * Devuelve el nombre del Zombie simulado.
     *
     * Retorno:
     * El nombre del Zombie.
     */
    public String getZombieNombre() {
        return zombieNombre;
    }

    /**
     * Descripcion:
     * Asigna el nombre del Zombie simulado.
     *
     * Parametros:
     * zombieNombre - Nombre a asignar.
     */
    public void setZombieNombre(String zombieNombre) {
        this.zombieNombre = zombieNombre;
    }

    /**
     * Descripcion:
     * Devuelve la vida inicial del Zombie.
     *
     * Retorno:
     * La vida antes de la explosion.
     */
    public int getZombieVidaInicial() {
        return zombieVidaInicial;
    }

    /**
     * Descripcion:
     * Asigna la vida inicial del Zombie.
     *
     * Parametros:
     * zombieVidaInicial - Vida a asignar.
     */
    public void setZombieVidaInicial(int zombieVidaInicial) {
        this.zombieVidaInicial = zombieVidaInicial;
    }

    /**
     * Descripcion:
     * Devuelve la vida final del Zombie.
     *
     * Retorno:
     * La vida despues de la explosion.
     */
    public int getZombieVidaFinal() {
        return zombieVidaFinal;
    }

    /**
     * Descripcion:
     * Asigna la vida final del Zombie.
     *
     * Parametros:
     * zombieVidaFinal - Vida a asignar.
     */
    public void setZombieVidaFinal(int zombieVidaFinal) {
        this.zombieVidaFinal = zombieVidaFinal;
    }

    /**
     * Descripcion:
     * Devuelve la distancia del Zombie al Creeper.
     *
     * Retorno:
     * La distancia calculada.
     */
    public double getZombieDistancia() {
        return zombieDistancia;
    }

    /**
     * Descripcion:
     * Asigna la distancia del Zombie al Creeper.
     *
     * Parametros:
     * zombieDistancia - Distancia a asignar.
     */
    public void setZombieDistancia(double zombieDistancia) {
        this.zombieDistancia = zombieDistancia;
    }

    /**
     * Descripcion:
     * Dice si la explosion alcanzo al Zombie.
     *
     * Retorno:
     * true si lo alcanzo, false si no.
     */
    public boolean isZombieAlcanzado() {
        return zombieAlcanzado;
    }

    /**
     * Descripcion:
     * Asigna si la explosion alcanzo al Zombie.
     *
     * Parametros:
     * zombieAlcanzado - Estado a asignar.
     */
    public void setZombieAlcanzado(boolean zombieAlcanzado) {
        this.zombieAlcanzado = zombieAlcanzado;
    }

    /**
     * Descripcion:
     * Devuelve el nombre del Jugador simulado.
     *
     * Retorno:
     * El nombre del Jugador.
     */
    public String getJugadorNombre() {
        return jugadorNombre;
    }

    /**
     * Descripcion:
     * Asigna el nombre del Jugador simulado.
     *
     * Parametros:
     * jugadorNombre - Nombre a asignar.
     */
    public void setJugadorNombre(String jugadorNombre) {
        this.jugadorNombre = jugadorNombre;
    }

    /**
     * Descripcion:
     * Devuelve la vida inicial del Jugador.
     *
     * Retorno:
     * La vida antes de la explosion.
     */
    public int getJugadorVidaInicial() {
        return jugadorVidaInicial;
    }

    /**
     * Descripcion:
     * Asigna la vida inicial del Jugador.
     *
     * Parametros:
     * jugadorVidaInicial - Vida a asignar.
     */
    public void setJugadorVidaInicial(int jugadorVidaInicial) {
        this.jugadorVidaInicial = jugadorVidaInicial;
    }

    /**
     * Descripcion:
     * Devuelve la vida final del Jugador.
     *
     * Retorno:
     * La vida despues de la explosion.
     */
    public int getJugadorVidaFinal() {
        return jugadorVidaFinal;
    }

    /**
     * Descripcion:
     * Asigna la vida final del Jugador.
     *
     * Parametros:
     * jugadorVidaFinal - Vida a asignar.
     */
    public void setJugadorVidaFinal(int jugadorVidaFinal) {
        this.jugadorVidaFinal = jugadorVidaFinal;
    }

    /**
     * Descripcion:
     * Devuelve la distancia del Jugador al Creeper.
     *
     * Retorno:
     * La distancia calculada.
     */
    public double getJugadorDistancia() {
        return jugadorDistancia;
    }

    /**
     * Descripcion:
     * Asigna la distancia del Jugador al Creeper.
     *
     * Parametros:
     * jugadorDistancia - Distancia a asignar.
     */
    public void setJugadorDistancia(double jugadorDistancia) {
        this.jugadorDistancia = jugadorDistancia;
    }

    /**
     * Descripcion:
     * Dice si la explosion alcanzo al Jugador.
     *
     * Retorno:
     * true si lo alcanzo, false si no.
     */
    public boolean isJugadorAlcanzado() {
        return jugadorAlcanzado;
    }

    /**
     * Descripcion:
     * Asigna si la explosion alcanzo al Jugador.
     *
     * Parametros:
     * jugadorAlcanzado - Estado a asignar.
     */
    public void setJugadorAlcanzado(boolean jugadorAlcanzado) {
        this.jugadorAlcanzado = jugadorAlcanzado;
    }
}
