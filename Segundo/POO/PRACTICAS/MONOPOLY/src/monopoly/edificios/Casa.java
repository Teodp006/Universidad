// java
package monopoly.edificios;

import partida.*;
import monopoly.casilla.propiedad.*;

/**
 * Representa una casa (edificio) asociada a un solar.
 * Mantiene el valor de alquiler específico de la casa y hereda el resto de comportamiento
 * de la clase base {@link Edificio}.
 */
public class Casa extends Edificio{
    /**
     * Alquiler que corresponde a esta casa cuando un jugador cae en la propiedad.
     */
    private float alquilerCasa;

    /**
     * Construye una casa con identificador, precio, alquiler y referencia al solar y propietario.
     *
     * @param identificador identificador único del edificio
     * @param alquilerCasa  cantidad de alquiler que genera la casa
     * @param precio        precio de la casa (coste de construcción/compra)
     * @param solar         solar al que pertenece esta casa
     * @param jugador       propietario inicial de la casa
     */
    public Casa(int identificador, float alquilerCasa, float precio, Solar solar, Jugador jugador) {
        // Delegamos la inicialización base a la clase padre Edificio
        super(identificador, precio, solar, jugador);
        // Almacenamos el alquiler específico de esta casa
        this.alquilerCasa = alquilerCasa;
    }

}


