// java
package monopoly.casilla.propiedad;

import monopoly.excepciones.juego.jugador.BancarrotaException;
import monopoly.excepciones.juego.jugador.NoPuedePagar;
import partida.Jugador;

/**
 * Representa una casilla de tipo Servicio.
 * El importe a pagar por aterrizar en un servicio depende del número de servicios
 * que posea el dueño y del valor de la tirada (dado).
 *
 * No se modifica la lógica original; solo se añaden comentarios/documentación.
 */
public class Servicio extends Propiedad {

    /**
     * Constructor que delega en la superclase Propiedad.
     *
     * @param nombre   nombre de la casilla
     * @param posicion posición en el tablero
     * @param valor    precio de adquisición
     * @param alquiler alquiler base (multiplicador por dado)
     * @param banca    referencia al jugador banca
     */
    public Servicio(String nombre, int posicion, float valor, float alquiler, Jugador banca) {
        super(nombre, posicion, valor, alquiler, banca);
    }

    /**
     * Calcula el valor del alquiler en función del número de servicios no hipotecados
     * que posea el dueño. El resultado devuelto es el factor por unidad del dado:
     * - 1 servicio: 4 * alquiler
     * - 2 servicios: 10 * alquiler
     *
     * El valor real a cobrar se multiplica por el valor de la tirada fuera de este método.
     *
     * @return factor base por unidad del dado
     */
    @Override
    public float Valor() {
        int numServicio = contarCasillas(getDuenho());
        if (numServicio == 1) {
            return 4 * getAlquiler(); // multiplicador para 1 servicio
        } else if (numServicio == 2) {
            return 10 * getAlquiler(); // multiplicador para 2 servicios
        }
        return 0; // en caso de no tener servicios activos
    }

    /**
     * Cuenta cuántas propiedades del jugador son instancias de Servicio y no están hipotecadas.
     *
     * @param Duenho jugador propietario cuyas propiedades se comprueban
     * @return número de servicios activos del dueño
     */
    @Override
    public int contarCasillas(Jugador Duenho) {
        int i = 0;
        // Recorremos todas las propiedades del dueño y contamos las instancias válidas
        for (Propiedad prop : Duenho.getPropiedades()) {
            if (prop instanceof Servicio && !prop.estaHipotecada()) {
                i++;
            }
        }
        return i;
    }

    /**
     * Evalúa si el jugador actual puede afrontar el pago de la casilla.
     * Si la propiedad no está hipotecada y el dueño no es la banca, se comprueba
     * si el jugador puede pagar el importe resultante de Valor() * tirada.
     * Si el dueño es la banca, se comprueba con el valor de compra.
     *
     * @param actual jugador que cae en la casilla
     * @param banca  referencia al jugador banca
     * @param tirada valor de la tirada (dado)
     * @return true si la acción puede proceder (p.ej. puede pagar o no aplica cobro)
     * @throws NoPuedePagar        si el jugador no puede pagar (delegado)
     * @throws BancarrotaException si la comprobación interna lanza bancarrota (delegado)
     */
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) throws NoPuedePagar, BancarrotaException {
        if (!estaHipotecada() && !getDuenho().esBanca()) {
            // Se usa Valor() * tirada para calcular lo que debería poder pagar el jugador
            return actual.puedePagar(Valor() * tirada);
        } else if (getDuenho().equals(banca)) {
            // Si la banca es dueña, la comprobación usa el valor de compra
            return actual.puedePagar(getValor());
        }
        // En otros casos no se aplica cobro
        return true;
    }

    /**
     * Cobra el alquiler al moroso multiplicando el valor base por la tirada.
     *
     * @param duenho  jugador que recibe el pago
     * @param moroso  jugador que debe pagar
     * @param tirada  valor de la tirada (dado)
     * @throws NoPuedePagar        si el moroso no puede pagar
     * @throws BancarrotaException si el pago causa bancarrota
     */
    @Override
    public void alquiler(Jugador duenho, Jugador moroso, int tirada) throws NoPuedePagar, BancarrotaException {
        if (evaluarCasilla(moroso, duenho, tirada)) {
            // Se aplica el pago: Valor() ya es el factor base, se multiplica por la tirada
            moroso.pagar(Valor() * tirada);
            duenho.cobrar(Valor() * tirada);
        }
    }

    /**
     * Representación para listados de venta.
     *
     * @return descripción de la casilla en formato texto
     */
    @Override
    public String casEnVenta() {
        String mensaje = "{\n\tNombre:" + this.getNombre() +
                "\n\tTipo: Servicio" +
                "\n\tValor:" + this.getValor() +
                "\n}";
        return mensaje;
    }

    /**
     * Información detallada de la casilla para mostrar al usuario.
     *
     * @return cadena con información de la casilla
     */
    @Override
    public String infoCasilla() {
        String info = "Nombre: " + getNombre() +
                "\nTipo: Servicio" +
                "\nValor: " + getValor() +
                "\nAlquiler base: " + getAlquiler() +
                "\nDueño: " + (getDuenho() != null ? getDuenho().getNombre() : "Banca") +
                "\nEstado: " + (estaHipotecada() ? "Hipotecada" : "No hipotecada");
        return info;
    }
}

