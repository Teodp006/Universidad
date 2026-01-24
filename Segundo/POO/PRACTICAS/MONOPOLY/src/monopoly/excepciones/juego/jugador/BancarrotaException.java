package monopoly.excepciones.juego.jugador;

import monopoly.Valor;

public class BancarrotaException extends NoPuedePagar {
    public BancarrotaException(String message) {
        super(message, Valor.FORTUNA_BANCA);
    }
}
