// java
package monopoly.casilla;

import monopoly.Valor;
import partida.Avatar;

import java.util.ArrayList;

/**
 * Casilla especial del tablero (por ejemplo Salida o Cárcel).
 * Mantiene la funcionalidad original: solo se añaden comentarios explicativos.
 */
public class Especial extends Casilla {

    /**
     * Construye una casilla especial con nombre y posición.
     *
     * @param nombre   nombre visible de la casilla (p. ej. "Salida" o "Carcel")
     * @param posicion posición en el tablero
     */
    public Especial(String nombre, int posicion) {
        super(nombre,posicion);
    }

    /**
     * Devuelve información textual específica para la casilla especial.
     * - Para "Salida" muestra el valor que se suma al pasar por ella.
     * - Para "Carcel" muestra el impuesto para salir y la lista de jugadores encarcelados.
     *
     * @return representación en texto de la casilla especial
     */
    @Override
    public String infoCasilla() {
        String mensaje = new String();
        switch (this.getNombre()) {
            case "Salida":
                // En la salida mostramos el valor que se añade al pasar por ella
                mensaje = "{\n\tValor al pasar:" + Valor.SUMA_VUELTA +
                        "\n}";
                break;
            case "Carcel":
                // Para la cárcel imprimimos el impuesto para salir y los jugadores en ella
                mensaje = "{\n\tSalir: " + Valor.IMPUESTO_CARCEL +
                        "\n\tJugadores: " + this.ListaJugadores() +
                        "\n}";
                break;
            default:
                // Si no es una casilla esperada, devolvemos una representación vacía
                mensaje = "{\n\tInformación no disponible\n}";
                break;
        }
        return mensaje;
    }

    /**
     * Construye una representación de los jugadores presentes en la casilla.
     * Solo está implementado para la casilla "Carcel" (devuelve nombre y tiradas en cárcel).
     *
     * @return cadena con la lista de jugadores en la cárcel
     * @throws IllegalArgumentException si se intenta usar para otra casilla distinta de "Carcel"
     */
    public String ListaJugadores() {
        String mensaje = new String(); // Reservamos memoria para el String
        if (this.getNombre().equals("Carcel")) {
            // Para cada avatar en la casilla añadimos su nombre y tiradas en la cárcel
            for (Avatar avatar : getAvatares()) {
                mensaje = mensaje.concat("[" + avatar.getJugador().getNombre() + "," + avatar.getJugador().getTiradasCarcel() + "]");
            }
        } else {
            // Lanzamos excepción si se solicita la lista para una casilla no soportada
            throw new IllegalArgumentException("No se puede imprimir info casilla de IrCarcel o Salida");
        }
        return mensaje;
    }
}

