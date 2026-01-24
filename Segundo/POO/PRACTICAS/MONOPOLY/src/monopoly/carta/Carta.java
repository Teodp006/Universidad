// java
package monopoly.carta;

import monopoly.Tablero;
import monopoly.excepciones.juego.jugador.BancarrotaException;
import monopoly.excepciones.juego.jugador.NoPuedePagar;
import partida.Jugador;
import java.util.ArrayList;

/**
 * Clase base para cartas del juego.
 * Cada carta puede conservar el jugador que la coge, una referencia al tablero,
 * una representación para impresión y la lista de jugadores de la partida.
 */
public abstract class Carta {
    private Jugador cogeCarta;
    private Tablero tablero;
    private String impresion;
    private ArrayList<Jugador> jugadores;

    /**
     * Crea una carta asociada a un tablero.
     *
     * @param tablero referencia al tablero del juego
     */
    public Carta(Tablero tablero) {
        this.tablero = tablero;
    }

    /**
     * Devuelve el jugador que ha cogido la carta.
     *
     * @return jugador que cogió la carta, o {@code null} si no corresponde
     */
    public Jugador getJugador() {
        return cogeCarta;
    }

    /**
     * Devuelve la cadena de impresión asociada a la carta.
     *
     * @return cadena para impresión
     */
    public String getImpresion() {
        return impresion;
    }

    /**
     * Establece la cadena de impresión de la carta.
     *
     * @param impresion cadena que describe la carta para mostrar por consola
     */
    public void setImpresion(String impresion) {
        this.impresion = impresion;
    }

    /**
     * Devuelve la lista de jugadores de la partida.
     *
     * @return lista de jugadores
     */
    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    /**
     * Se invoca cuando un jugador coge la carta: registra el jugador y la lista
     * de jugadores de la partida.
     *
     * @param jugador jugador que coge la carta
     * @param jugadoresPartida lista de jugadores de la partida
     */
    public void cogerCarta(Jugador jugador, ArrayList<Jugador> jugadoresPartida) {
        this.cogeCarta = jugador;
        this.jugadores = jugadoresPartida;
    }

    /**
     * Devuelve el tablero asociado a la carta.
     *
     * @return tablero del juego
     */
    public Tablero getTablero() {
        return tablero;
    }

    /**
     * Acción que realiza la carta sobre un jugador.
     * Implementaciones concretas deben definir el comportamiento.
     *
     * @param jugador jugador sobre el que actúa la carta
     * @throws NoPuedePagar si el jugador no puede pagar una cantidad requerida
     * @throws BancarrotaException si la acción provoca bancarrota del jugador
     */
    public abstract void accion(Jugador jugador) throws NoPuedePagar, BancarrotaException;
}
