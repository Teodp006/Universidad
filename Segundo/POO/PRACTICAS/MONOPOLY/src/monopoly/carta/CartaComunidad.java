package monopoly.carta;
import monopoly.Tablero;
import monopoly.excepciones.juego.jugador.NoPuedePagar;
import partida.Jugador;

import static monopoly.Juego.consolaNormal;

/**
 * Carta de tipo \"Comunidad\" con varias acciones predefinidas.
 * Cada instancia representa una carta concreta identificada por el
 * campo {@code comunidad} y ejecuta una acción distinta sobre el
 * jugador que la recibe.
 */
public class CartaComunidad extends Carta {
    private int comunidad;

    /**
     * Crea una carta de comunidad asociada a un tablero y con un tipo concreto.
     *
     * @param comunidad identificador del tipo de carta de comunidad (1..6)
     * @param tablero referencia al tablero del juego
     */
    public CartaComunidad(int comunidad, Tablero tablero) {
        super(tablero);
        switch (comunidad) {
            case 1:
                this.comunidad = comunidad;
                setImpresion("Paga 500.000€ por un fin de semana en un balneario de 5 estrellas.");
                break;
            case 2:
                this.comunidad = comunidad;
                setImpresion("Te investigan por fraude de identidad. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar los 2.000.000€");
                break;
            case 3:
                this.comunidad = comunidad;
                setImpresion("Colócate en la casilla de salida. Cobra 2.000.000€");
                break;
            case 4:
                this.comunidad = comunidad;
                setImpresion("Devolucion de Hacienda. Cobra 500.000€");
                break;
            case 5:
                this.comunidad = comunidad;
                setImpresion("Retrocede hasta Solar1 para comprar antigüedades exoticas.");
                break;
            case 6:
                this.comunidad = comunidad;
                setImpresion("Ve al solar 20 para disfrutar del San Fermín. Si pasas por la casilla de salida, cobra 2.000.000€");
                break;
            default:
                break;
        }

    }

    /**
     * Ejecuta la acción correspondiente a la carta de comunidad sobre el jugador.
     * La acción varía según el valor de {@code comunidad} e incluye pagos, movimientos,
     * encarcelamiento o cambios en la fortuna del jugador.
     *
     * @param jugador jugador al que se aplica la carta
     * @throws NoPuedePagar si el jugador no puede realizar un pago requerido
     */
    @Override
    public void accion(Jugador jugador) throws NoPuedePagar {
        switch (comunidad) {
            case 1:
                // printeamos el mensaje correspondiente y restamos 500.000€ a la fortuna del jugador por un gasto de ocio asi como le sumamos los impuestos correspondientes y los gastos
                consolaNormal.imprimir("Paga 500.000€ por un fin de semana en un balneario de 5 estrellas.");
                if (getJugador().puedePagar(500000)) {
                    getJugador().sumarFortuna(-500000);
                    getJugador().sumarGastos(500000);
                    getJugador().sumarimpuestos(500000);
                }
                break;
            case 2:
                // printeamos el mensaje correspondiente y encarcelamos al jugador
                consolaNormal.imprimir("Te investigan por fraude de identidad. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar los 2.000.000€");
                getJugador().encarcelar(getTablero().getPosiciones());
                break;
            case 3:
                // printeamos el mensaje correspondiente y movemos el avatar del jugador a la casilla de salida
                consolaNormal.imprimir("Colócate en la casilla de salida. Cobra 2.000.000€");
                int posicion = getJugador().getAvatar().getLugar().getPosicion();
                getJugador().getAvatar().moverAvatar(getTablero().getPosiciones(), 40 - posicion);
                break;
            case 4:
                // printeamos el mensaje correspondiente y sumamos 500.000€ a la fortuna del jugador asi como a sus premios por inversiones y bote
                consolaNormal.imprimir("Devolucion de Hacienda. Cobra 500.000€");
                getJugador().sumarFortuna(500000);
                getJugador().sumarpremiosinversionesbote(500000);
                break;
            case 5:
                // printeamos el mensaje correspondiente y movemos el avatar del jugador a la casilla solar1
                consolaNormal.imprimir("Retrocede hasta Solar1 para comprar antigüedades exoticas.");
                posicion = getJugador().getAvatar().getLugar().getPosicion();
                getJugador().getAvatar().moverAvatar(getTablero().getPosiciones(), -posicion + 1);
                break;
            case 6:
                // printeamos el mensaje correspondiente y movemos el avatar del jugador a la casilla solar20
                consolaNormal.imprimir("Ve al solar 20 para disfrutar del San Fermín. Si pasas por la casilla de salida, cobra 2.000.000€");
                posicion = getJugador().getAvatar().getLugar().getPosicion();
                // volvemos a hacer la diferenciacion para comprobar si se tiene que pasar por salida o no
                if (34 - posicion < 0) {
                    getJugador().getAvatar().moverAvatar(getTablero().getPosiciones(), 34 - posicion + 40);
                } else {
                    getJugador().getAvatar().moverAvatar(getTablero().getPosiciones(), 34 - posicion);
                }
                break;
            default:
                break;
        }
    }
}

