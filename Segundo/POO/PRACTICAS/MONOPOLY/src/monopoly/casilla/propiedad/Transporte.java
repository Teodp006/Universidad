// java
package monopoly.casilla.propiedad;

import monopoly.excepciones.juego.jugador.BancarrotaException;
import monopoly.excepciones.juego.jugador.NoPuedePagar;
import partida.Jugador;

/**
 * Representa una propiedad de tipo Transporte.
 * El valor del alquiler depende de cuántos transportes sin hipoteca posea el dueño.
 */
public class Transporte extends Propiedad {

    /**
     * Construye un transporte con nombre, posición, valor, alquiler y referencia a la banca.
     *
     * @param nombre   nombre de la casilla
     * @param posicion posición en el tablero
     * @param valor    precio de adquisición
     * @param alquiler alquiler base por transporte
     * @param banca    referencia al jugador banca
     */
    public Transporte(String nombre, int posicion, float valor, float alquiler, Jugador banca) {
        super(nombre, posicion, valor, alquiler, banca);
    }

    /**
     * Calcula el importe a pagar por alquiler en función del número de transportes no hipotecados
     * que posea el dueño.
     *
     * @return importe de alquiler a pagar
     */
    @Override
    public float Valor() {
        // multiplicamos el alquiler base por el número de transportes del dueño
        return contarCasillas(getDuenho()) * getAlquiler();
    }

    /**
     * Cuenta cuántas propiedades del dueño son instancias de Transporte y no están hipotecadas.
     *
     * @param Duenho jugador propietario cuyas propiedades se recorren
     * @return número de transportes activos del dueño
     */
    @Override
    public int contarCasillas(Jugador Duenho) {
        int i = 0;
        // Recorremos todas las propiedades del dueño
        for (Propiedad prop : Duenho.getPropiedades()) {
            // Contar solo si es Transporte y no está hipotecada
            if (prop instanceof Transporte && !prop.estaHipotecada()) {
                i++;
            }
        }
        return i;
    }

    /**
     * Intenta cobrar el alquiler al jugador moroso si la casilla corresponde.
     * Lanza excepciones definidas por las reglas del juego si procede.
     *
     * @param duenho  jugador que recibe el pago
     * @param moroso  jugador que debe pagar
     * @param tirada  valor de la tirada (no usado en esta implementación)
     * @throws NoPuedePagar        si el moroso no puede pagar
     * @throws BancarrotaException si el pago causa bancarrota
     */
    @Override
    public void alquiler(Jugador duenho, Jugador moroso, int tirada) throws NoPuedePagar, BancarrotaException {
        if (evaluarCasilla(moroso, duenho, tirada)) {
            moroso.pagar(Valor());
            duenho.cobrar(Valor());
        }
    }

    /**
     * Evalúa si, en esta casilla, debe intentarse cobrar alquiler o permitir compra.
     *
     * @param actual jugador que cae en la casilla
     * @param banca  referencia al jugador banca
     * @param tirada valor de la tirada (no usado directamente)
     * @return true si se puede proceder sin impedir la acción; false si no puede pagar
     * @throws NoPuedePagar        delegada desde comprobaciones internas
     * @throws BancarrotaException delegada desde comprobaciones internas
     */
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) throws NoPuedePagar, BancarrotaException {
        // Si la propiedad no está hipotecada y el dueño no es la banca, se evalúa si el jugador puede pagar el alquiler
        if (!estaHipotecada() && !getDuenho().esBanca()) {
            return actual.puedePagar(Valor());
        } else if (getDuenho().equals(banca)) {
            // Si el dueño es la banca, se usa el valor de compra para comprobar si puede pagar
            return actual.puedePagar(getValor());
        }
        // En otros casos devolvemos true por defecto (no se cobra o no aplica)
        return true;
    }

    // Método auxiliar que existía originalmente y se mantiene sin alterar.
    public void pagarAlquiler(Jugador du) {
        du.pagar(Valor());
        getDuenho().sumarFortuna(Valor());
    }

    /**
     * Devuelve una cadena con información resumida de la casilla para su venta.
     *
     * @return descripción en formato texto
     */
    @Override
    public String casEnVenta() {
        String mensaje = "{\n\tNombre:" + this.getNombre() +
                "\n\tTipo: Transporte" +
                "\n\tValor: " + this.getValor() +
                "\n\tAlquiler base: " + this.getAlquiler() +
                "\n}";
        return mensaje;
    }

    /**
     * Devuelve información detallada de la casilla, incluyendo propietario si lo hay.
     *
     * @return descripción en formato texto
     */
    @Override
    public String infoCasilla() {
        if (!this.getDuenho().getNombre().equals("banca")) {
            String mensaje = "{\n\tTipo: Transporte" +
                    "\n\tPropietario:" + this.getDuenho().getNombre() +
                    "\n\tValor:" + this.getValor() +
                    "\n\tAlquiler:" + this.getAlquiler() +
                    "\n}";
            return mensaje;
        } else {
            String mensaje = "{\n\tTipo: Transporte" +
                    "\n\tValor:" + this.getValor() +
                    "\n\tAlquiler:" + this.getAlquiler() +
                    "\n}";
            return mensaje;
        }
    }
}

