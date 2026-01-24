// java
package monopoly.edificios;
import partida.*;
import monopoly.casilla.propiedad.*;

/**
 * Representa una piscina (edificio) asociada a un solar del tablero.
 * Mantiene el alquiler específico de la piscina y delega la inicialización a la clase base {@link Edificio}.
 */
public class Piscina extends Edificio{
    /**
     * Alquiler que corresponde a esta piscina cuando un jugador cae en la propiedad.
     */
    private float alquilerPiscina;

    /**
     * Construye una piscina con identificador, alquiler, precio, solar y propietario.
     *
     * @param identificador    identificador único del edificio
     * @param alquilerPiscina  cantidad de alquiler que genera la piscina
     * @param precio           precio del edificio (coste de construcción/compra)
     * @param solar            solar al que pertenece esta piscina
     * @param jugador          propietario inicial de la piscina
     */
    public Piscina(int identificador, float alquilerPiscina, float precio, Solar solar, Jugador jugador) {
        // Delegar inicialización común a la clase padre Edificio
        super(identificador, precio, solar, jugador);
        // Almacenar el alquiler específico de la piscina
        this.alquilerPiscina = alquilerPiscina;
    }
}
