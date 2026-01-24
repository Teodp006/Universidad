// java
package monopoly.edificios;

import monopoly.casilla.Casilla;

import monopoly.casilla.propiedad.Solar;
import partida.*;

/**
 * Representa un edificio asociado a un solar del tablero.
 * Mantiene identificador, propietario, precio y referencia al solar correspondiente.
 * La implementación original se conserva; solo se añaden comentarios explicativos.
 */
public class Edificio {
    /**
     * Identificador único del edificio.
     */
    private int identificador;
    /**
     * Jugador propietario del edificio (puede ser null si no tiene propietario).
     */
    private Jugador propietario;
    /**
     * Precio del edificio (coste de construcción/compra).
     */
    private float precio;
    /**
     * Solar al que pertenece este edificio.
     */
    private Solar solar;

    /**
     * Constructor que inicializa los datos básicos del edificio.
     *
     * @param identificador identificador único del edificio
     * @param precio        precio del edificio
     * @param solar         solar asociado al edificio
     * @param jugador       propietario inicial (puede ser null)
     */
    public Edificio(int identificador, float precio, Solar solar, Jugador jugador) {
        // Asignación directa de los atributos con los valores proporcionados
        this.identificador = identificador;
        this.precio = precio;
        this.solar = solar;
        this.propietario = jugador;
    }

    /**
     * Devuelve el identificador del edificio.
     *
     * @return identificador entero
     */
    public int getIdentificador(){
        return identificador;
    }

    /**
     * Devuelve el propietario actual del edificio.
     *
     * @return instancia de Jugador que posee el edificio
     */
    public Jugador getPropietario() {
        return propietario;
    }

    /**
     * Devuelve el precio del edificio.
     *
     * @return precio como float
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * Devuelve el solar asociado a este edificio.
     *
     * @return referencia al Solar correspondiente
     */
    public Solar getSolar() {
        return solar;
    }

    /**
     * Representación en texto del edificio mostrando sus campos principales.
     *
     * @return cadena con información resumida del edificio
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "\nidentificador=" + identificador +
                "\npropietario=" + propietario.getNombre() +
                "\nprecio=" + precio +
                "\nsolar=" + solar.getNombre() +
                "\n}";
    }

}

