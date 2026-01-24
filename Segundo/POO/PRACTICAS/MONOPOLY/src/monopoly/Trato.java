// java
package monopoly;

import monopoly.excepciones.casilla.ErrorHipoteca;
import monopoly.casilla.propiedad.*;

import static monopoly.Juego.consolaNormal;

import monopoly.excepciones.juego.jugador.BancarrotaException;
import monopoly.excepciones.juego.jugador.NoPuedePagar;
import partida.*;

/**
 * Representa un trato entre dos jugadores: intercambio de propiedades y/o dinero.
 * Contiene la lógica para ejecutar el intercambio y gestiona comprobaciones
 * sobre dueños, disponibilidad de efectivo y soluciones con hipotecas/ventas.
 */
public class Trato {
    private String Idtrato;
    private Jugador ofertante;
    private Jugador receptor;
    private Propiedad propiedadOfertada;
    private float dineroPeticion = 0;
    private Propiedad propiedadPeticion;
    private float dineroOferta = 0;

    /**
     * Crea un trato con los elementos propuestos por el ofertante hacia el receptor.
     *
     * @param Idtrato identificador del trato
     * @param ofertante jugador que propone el trato
     * @param receptor jugador que recibe la propuesta
     * @param propiedadOfertada propiedad ofrecida por el ofertante (puede ser null)
     * @param propiedadPeticion propiedad solicitada por el ofertante (puede ser null)
     * @param dineroOferta dinero ofrecido por el ofertante (>= 0)
     * @param dineroPeticion dinero solicitado al receptor (>= 0)
     */
    public Trato(String Idtrato, Jugador ofertante, Jugador receptor, Propiedad propiedadOfertada, Propiedad propiedadPeticion, float dineroOferta, float dineroPeticion) {
        this.Idtrato = Idtrato;
        this.ofertante = ofertante;
        this.receptor = receptor;
        this.propiedadOfertada = propiedadOfertada;
        this.dineroOferta = dineroOferta;
        this.propiedadPeticion = propiedadPeticion;
        this.dineroPeticion = dineroPeticion;
    }

    /**
     * Devuelve el id del trato.
     *
     * @return identificador del trato
     */
    public String getIdtrato() {
        return Idtrato;
    }

    /**
     * Ejecuta el trato intercambiando dinero y/o propiedades entre los jugadores.
     * - Verifica que el ofertante pueda pagar la oferta en cash (o lanza la
     *   posibilidad de resolverlo con ventas/hipotecas).
     * - Transfiere propiedades si no pertenecen ya al receptor/ofertante.
     * - Verifica que el receptor pueda pagar la petición y, en caso contrario,
     *   ofrece la posibilidad de solucionarlo mediante ventas/hipotecas.
     *
     * Las situaciones de bancarrota durante las comprobaciones cancelan el trato
     * (se captura la excepción y se informa por consola).
     *
     * @throws NoPuedePagar no se propaga en la implementación actual porque se gestiona internamente,
     *                      la firma se mantiene por compatibilidad.
     */
    public void ejecutarTrato() throws NoPuedePagar {
        // Intento cobro del ofertante (dinero que ofrece), como el no es quien acepta el trato ahora, si no tiene dinero
        // no vamos a permitir que el otro lo obligue a pagar, se cancela el trato directamente.
        if (dineroOferta > 0) {
            try {
                if (ofertante.puedePagar(dineroOferta)) {
                    ofertante.sumarFortuna(-dineroOferta);
                    ofertante.sumarGastos(dineroOferta);
                    receptor.sumarFortuna(dineroOferta);
                }
            } catch (NoPuedePagar e){
                consolaNormal.imprimir("Trato cancelado: " + ofertante.getNombre() +" te ofrecía " + dineroOferta + "€ pero ahora no tiene suficiente dinero para afrontarlo.");
            }
        }

        // Transferencia de la propiedad ofrecida (si existe)
        if (propiedadOfertada != null) {
            if (propiedadOfertada.getDuenho().equals(ofertante)) {
                propiedadOfertada.tradearPropiedad(receptor);
            } else {
                consolaNormal.imprimir("Trato cancelado: la propiedad " + propiedadOfertada.getNombre() + " ya no pertence a " + ofertante.getNombre());
                return;
            }
        }

        // Transferencia de la propiedad solicitada (si existe)
        if (propiedadPeticion != null) {
            if (propiedadPeticion.getDuenho().equals(receptor)) {
                propiedadPeticion.tradearPropiedad(ofertante);
            } else {
                consolaNormal.imprimir("Trato cancelado: la propiedad " + propiedadPeticion.getNombre() + " ya no pertence a " + receptor.getNombre());
                return;
            }
        }

        // Pago por parte del receptor (dinero que solicita el ofertante). Aquí, como es quien acepta puede decidir apalancarse
        if (dineroPeticion > 0) {
            try {
                if (receptor.puedePagar(dineroPeticion)) {
                    ofertante.sumarFortuna(dineroPeticion);
                    receptor.sumarFortuna(-dineroPeticion);
                    receptor.sumarGastos(dineroPeticion);
                }
            } catch (BancarrotaException e) {
                consolaNormal.imprimir("Trato cancelado porque si no entrarías en bancarrota y perderías la partida.");
                return;
            } catch (NoPuedePagar e) {
                consolaNormal.imprimir(receptor.getNombre() + " estás a punto de acometer un trato para el que no tienes dinero, tendrás que obtener cash de tus propiedades ¿ Seguro que quieres continuar (Si/No) ?.");
                try {
                    // quien no puede pagar es el receptor; intentamos solucionar su deuda
                    e.solucionarDuedas(receptor, ofertante, dineroPeticion);
                } catch (ErrorHipoteca ex) {
                    consolaNormal.imprimir(ex.getMessage());
                }
                return;
            }
        }
    }

    @Override
    public String toString() {
        String mensaje = new String();
        mensaje = "{\nTrato ID: " + Idtrato +
                "\n\t Jugador Propone: " + ofertante.getNombre() +
                "\n\t trato: cambiar (";
        if (propiedadOfertada != null) {
            mensaje += propiedadOfertada.getNombre() + ((dineroOferta > 0) ? " y " : "");
        }
        if (dineroOferta > 0) {
            mensaje += dineroOferta;
        }
        mensaje += ", ";
        if (propiedadPeticion != null) {
            mensaje += propiedadPeticion.getNombre() + ((dineroPeticion > 0) ? " y " : "");
        }
        if (dineroPeticion > 0) {
            mensaje += dineroPeticion;
        }
        mensaje += ")\n}";
        return mensaje;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Trato trato = (Trato) obj;
        return Idtrato.equals(trato.Idtrato);
    }
}



