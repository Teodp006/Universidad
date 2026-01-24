// java
package monopoly.edificios;
import partida.*;
import monopoly.casilla.propiedad.*;

/**
 * Representa un hotel asociado a un solar en el tablero.
 * Mantiene el alquiler específico del hotel y hereda comportamientos de {@link Edificio}.
 */
public class Hotel extends Edificio{
    /**
     * Alquiler que corresponde a este hotel cuando un jugador cae en la propiedad.
     */
    private float alquilerHotel;

    /**
     * Construye un hotel con identificador, alquiler, precio, referencia al solar y propietario.
     *
     * @param identificador identificador único del edificio
     * @param alquilerHotel cantidad de alquiler que genera el hotel
     * @param precio        precio del hotel (coste de construcción/compra)
     * @param solar         solar al que pertenece este hotel
     * @param jugador       propietario inicial del hotel
     */
    public Hotel(int identificador, float alquilerHotel, float precio, Solar solar, Jugador jugador) {
        // Delegamos la inicialización base a la clase padre Edificio
        super(identificador, precio, solar, jugador);
        // Almacenamos el alquiler específico de este hotel
        this.alquilerHotel = alquilerHotel;
    }

}


