package monopoly;


import monopoly.excepciones.casilla.ErrorCasilla;
import monopoly.excepciones.casilla.ErrorHipoteca;
import monopoly.excepciones.casilla.NoEsPropiedadJugador;
import monopoly.excepciones.juego.*;
import monopoly.excepciones.juego.jugador.NoExisteJugador;
import monopoly.excepciones.juego.jugador.NoPuedePagar;

public interface Comando {

    /** Funcion para crear un jugador
    * @param nombre Nombre del jugador
    * @param tipoAvatar Tipo de avatar del jugador
    * **/
    void crearJugador(String nombre, String tipoAvatar) throws NoExisteComando;

    void listarJugadores();

    void jugadorActual() throws ErrorJuego;

    void acabarTurno() throws ErrorJuego;

    void verTablero();

    void comprarCasilla(String nombreCasilla) throws NoExisteCasilla, NoEsPropiedadJugador, NoPuedePagar, ErrorCasilla, ErrorJuego;

    void edificarEdificio(String tipoEdificio) throws NoPuedePagar, ErrorJuego;

    void venderEdificio(String tipoEdificio, String nombreCasilla, int numeroEdificios) throws NoExisteCasilla, NoEsPropiedadJugador, ErrorJuego;

    void lanzarDadosAleatorios() throws NoPuedePagar, ErrorJuego;

    void lanzarDados(int tirada1, int tirada2) throws NoPuedePagar, ErrorJuego;

    void listarEdificiosGrupo(String nombreGrupo);

    void listarEdificios();

    void listarCasillasEnVenta();

    void hipotecarCasilla(String nombreCasilla) throws NoExisteCasilla, ErrorHipoteca, NoEsPropiedadJugador, ErrorJuego;

    void deshipotecarCasilla(String nombreCasilla) throws NoExisteCasilla, ErrorHipoteca, NoEsPropiedadJugador, NoPuedePagar, ErrorJuego;

    void describirCasilla(String nombreCasilla) throws NoExisteCasilla ;

    void describirJugador(String nombreJugador);

    void salirCarcel() throws ErrorJuego;

    void estadisticasPartida(); //

    void estadisticasJugador(String nombreJugador); //

    void trato(String partes[]) throws NoEsPropiedadJugador, NoExisteJugador, NoPuedePagar, ErrorJuego; //

    void aceptarTrato(String idtrato) throws ErrorJuego; //

    void eliminarTrato(String idtrato) throws ErrorJuego; //

    void listarTratos() throws ErrorJuego; //

}
