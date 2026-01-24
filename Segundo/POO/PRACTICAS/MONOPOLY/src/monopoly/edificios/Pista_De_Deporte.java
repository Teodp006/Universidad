package monopoly.edificios;
import partida.*;
import monopoly.casilla.propiedad.*;

/**
 * Representa una pista de deporte (edificio) asociada a un solar.
 * Mantiene el alquiler específico de la pista y hereda el comportamiento de {@link Edificio}.
 */
public class Pista_De_Deporte extends Edificio{
    /**
     * Alquiler que corresponde a esta pista de deporte cuando un jugador cae en la propiedad.
     */
    private float alquilerPistaDeporte;

    /**
     * Construye una pista de deporte con identificador, alquiler, precio, solar y propietario.
     *
     * @param identificador identificador único del edificio
     * @param alquilerPistaDeporte cantidad de alquiler que genera la pista
     * @param precio precio del edificio (coste de construcción/compra)
     * @param solar solar al que pertenece esta pista
     * @param jugador propietario inicial de la pista
     */
    public Pista_De_Deporte(int identificador, float alquilerPistaDeporte, float precio, Solar solar, Jugador jugador) {
        // Delegar inicialización común a la clase padre Edificio
        super(identificador, precio, solar, jugador);
        // Almacenar el alquiler específico de la pista de deporte
        this.alquilerPistaDeporte = alquilerPistaDeporte;
    }
}


