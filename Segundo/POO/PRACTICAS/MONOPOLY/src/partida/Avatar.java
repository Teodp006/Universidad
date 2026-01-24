// java
package partida;

import monopoly.*;
import monopoly.casilla.*;
import monopoly.casilla.propiedad.*;
import monopoly.casilla.accion.*;
import monopoly.excepciones.juego.jugador.BancarrotaException;
import monopoly.excepciones.juego.jugador.NoPuedePagar;

import static monopoly.Juego.consolaNormal;

import java.util.ArrayList;
import java.util.Random;

/**
 * Representa el avatar de un jugador en el tablero.
 * Mantiene su identificador, tipo, referencia al jugador propietario,
 * la casilla donde se encuentra y ayuda a moverlo por el tablero.
 */
public class Avatar {

    // Atributos
    private String id; // Identificador: una letra generada aleatoriamente.
    private String tipo; // Sombrero, Esfinge, Pelota, Coche
    private Jugador jugador; // Un jugador al que pertenece ese avatar.
    private Casilla lugar; // Los avatares se sitúan en Casillas del tablero.
    private int valor;

    /**
     * Constructor vacío. No inicializa campos.
     */
    public Avatar() {
    }

    /**
     * Constructor principal.
     *
     * @param tipo      tipo del avatar (por ejemplo "sombrero")
     * @param jugador   jugador al que pertenece
     * @param lugar     casilla inicial donde se coloca el avatar
     * @param avCreados lista de avatares ya creados (se usa para generar un id único)
     */
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        this.jugador = jugador;
        this.lugar = lugar;
        lugar.anhadirAvatar(this);
        this.tipo = tipo;
        if (avCreados == null) {
            avCreados = new ArrayList<Avatar>();
        }
        avCreados.add(this);
        generarId(avCreados);
    }

    // Getters y setters

    /**
     * @return id del avatar (letra)
     */
    public String getId() {
        return id;
    }

    /**
     * @return jugador propietario del avatar
     */
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * @return tipo del avatar
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @return casilla donde está el avatar
     */
    public Casilla getLugar() {
        return lugar;
    }

    /**
     * Establece el identificador del avatar.
     *
     * @param id nuevo id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Establece el jugador propietario.
     *
     * @param jugador jugador a asociar
     */
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    /**
     * Establece el tipo de avatar.
     *
     * @param tipo tipo a asignar
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Establece la casilla actual del avatar.
     *
     * @param lugar casilla destino
     */
    public void setLugar(Casilla lugar) {
        this.lugar = lugar;
    }

    /**
     * Mueve el avatar a una nueva posición calculada a partir de la tirada.
     * Actualiza la posición, imprime mensajes y aplica efectos de la casilla.
     *
     * @param casillas    estructura de casillas del tablero (ArrayList de ArrayList)
     * @param valorTirada número de casillas a desplazar (positivo o negativo)
     * @throws NoPuedePagar       si durante la acción se detecta que el jugador puede cubrir con activos pero no con cash
     * @throws BancarrotaException si el jugador entra en bancarrota durante la acción
     */
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) throws NoPuedePagar, BancarrotaException {
        /* Buscamos la localizacion del lugar donde está nuestro Avatar, será un casillas[p1][p2] */
        int posicion = this.getLugar().getPosicion();
        Casilla origen = this.getLugar();
        // Eliminamos el avatar de la casilla actual para moverlo
        origen.getAvatares().remove(this);
        if (posicion + valorTirada >= 40) { // Compruebo que no dé una vuelta extra al tablero
            posicion = (posicion + valorTirada) - (Valor.NUM_CASILLAS);
            if (origen.getPosicion() != 40) {
                if (posicion == 0) { // Si queda 0 significa que estamos en la casilla 40 (Salida)
                    posicion = 40;
                }
                // Excepto en la primera jugada, al pasar por Salida se suma la recompensa
                if (this.getJugador().getVueltas() >= 0) {
                    this.getJugador().sumarFortuna(Valor.SUMA_VUELTA);
                }
                this.getJugador().setVueltas(this.getJugador().getVueltas() + 1);
            } else if (this.getJugador().getVueltas() == -1) {
                this.getJugador().setVueltas(this.getJugador().getVueltas() + 1);
            }
        } else { // Si no, simplemente sumamos las casillas
            posicion += valorTirada;
        }

        // Buscamos la casilla destino por posición y la actualizamos
        Casilla destino = this.buscarCasillaPorPosicion(casillas, posicion);
        // Añadimos el avatar a la casilla destino
        destino.getAvatares().add(this);
        destino.incrementarVisita();

        // Aplicamos efectos y mensajes según el tipo de casilla
        if (destino.getNombre().equals("IrCarcel")) {
            this.getJugador().encarcelar(casillas);
            destino.eliminarAvatar(this);
            consolaNormal.imprimir("El jugador " + this.getJugador().getNombre() + " ha avanzado " + valorTirada + " posiciones desde la casilla " + origen.getNombre() + " hasta la casilla IrCarcel. El avatar se coloca en la casilla de Cárcel");
            return;
        } else if (destino instanceof Accion) {
            consolaNormal.imprimir("El avatar " + this.getId() + " avanza " + valorTirada + " posiciones, desde la casilla " + origen.getNombre() + " hasta la casilla " + destino.getNombre() + ". ");
            if (destino instanceof Parking) {
                consolaNormal.imprimir("El jugador " + this.getJugador().getNombre() +
                        ((((Parking) destino).getValor() > 0) ? " recibe " + ((Parking) destino).getValor() + "€" :
                                " no recibe nada"));
            }
        } else if (destino instanceof Impuesto) {
            Impuesto imp = (Impuesto) destino;
            consolaNormal.imprimir("El avatar " + this.getId() + " avanza " + valorTirada + " posiciones, desde la casilla " + origen.getNombre() + " hasta la casilla " + imp.getNombre() + ". El jugador " + this.getJugador().getNombre() + " paga " + imp.getImpuesto() + "€ de impuesto que se depositan en el Parking.");
            if (this.jugador.puedePagar(imp.getImpuesto())) {
                imp.pagarImpuesto(this.jugador);
                // La casilla del Parking es la última del lado Oeste
                Parking parking = (Parking) casillas.get(1).get(casillas.get(1).size() - 1);
                parking.sumarValor(imp.getImpuesto());
            }
        } else if (destino instanceof Propiedad) {
            Propiedad prop = (Propiedad) destino;
            if (!prop.getDuenho().esBanca() && !prop.getDuenho().equals(this.getJugador())) {
                if (!prop.estaHipotecada()) {
                    if (prop.evaluarCasilla(this.getJugador(), prop.getDuenho(), 0)) {
                        if (prop instanceof Solar) {
                            consolaNormal.imprimir("El avatar " + this.getId() + " avanza " + valorTirada + " posiciones, desde la casilla " + origen.getNombre() + " hasta la casilla " + prop.getNombre() + ". Se han pagado " + prop.Valor() + "€ de alquiler al jugador " + prop.getDuenho().getNombre() + ".");
                        } else if (prop instanceof Transporte) {
                            consolaNormal.imprimir("El avatar " + this.getId() + " avanza " + valorTirada + " posiciones, desde la casilla " + origen.getNombre() + " hasta la casilla " + prop.getNombre() + ". Se han pagado " + prop.Valor() + "€ de alquiler al jugador " + prop.getDuenho().getNombre() + ".");
                        } else if (prop instanceof Servicio) {
                            consolaNormal.imprimir("El avatar " + this.getId() + " avanza " + valorTirada + " posiciones, desde la casilla " + origen.getNombre() + " hasta la casilla " + prop.getNombre() + ". Se han pagado " + (valorTirada * prop.Valor()) + "€ de alquiler al jugador " + prop.getDuenho().getNombre() + ".");
                        }
                    }
                    prop.alquiler(prop.getDuenho(), this.jugador, valorTirada);
                } else {
                    consolaNormal.imprimir("El avatar " + this.getId() + " avanza " + valorTirada + " posiciones, desde la casilla " + origen.getNombre() + " hasta la casilla " + prop.getNombre() + ". La casilla está hipotecada, por lo que no se paga alquiler.");
                }
            } else {
                consolaNormal.imprimir("El avatar " + this.getId() + " avanza " + valorTirada + " posiciones, desde la casilla " + origen.getNombre() + " hasta la casilla " + destino.getNombre() + ".");
            }
        }

        // Colocamos al avatar en la casilla destino
        this.setLugar(destino);
    }

    /**
     * Genera un id único (letra mayúscula) para el avatar evitando colisiones con avatares existentes.
     *
     * @param avCreados lista de avatares ya creados
     */
    private void generarId(ArrayList<Avatar> avCreados) {
        char i;
        Random rnd = new Random();
        do {
            i = (char) ('A' + rnd.nextInt(26));
        } while (existeId(avCreados, i));
        // Usar el último elemento añadido en la lista
        avCreados.get(avCreados.size() - 1).setId(String.valueOf(i));
        consolaNormal.imprimir("Id del Avatar: " + getId());
    }

    /**
     * Comprueba si la letra dada ya está usada por otro avatar en la lista.
     *
     * @param avCreados lista de avatares existentes
     * @param i         letra a comprobar
     * @return true si ya existe, false en caso contrario
     */
    private boolean existeId(ArrayList<Avatar> avCreados, char i) {
        if (avCreados.size() > 1) {
            for (Avatar av : avCreados) {
                if (av != avCreados.get(avCreados.size() - 1) && av.getId() != null && av.getId().equals(String.valueOf(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Devuelve el id del avatar como representación textual.
     *
     * @return id del avatar
     */
    @Override
    public String toString() {
        return id;
    }

    /**
     * Busca la casilla correspondiente a una posición numérica en la estructura de casillas.
     *
     * @param casillas      estructura del tablero
     * @param posiciondestino posición buscada (1..40)
     * @return casilla encontrada o null si no existe
     */
    private Casilla buscarCasillaPorPosicion(ArrayList<ArrayList<Casilla>> casillas, int posiciondestino) {
        int i = 0;
        for (ArrayList<Casilla> lado : casillas) {
            for (Casilla casilla : lado) {
                i++;
                if (i == posiciondestino) {
                    return casilla;
                }
            }
        }
        return null;
    }
}



