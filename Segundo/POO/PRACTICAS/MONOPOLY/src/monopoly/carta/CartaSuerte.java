// java
package monopoly.carta;

import monopoly.Valor;

import monopoly.casilla.propiedad.Propiedad;
import monopoly.excepciones.casilla.ErrorHipoteca;
import monopoly.excepciones.juego.jugador.BancarrotaException;
import monopoly.excepciones.juego.jugador.NoPuedePagar;
import partida.*;
import monopoly.Tablero;

import static monopoly.Juego.consolaNormal;

/**
 * Representa una carta de tipo \"Suerte\" dentro del juego.
 * Cada instancia tiene un identificador {@code suerte} que determina
 * la acción que se aplica cuando la carta se ejecuta sobre un jugador.
 * La clase extiende {@link Carta} y utiliza el {@link Tablero} asociado
 * para mover avatares y consultar posiciones.
 */
public class CartaSuerte extends Carta {

    private int suerte;

    /**
     * Crea una carta de suerte asociada al tablero y con un tipo concreto.
     *
     * @param suerte identificador del tipo de carta de suerte (valores soportados en el switch)
     * @param tablero referencia al tablero del juego
     */
    public CartaSuerte(int suerte, Tablero tablero) {
        super(tablero);
        switch(suerte){
            case 1:
                setImpresion("Decides hacer un viaje de placer. Avanza hasta Solar19. Si pasas por la casilla de salida, combra " + Valor.SUMA_VUELTA + "€");
                this.suerte = suerte;
                break;
            case 2:
                setImpresion("Los acreedores te persiguen por impago. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar los 2.000.000€");
                this.suerte = suerte;
                break;
            case 3:
                setImpresion("¡Has ganado el bote de la loteria! Recibe 1.000.000€");
                this.suerte = suerte;
                break;
            case 4:
                setImpresion("Has sido elegido presidente de la junta directiva. Paga a cada jugador 250.000€");
                this.suerte = suerte;
                break;
            case 5:
                setImpresion("¡Hora punta de tráfico! Retrocede tres casillas.");
                this.suerte = suerte;
                break;
            case 6:
                setImpresion("Te multan por usar el móvil mientras conduces. Paga 150.000€");
                this.suerte = suerte;
                break;
            case 7:
                setImpresion("Avanza hasta la casilla de transporte mas cercana. Si no tiene dueño, puedes comprarla. Si tiene dueño, paga al dueño el doble de la operación indicada");
                this.suerte = suerte;
                break;
            default:
                break;

        }
    }

    /**
     * Ejecuta la acción asociada a esta carta de suerte sobre el jugador indicado.
     * Las acciones incluyen movimientos del avatar, pagos, encarcelamiento y cambios
     * en la fortuna según el valor de {@code suerte}.
     *
     * @param jugador jugador al que se aplica la carta
     * @throws NoPuedePagar si la acción requiere un pago que el jugador no puede afrontar
     * @throws BancarrotaException si la acción provoca la bancarrota del jugador
     */
    @Override
    public void accion(Jugador jugador) throws NoPuedePagar, BancarrotaException {
        switch (this.suerte) {
            case 1:
                consolaNormal.imprimir(getImpresion());
                if (33 - getJugador().getAvatar().getLugar().getPosicion() < 0) {
                    // ya se ha avanzado mas alla de la casilla 19 por lo que hay q dar una vuelta entera
                    getJugador().getAvatar().moverAvatar(getTablero().getPosiciones(), 32 - getJugador().getAvatar().getLugar().getPosicion() + 40);
                } else {
                    getJugador().getAvatar().moverAvatar(getTablero().getPosiciones(), 32 - getJugador().getAvatar().getLugar().getPosicion());
                }
                break;
            case 2:
                // printeamos el mensaje correspondiente y encarcelamos al jugador
                consolaNormal.imprimir(getImpresion());
                getJugador().encarcelar(getTablero().getPosiciones());
                break;
            case 3:
                // printeamos el mensaje correspondiente y sumamos 1.000.000€ a la fortuna del jugador asi como a sus premios por inversiones y bote
                consolaNormal.imprimir(getImpresion());
                getJugador().sumarFortuna(1000000);
                getJugador().sumarpremiosinversionesbote(1000000);
                break;
            case 4:
                // printeamos el mensaje correspondiente y restamos 250.000€ a la fortuna del jugador por cada jugador contrario ademas de darselos al resto de jugadores y sumar los gastos e impuestos correspondientes
                consolaNormal.imprimir(getImpresion());
                if(getJugadores().size()>1) {
                    for (Jugador jugador1 : getJugadores()) {
                        if (!jugador1.equals(getJugador())) {
                            try {
                                if (getJugador().puedePagar((float) 250000)) {
                                    jugador1.sumarFortuna(250000);
                                    jugador1.sumarpremiosinversionesbote(250000);
                                    getJugador().sumarFortuna(-250000);
                                    getJugador().sumarGastos(250000);
                                    getJugador().sumarimpuestos(250000);
                                }
                            }catch(NoPuedePagar e) { // Caso especial en el que no le debemos dinero al dueño de la casilla (Suerte)
                                consolaNormal.imprimir("El jugador " + getJugador().getNombre() + " no puede pagar a todos los jugadores.");
                                try {
                                    e.solucionarDuedas(getJugador(), jugador1, 250000);
                                }catch (ErrorHipoteca ex){
                                    consolaNormal.imprimir(ex.getMessage());
                                }
                            }
                        }
                    }
                }
                break;
            case 5:
                // printeamos el mensaje correspondiente y movemos el avatar del jugador 3 casillas hacia atras
                consolaNormal.imprimir(getImpresion());
                getJugador().getAvatar().moverAvatar(getTablero().getPosiciones(), -3);
                break;
            case 6:
                consolaNormal.imprimir(getImpresion());
                if (getJugador().puedePagar(150000)) {
                    getJugador().sumarFortuna(-150000);
                    getJugador().sumarGastos(150000);
                    getJugador().sumarimpuestos(150000);
                }
                break;
            case 7:
                // printeamos el mensaje correspondiente y movemos el avatar del jugador hasta la casilla de transporte mas cercana
                consolaNormal.imprimir(getImpresion());
                // sacamos la posicion actual del jugador
                int posActual = getJugador().getAvatar().getLugar().getPosicion();
                // en funcion de su posicion actual movemos su avatar a la casilla de transporte mas cercana ( utilizamos el switch para diferenciar entre cada una de las posiciones de las casillas suerte detro de nuestro tablero)
                switch (posActual) {
                    case 7:
                        getJugador().getAvatar().moverAvatar(getTablero().getPosiciones(), -2);
                        break;
                    case 22:
                        getJugador().getAvatar().moverAvatar(getTablero().getPosiciones(), 3);
                        break;
                    case 36:
                        getJugador().getAvatar().moverAvatar(getTablero().getPosiciones(), -1);
                        break;
                }
                Propiedad transporte = (Propiedad) getJugador().getAvatar().getLugar();
                if (!transporte.getDuenho().esBanca() || !transporte.getDuenho().equals(getJugador()) && jugador.puedePagar(transporte.Valor())) {
                    transporte.alquiler(transporte.getDuenho(), getJugador(), 2);
                }
                break;
            default:
                break;
        }

    }

}

