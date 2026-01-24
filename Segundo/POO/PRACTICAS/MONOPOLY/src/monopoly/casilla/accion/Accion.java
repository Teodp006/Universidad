// java
package monopoly.casilla.accion;

import monopoly.casilla.Casilla;
import monopoly.excepciones.juego.jugador.BancarrotaException;
import monopoly.excepciones.juego.jugador.NoPuedePagar;
import partida.*;
import java.util.ArrayList;

/**
 * Casilla de tipo Acción del tablero.
 * Las casillas acción aplican efectos cuando un avatar cae en ellas;
 * esta clase define la interfaz (métodos abstractos) que deben implementar
 * las acciones concretas.
 */
public abstract class Accion extends Casilla {

    /**
     * Construye una casilla de acción con nombre y posición.
     *
     * @param nombre   nombre de la casilla
     * @param posicion posición numérica en el tablero
     */
    public Accion(String nombre, int posicion) {
        super(nombre, posicion);
    }

    /**
     * Ejecuta la acción asociada a la casilla cuando se procesa por contador
     * (por ejemplo, durante la lectura de cartas/sorpresas que afectan a varios jugadores).
     *
     * @param contador contador o identificador de la acción a ejecutar
     * @param jugador  jugador que originó la acción
     * @param jugadores lista de jugadores de la partida (puede usarse para afectar a otros)
     * @throws NoPuedePagar si la acción requiere un pago que el jugador podría cubrir sólo vendiendo/hipotecando
     */
    public abstract void accion(int contador, Jugador jugador, ArrayList<Jugador> jugadores) throws NoPuedePagar;

    /**
     * Ejecuta la acción asociada a la casilla para un único jugador.
     *
     * @param jugador jugador sobre el que se aplica la acción
     * @throws NoPuedePagar      si la acción requiere un pago que el jugador podría cubrir sólo vendiendo/hipotecando
     * @throws BancarrotaException si la acción provoca que el jugador entre en bancarrota
     */
    public abstract void accion(Jugador jugador) throws NoPuedePagar, BancarrotaException;

    /**
     * Proporciona información textual de la casilla.
     * Las subclases pueden sobreescribir para devolver detalles específicos.
     *
     * @return cadena con información sobre la casilla (por defecto vacía)
     */
    @Override
    public String infoCasilla() {
        String mensaje = new String();
        return mensaje;
    }
}

