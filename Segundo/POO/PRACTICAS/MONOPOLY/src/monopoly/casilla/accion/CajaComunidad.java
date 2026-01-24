// java
package monopoly.casilla.accion;

import monopoly.Tablero;
import monopoly.excepciones.juego.jugador.NoPuedePagar;
import partida.*;
import monopoly.carta.CartaComunidad;
import java.util.ArrayList;

/**
 * Representa la casilla "Caja de Comunidad".
 * Contiene un mazo de cartas de comunidad y delega la acción a la carta correspondiente.
 */
public class CajaComunidad extends Accion {
    /**
     * Lista de cartas de comunidad disponibles en esta casilla.
     * Se mantiene en el orden en que se añadieron.
     */
    ArrayList<CartaComunidad> Cartas;


    /**
     * Construye una CajaComunidad inicializando el listado de cartas.
     *
     * @param nombre   nombre de la casilla
     * @param posicion posición en el tablero
     * @param tablero  referencia al tablero, usada para construir las cartas
     */
    public CajaComunidad(String nombre, int posicion, Tablero tablero) {

        super(nombre, posicion);
        Cartas = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Cartas.add(new CartaComunidad(i+1,tablero));
        }
    }

    /**
     * Acción que se ejecutaría cuando un jugador cae en la casilla.
     * Actualmente no realiza ninguna operación (implementación vacía).
     * No lanza excepciones en la implementación actual.
     *
     * @param jugador jugador que cae en la casilla
     */
    @Override
    public void accion(Jugador jugador){
        // Implementación intencionalmente vacía: no se realiza ninguna acción aquí.
        return;
    }

    /**
     * Ejecuta la carta indicada por el contador sobre el jugador.
     * Se delega primero al método cogerCarta de la carta y luego a su accion.
     *
     * @param contador índice de la carta a ejecutar (se usa tal cual)
     * @param jugador  jugador afectado por la carta
     * @param jugadores lista completa de jugadores (pasada a cogerCarta)
     * @throws NoPuedePagar si la carta requiere un pago que sólo puede obtenerse vendiendo/hipotecando
     */
    @Override
    public void accion(int contador, Jugador jugador, ArrayList<Jugador> jugadores) throws NoPuedePagar {
        // Se asume que el contador es válido según la lógica del programa que llama a este método.
        Cartas.get(contador).cogerCarta(jugador,jugadores);
        Cartas.get(contador).accion(jugador);
    }


    /**
     * Devuelve la lista de cartas de comunidad de esta casilla.
     *
     * @return lista modificable de cartas
     */
    public ArrayList<CartaComunidad> getCartas() {
        return Cartas;
    }
}

