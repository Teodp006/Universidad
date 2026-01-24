// java
package monopoly.casilla;

import partida.Jugador;

/**
 * Representa una casilla de impuesto en el tablero.
 * Contiene la cantidad fija que debe pagar el jugador al caer en ella.
 */
public class Impuesto extends Casilla {
    /**
     * Cantidad que debe pagar el jugador al caer en esta casilla.
     */
    private float impuesto;

    /**
     * Construye una casilla de impuesto con nombre, posición y cantidad a pagar.
     *
     * @param nombre   nombre visible de la casilla
     * @param posicion posición en el tablero
     * @param impuesto cantidad fija a pagar al caer en la casilla
     */
    public Impuesto(String nombre, int posicion, float impuesto) {
        super(nombre, posicion);
        this.impuesto = impuesto;
    }

    /**
     * Devuelve la cantidad de impuesto asociada a esta casilla.
     *
     * @return importe del impuesto
     */
    public float getImpuesto() {
        return impuesto;
    }

    /**
     * Información textual de la casilla, mostrando el tipo y el importe a pagar.
     *
     * @return representación en texto de la casilla
     */
    @Override
    public String infoCasilla() {
        String mensaje = new String();
        mensaje = "{\n\tTipo: " + this.getClass() +
                "\n\tA pagar: " + this.getImpuesto() +
                "\n}";
        return mensaje;
    }

    /**
     * Realiza el pago del impuesto por parte del jugador:
     * - Resta la cantidad de la fortuna del jugador.
     * - Registra el gasto en las estadísticas del jugador.
     * - Suma el importe a la métrica de impuestos pagados por el jugador.
     *
     * @param jugador jugador que debe pagar el impuesto
     */
    public void pagarImpuesto(Jugador jugador) {
        // Restar la cantidad de la fortuna del jugador (positivo -> resta)
        jugador.sumarFortuna(-impuesto);
        // Registrar el gasto total realizado por el jugador
        jugador.sumarGastos(impuesto);
        // Contabilizar impuestos pagados por el jugador
        jugador.sumarimpuestos(impuesto);
    }

}
