// java
package monopoly.casilla.accion;

import monopoly.ConsolaNormal;
import partida.Jugador;
import partida.Avatar;

import java.util.ArrayList;

import static monopoly.Juego.consolaNormal;

/**
 * Casilla especial "Parking" que acumula un bote (\`valor\`) y lo entrega
 * al jugador que cae en la casilla.
 *
 * No se modifica la lógica original: solo se añaden comentarios explicativos.
 */
public class Parking extends Accion {
    /**
     * Dinero acumulado en el parking (bote).
     */
    private float valor; // Dinero acumulado en el parking

    /**
     * Construye la casilla Parking con nombre y posición, inicializando el bote a 0.
     *
     * @param nombre  nombre de la casilla
     * @param posicion posición en el tablero
     */
    public Parking(String nombre, int posicion) {
        super(nombre, posicion);
        valor = 0;
    }

    /**
     * Obtiene el valor actual del bote.
     *
     * @return cantidad acumulada en el parking
     */
    public float getValor() {
        return valor;
    }

    /**
     * Establece el valor del bote.
     * Método privado usado internamente para centralizar la asignación.
     *
     * @param valor nueva cantidad del bote
     */
    private void setValor(float valor) {
        this.valor = valor;
    }

    /**
     * Vacía el bote poniendo su valor a cero.
     * Se usa cuando un jugador cobra el bote.
     */
    public void vaciar() {
        setValor(0);
    }

    /**
     * Suma una cantidad al bote acumulado.
     * Método de conveniencia que mantiene la encapsulación del campo.
     *
     * @param valor cantidad a añadir al bote
     */
    public void sumarValor(float valor) {;
        setValor(getValor() + valor);
    }

    /**
     * Información textual de la casilla, mostrando el bote y los jugadores presentes.
     *
     * @return representación en texto de la casilla Parking
     */
    @Override
    public String infoCasilla() {
        String mensaje = new String();
        mensaje = "{\n\tBote: " + this.getValor() +
                "\n\tJugadores: " + this.ListaJugadores() +
                "\n}";
        return mensaje;
    }

    /**
     * Método auxiliar para listar los jugadores (avatares) presentes en la casilla.
     * Itera sobre la lista de avatares y concatena sus nombres.
     *
     * @return cadena con la lista de jugadores en el parking
     */
    public String ListaJugadores(){//Método para listar los jugadores en casillas especiales (Cárcel y Parking).
        String mensaje = new String();
        mensaje = mensaje.concat("[");
        for (Avatar avatar : getAvatares()) { //Concatenamos el mensaje con los jugadores en el parking
            mensaje = mensaje.concat(avatar.getJugador().getNombre() + ", ");
        }
        mensaje = mensaje.concat("]");

        return mensaje;
    }

    /**
     * Acción ejecutada cuando un jugador cae en Parking:
     * - Se le entrega la cantidad acumulada en el bote.
     * - Se registra el premio en el jugador mediante sumarpremiosinversionesbote.
     * - Se vacía el bote.
     *
     * @param jugador jugador que cae en la casilla y cobra el bote
     */
    @Override
    public void accion(Jugador jugador) {
        float cantidad = this.getValor();
        jugador.sumarFortuna(cantidad);
        jugador.sumarpremiosinversionesbote(cantidad);
        this.vaciar();
    }

    /**
     * Versión con contador y lista de jugadores: delega a la implementación sin contador.
     *
     * @param contador índice/contador (no usado)
     * @param jugador  jugador afectado
     * @param jugadores lista de jugadores (no usada)
     */
    @Override
    public void accion(int contador, Jugador jugador , ArrayList<Jugador> jugadores) {
        accion(jugador);
    }

}

