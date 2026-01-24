// java
package monopoly.casilla.accion;
import monopoly.Tablero;
import monopoly.carta.*;

import java.util.ArrayList;

import monopoly.excepciones.juego.jugador.NoPuedePagar;
import partida.*;

/**
 * Casilla de tipo "Suerte".
 * Contiene un mazo de cartas de suerte y delega la ejecución a la carta correspondiente.
 * La lógica original se mantiene: solo se añaden comentarios explicativos.
 */
public class Suerte extends Accion {
    /**
     * Lista de cartas de suerte disponibles en esta casilla.
     * Se mantiene en el orden en que se añaden.
     */
    ArrayList<CartaSuerte> Cartas;


    /**
     * Construye la casilla Suerte e inicializa el mazo de cartas.
     *
     * @param nombre   nombre de la casilla
     * @param posicion posición en el tablero
     * @param tablero  referencia al tablero usada para construir las cartas
     */
    public Suerte(String nombre, int posicion, Tablero tablero) {
        super(nombre, posicion);
        Cartas = new ArrayList<>();
        // Se crean 7 cartas de suerte con identificadores 1..7
        for (int i = 0; i < 7; i++) {
            Cartas.add(new CartaSuerte(i+1,tablero));
        }
    }

    /**
     * Ejecuta la carta indicada por el contador.
     * Se delega primero a cogerCarta (que puede afectar a varios jugadores)
     * y después se ejecuta la acción de la carta sobre el jugador.
     *
     * @param contador índice de la carta a usar (se usa tal cual)
     * @param jugador  jugador sobre el que se aplica la carta
     * @param jugadores lista completa de jugadores (necesaria para algunas cartas)
     * @throws NoPuedePagar si la carta requiere un pago que el jugador solo podría cubrir vendiendo/hipotecando
     */
    @Override
    public void accion(int contador, Jugador jugador, ArrayList<Jugador> jugadores) throws NoPuedePagar {
        // Se asume que el caller proporciona un contador válido según la lógica del programa.
        (Cartas.get(contador)).cogerCarta(jugador, jugadores);
        Cartas.get(contador).accion(jugador);
    }

    /**
     * Acción cuando un único jugador cae en la casilla sin especificar carta.
     * Implementación intencionalmente vacía: la lógica de cartas se maneja en la versión con contador.
     *
     * @param jugador jugador que cae en la casilla
     */
    @Override
    public void accion(Jugador jugador) {
        // No hacer nada aquí; mantenido para compatibilidad con la interfaz Accion.
    }


    /**
     * Devuelve la lista de cartas de suerte de esta casilla.
     *
     * @return lista modificable de cartas
     */
    public ArrayList<CartaSuerte> getCartas() {
        return Cartas;
    }
}

