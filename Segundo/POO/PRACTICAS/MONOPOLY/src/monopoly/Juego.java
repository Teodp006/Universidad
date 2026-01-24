package monopoly;

import monopoly.carta.CartaComunidad;
import monopoly.carta.CartaSuerte;
import monopoly.excepciones.juego.*;
import monopoly.excepciones.casilla.*;
import monopoly.excepciones.juego.jugador.BancarrotaException;
import monopoly.excepciones.juego.jugador.NoExisteJugador;
import monopoly.excepciones.juego.jugador.NoPuedePagar;
import partida.Avatar;
import partida.Dado;
import partida.Jugador;
import monopoly.edificios.*;
import monopoly.casilla.accion.*;
import monopoly.casilla.propiedad.*;
import monopoly.casilla.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static monopoly.Valor.SUMA_VUELTA;

public class Juego implements Comando{

    private final ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private final ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private final Tablero tablero; //Tablero en el que se juega.
    private final Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private final Dado dado2;
    private final Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private int suerte = 0;
    private int numerotratos = 1;
    private int comunidad = 0;
    private int incrementoSolares=0;
    public final static ConsolaNormal consolaNormal = new ConsolaNormal();

    public Juego() {
        this.jugadores = new ArrayList<>();
        this.avatares = new ArrayList<>();
        this.banca = new Jugador();
        tablero = new Tablero(this.banca);
        this.banca.setFortuna(Valor.FORTUNA_BANCA);
        this.banca.setPropiedades(tablero.propiedadesBanca()); // CasillaOriginal, hay q cambiar por casilla a secas
        this.dado1 = new Dado();
        this.dado2 = new Dado();

    }

    /** Metodo privado que permite comenzar una partida a través de los comandos presentes en un archivo de texto.
     *
     * @param fichero Nombre del fichero de texto que contiene los comandos a ejecutar.
     * @return ArrayList<String> con las lineas del fichero, que luego se analizarán como comandos.
     */
    private ArrayList<String> lecturaFichero(String fichero) {
        // Lista para almacenar las lineas del fichero
        ArrayList<String> lista = new ArrayList<>();
        try{
            //  Abrimos el fichero y creamos el scanner q utilizaremos para leerlo linea a linea e ir insertandolas en la lista
            File f= new File(fichero);
            Scanner sc= new Scanner(f);
            while(sc.hasNextLine()){
                String linea= sc.nextLine();
                lista.add(linea);
            }
        }catch(FileNotFoundException e){
            consolaNormal.imprimir("Error...");

        }
        return lista;
    }

    /** Metodo estatico que inicia una partida de Monopoly leyendo los comandos de un fichero de texto y permitiendo la interacción con el usuario a través de la consola.
     *
     * @param nombreFichero Opcional, nombre del fichero de texto que contiene los comandos a ejecutar al inicio de la partida.
     */
    public static void iniciarPartida(String nombreFichero){
        Juego juego = new Juego();
        // Lectura del fichero de comandos
        if (nombreFichero != null) {
            ArrayList<String> listacomandos = juego.lecturaFichero(nombreFichero);
            for (String comando : listacomandos) {
                try {
                    juego.analizarComando(comando);
                } catch (NoExisteComando e) {
                    consolaNormal.imprimir(e.getMessage());
                    iniciarPartida(nombreFichero);
                }
            }
        }
        while (true) {
            try{
                //bucle infinito hasta q el usuario decida salir
                String comando = consolaNormal.leer("Ingrese un comando (o 'salir' para terminar): "); // consolaNormal.imprimir("Ingrese un comando (o 'salir' para terminar): ");
                if (comando.equalsIgnoreCase("salir")) {
                    break;
                }
                juego.analizarComando(comando);
            }catch(NoExisteComando e) {
                consolaNormal.imprimir(e.getMessage());
            }
        }
    }

    /** Metodo privado que implementa la gestión de todos los comandos y sus excepciones.
     *
     * @param comando comando a ejecutar.
     * @throws NoExisteComando si el comando no existe o no tiene el formato correcto.
     */
    private void analizarComando(String comando) throws NoExisteComando {
        consolaNormal.imprimir(">>> " + comando); // consolaNormal.imprimir(">>> " + comando);
        //partimos el string del comando entero en sus diferentes componentes
        String[] partes = comando.split(" ");
        //distinguimos mediante el switch entre los diferentes comandos existentes.
        try {
            switch (partes[0]) {
                case "crear":
                    if (partes.length == 1) {
                        throw new NoExisteComando("Error: El comando 'crear' debe ir acompañador de jugador.\n");
                    }
                    //distinguimos los diferentes casos del comando crear
                    if (partes[1].equals("jugador")) {
                        if (partes.length == 4) {
                            crearJugador(partes[2], partes[3]);
                        } else {
                            throw new NoExisteComando("Error: El comando 'crear jugador' debe ir seguido del nombre del jugador y el tipo de avatar.\n");
                        }
                    } else {
                        throw new NoExisteComando("Error: El comando 'crear' debe ir acompañador de jugador.\n");
                    }
                    break;

                case "jugador":
                    //llamamos al metodo q nos da el jugador que tiene el turno actualmente
                    jugadorActual();
                    break;

                case "listar":
                    //diferenciamos entre los diferentes casos del comando listar
                    if (partes.length == 1) {
                        throw new NoExisteComando("Error: El comando 'listar' debe ir seguido de 'jugadores', 'enventa' o 'edificios'.\n");
                    }
                    switch (partes[1]) {
                        case "jugadores":
                            //llamamos al metodo q lista los jugadores
                            listarJugadores();
                            break;
                        case "enventa":
                            //llamamos al metodo q lista las casillas en venta
                            listarCasillasEnVenta();
                            break;
                        case "edificios":
                            if (partes.length == 3) {
                                //llamamos al metodo q lista los edificios de un grupo en concreto
                                listarEdificiosGrupo(partes[2]);
                            } else {
                                //llamamos al metodo q lista los edificios
                                listarEdificios();
                            }
                            break;
                        default:
                            throw new NoExisteComando("Error: El comando 'listar' debe ir seguido de 'jugadores', 'enventa' o 'edificios'.\n");
                    }
                    break;
                case "lanzar":
                    //comprobamos si el comando es lanzar dados
                    //si el comando tiene 3 partes se han forzado los dados
                    if (!tirado && partes.length == 3) {
                        try {
                            String[] partes2 = partes[2].split("\\+");
                            if (partes2.length != 2) {
                                throw new NoExisteComando("Error en el formato de los dados. Deben ser números enteros separados por '+'.\n");
                            }
                            lanzarDados(Integer.parseInt(partes2[0]), Integer.parseInt(partes2[1]));
                            // llamamos al metodo para comprobar si ha caido en una casilla de suerte o comunidad
                        } catch (NumberFormatException e) {
                            consolaNormal.imprimir("Error en el formato de los dados. Deben ser números enteros separados por '+'.\n");
                            tirado = false;
                        }
                    }
                    //si el comando tiene 2 partes se lanzan los dados obteniendo numeros aleatorios
                    else if (!tirado && partes.length == 2) {
                        if (partes[1].equals("dados")) {
                            lanzarDadosAleatorios();
                            // llamamos al metodo para comprobar si ha caido en una casilla de suerte o comunidad
                        }
                    }
                    // comprobamos si el jugador esta en la carcel y si no ha tirado aun para q pueda lanzar los dados para intentar salir sin pagar
                    else {
                        consolaNormal.imprimir("El jugador " + jugadorTurno().getNombre() + " ya ha tirado los dados este turno.\n");
                    }

                    break;

                case "acabar":
                    //distinguimos si el comando es acabar turno y llamamos al metodo correspondiente
                    acabarTurno();
                    //printeamos el jugador que tiene el turno ahora
                    consolaNormal.imprimir("El jugador actual es " + jugadorTurno().getNombre() + ".\n");
                    listarTratos();
                    break;
                case "salir":
                    //comprobamos si el comando es salir carcel y llamamos al metodo correspondiente en caso de que nuestro jugador este en la carcel
                    if (jugadorTurno().getenCarcel()) {
                        salirCarcel();
                    } else {
                        consolaNormal.imprimir("El jugador " + jugadorTurno().getNombre() + " no está en la carcel.\n");
                    }
                    break;
                case "describir":
                    //distinguimos entre los diferentes casos del comando describir
                    if (partes.length == 1) {
                        throw new NoExisteComando("Error: El comando 'describir' debe ir seguido de jugador <nombre> o <nombreCasilla>.\n");
                    }
                    if (partes[1].equals("jugador")) {
                        if (partes.length != 3) {
                            throw new NoExisteComando("Error: El comando 'describir jugador' debe ir seguido del nombre del jugador.\n");
                        }
                        //llamamos al metodo q describe un jugador
                        describirJugador(partes[2]);
                    } else {//llamamos al metodo q describe una casilla
                        describirCasilla(partes[1]);
                    }
                    break;
                case "comprar":
                    //comprobamos si el comando es comprar casilla y si tiene al menos 2 partes para q pueda hacer bien la ejecucion
                    if (partes.length != 2) {
                        throw new NoExisteComando("Error: El comando 'comprar' debe ir seguido del nombre de la casilla a comprar.\n");
                    }
                    //llamamos al metodo para comprar la casilla
                    comprarCasilla(partes[1]);
                    break;
                case "ver":
                    //comprobamos si el comando es ver tablero y printeamos el tablero
                    verTablero();
                    break;
                case "edificar":
                    if (partes.length == 4) {
                        if (partes[1].equals("pista") && partes[2].equals("de") && partes[3].equals("deporte")) {
                            partes[1] = "Pista de deporte";
                        }
                        edificarEdificio(partes[1]);
                    } else if (partes.length == 2) {
                        edificarEdificio(partes[1]);
                    } else {
                        throw new NoExisteComando("Error en el formato del comando 'edificar'. Debe ir seguido del tipo de edificio y el nombre de la casilla.\n");
                    }
                    break;
                case "hipotecar":
                    if (partes.length != 2) {
                        throw new NoExisteComando("Error: El comando 'hipotecar' debe ir seguido del nombre de la casilla a hipotecar.\n");
                    }
                    hipotecarCasilla(partes[1]);
                    break;
                case "deshipotecar":
                    if (partes.length != 2) {
                        throw new NoExisteComando("Error: El comando 'deshipotecar' debe ir seguido del nombre de la casilla a deshipotecar.\n");
                    }
                    deshipotecarCasilla(partes[1]);
                    break;
                case "vender":
                    if (partes.length < 4) {
                        throw new NoExisteComando("Error: El comando 'vender' debe ir seguido del tipo de propiedad que se quiere vender, el nombre de la casilla y el numero de propiedades que se quieren vender.");
                    } else if (partes[1].equals("Pista") && partes[2].equals("de") && partes[3].equals("deporte")) {
                        partes[1] = "Pista de deporte";
                        partes[3] = partes[5];
                        partes[2] = partes[4];
                    }
                    venderEdificio(partes[1], partes[2], Integer.parseInt(partes[3]));

                    break;
                case "estadisticas":
                    if (partes.length == 1) {
                        //llamamos al metodo para ver las estadisticas de la partida
                        estadisticasPartida();
                    } else if (partes.length == 2) {
                        //llamamos al metodo para ver las estadisticas de un jugador
                        estadisticasJugador(partes[1]);
                    } else {
                        throw new NoExisteComando("Error: El comando 'estadisticas' debe ir seguido de 'partida'.\n");
                    }
                    break;
                case "trato":
                    if (partes.length < 5) {
                        throw new NoExisteComando("Error: El comando trato debe seguir el formato: trato <receptor>: cambiar (<oferta>, <peticion>)");
                    }
                    trato(partes);
                    break;
                case "tratos":
                    listarTratos();
                    break;
                case "eliminar":
                    if (partes.length != 2) {
                        throw new NoExisteComando("Error: El comando 'eliminar' debe ir seguido del identificador del trato a eliminar.\n");
                    }
                    eliminarTrato(partes[1]);
                    break;
                case "aceptar":
                    if (partes.length != 2) {
                        throw new NoExisteComando("Error: El comando 'aceptar' debe ir seguido del identificador del trato a aceptar.\n");
                    }
                    aceptarTrato(partes[1]);
                    break;
                default:
                    throw new NoExisteComando("Error: Ese comando no existe\n");
            }
        } catch(BancarrotaException e) {
            consolaNormal.imprimir(e.getMessage());
            try {
                jugadores.remove(jugadorTurno());
            }
            catch(ErrorJuego ex){
                consolaNormal.imprimir(ex.getMessage());
            }

        } catch(NoPuedePagar e){
            consolaNormal.imprimir(e.getMessage());
            try {
                if (jugadorTurno().getAvatar().getLugar() instanceof Propiedad propiedad) {
                    e.solucionarDuedas(jugadorTurno(), propiedad.getDuenho(), e.getCantidadPendiente());
                } else {
                    e.solucionarDuedas(jugadorTurno(), banca, e.getCantidadPendiente());
                }
            }
            catch(ErrorHipoteca | ErrorJuego ex){
                consolaNormal.imprimir(ex.getMessage());
            }

        } catch (NoExisteJugador | NoExisteCasilla | ErrorHipoteca | NoEsPropiedadJugador e){
            consolaNormal.imprimir(e.getMessage());
        } catch (ErrorJuego | ErrorCasilla e){
            consolaNormal.imprimir(e.getMessage());
        }
    }

    @Override
    /** Metodo que implementa el comando crear jugador <nombre> <tipoAvatar> un jugador cuando no existen ya 4 jugadores en la partida jugando simultáneamente, si ese nombre y tipoAvatar no están ya en uso.
    *
    * @param nombre Nombre del jugador a crear.
    * @param tipoAvatar Tipo de avatar del jugador a crear (coche, pelota, sombrero o esfinge).
    *
    */
    public void crearJugador(String nombre, String tipoAvatar) {
        if (jugadores.size() >= 4) {
            consolaNormal.imprimir("No se pueden crear más jugadores. El número máximo es 4.\n");
            return;
        }
        // recorremos la lista de jugaodres para revisar q no tengamos el mismo nombre ya guardado
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equals(nombre)) {
                consolaNormal.imprimir("El nombre de jugador " + nombre + " ya está en uso. Elija otro.\n");
                return;
            }
        }
        // comprobamos q la figura pertenezca a las disponibles y q no esta ya escogida
        if (tipoAvatar.equals("coche") || tipoAvatar.equals("pelota") || tipoAvatar.equals("sombrero") || tipoAvatar.equals("esfinge")) {
            for (Jugador jugador : jugadores) {
                if (jugador.getAvatar().getTipo().equals(tipoAvatar)) {
                    consolaNormal.imprimir("La figura " + jugador.getAvatar().getTipo() + " ya esta en uso. Elija otro\n");
                    return;
                }
            }
            Jugador jugador = new Jugador(nombre, tipoAvatar, tablero.getInicio(), avatares); // CasillaOriginal igual q arriba
            jugadores.add(jugador);
            consolaNormal.imprimir(jugador.toString());
        } else {
            consolaNormal.imprimir("El avatar " + nombre + " no existe. Los avatares disponibles son: coche, pelota, sombrero y esfinge.\n");
        }
    }

    @Override
    /** Metodo para listar los jugadores de la partida con su informacion (Propiedades, edificios, avatar, nombre, fortuna...).
     *
     */
    public void listarJugadores() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        // recorremos la lista de jugadores con un for each para printear su informacion
        for (Jugador jugador : jugadores) {
            sb.append("{\n");
            sb.append("\tnombre: ").append(jugador.getNombre()).append(",\n\tavatar: ").append(jugador.getAvatar().getTipo()).append(",\n\tfortuna: ").append((int) jugador.getFortuna());
            //verificamos si el jugador tiene propiedades para printearlas
            if (!jugador.getPropiedades().isEmpty()) {
                sb.append("\n\tpropiedades: [");
                int j = 0;
                //recorremos la lista de propiedades del jugador para printearlas
                for (Propiedad prop : jugador.getPropiedades()) {
                    sb.append(prop.getNombre());
                    if (j < jugador.getPropiedades().size() - 1) {
                        sb.append(",");
                        j++;
                    }
                }
                sb.append("]");
            }
            // verificamos si el jugador tiene edificios para printearlos
            if (!jugador.getEdificios().isEmpty()) {
                sb.append("\n\tedificios: [");
                int j = 0;
                //recorremos la lista de edificios del jugador para printearlas
                for (Edificio edificio : jugador.getEdificios()) {
                    sb.append(edificio.getClass().getSimpleName()).append("-").append(edificio.getIdentificador());
                    if (j < jugador.getEdificios().size() - 1) {
                        sb.append(",");
                        j++;
                    }
                }
                sb.append("]\n");
            }
            if (i < jugadores.size() - 1) {
                sb.append("},\n");
                i++;
            }
        }
        sb.append("\n}\n");
        consolaNormal.imprimir(sb.toString());
    }


    @Override
    /** Método que implementa el comando jugador para imprimir por pantalla todos los datos del jugador que tiene el turno.
     *
     * @throws ErrorJuego si no hay jugadores en la partida.
     */
    public void jugadorActual() throws ErrorJuego {
        Jugador jugador = jugadorTurno();
        String mensaje = "{\n\tnombre: " + jugador.getNombre() + ",\n\tavatar: " + jugador.getAvatar();
        consolaNormal.imprimir(mensaje + "\n}\n");
    }

    @Override
    /** Método que implementa el comando acabar turno para finalizar el turno del jugador actual y pasar al siguiente.
     *
     * @throws ErrorJuego si no hay jugadores en la partida, porque no se puede terminar ningún turno.
     */
    public void acabarTurno() throws ErrorJuego{
        int i = jugadores.size();
        if(i == 0){
            throw new ErrorJuego("No puede ejecutar <acabar turno> porque no hay jugadores en la partida.\n");
        }
        boolean checkVueltas=true;
        turno = (turno + 1) % i;
        tirado = false;
        lanzamientos = 0;
        // comprobamos si todos los jugadores han dado 4 vueltas para incrementar el valor de los solares
        for(Jugador j: jugadores){
            if(j.getVueltas()<4 || incrementoSolares==1){
                checkVueltas = false;
                break;
            }
        }

        if(checkVueltas && incrementoSolares==0){
            tablero.incrementarValorSolares();
            incrementoSolares = 1;
        }
    }

    @Override
    /** Método que implementa el comando ver tablero para mostrar el estado actual del tablero.
     *
     */
    public void verTablero() {
        // Aquí no queremos ver las cartas, solo el tablero
        tablero.setHayCarta(false);
        consolaNormal.imprimir(tablero.toString());
    }

    @Override
    /** Método que implementa el comando comprar <nombre_casilla> para , comprar la casilla en la que se encuentra el jugador que tiene el turno.
     * @param nombreCasilla Nombre de la casilla que se va a comprar (debe coincidir con el Lugar donde está el jugador que tiene le turno).
     * @throws ErrorCasilla Si la Casilla no está a la venta (porque no es una propiedad) o si ya la posee otro jugador.
     * @throws ErrorJuego Si no hay jugadores en la partida, si el jugador no tiene suficiente dinero para comprar la casilla o si la casilla indicada no existe en el tablero.
     */
    public void comprarCasilla(String nombreCasilla) throws ErrorJuego, ErrorCasilla{
        //buscamos la casilla en el tablero y obtenemos el jugador que tiene el turno
        Casilla casilla = tablero.buscarCasilla(nombreCasilla);
        Jugador jugador = jugadorTurno();
        //verificamos q la casilla no sea null para proceder a la compra
        if (casilla != null) {
            //verificamos q el dueño de la casilla sea la banca y q el jugador este en la casilla para poder comprarla
            if(casilla instanceof Propiedad propiedad) {
                if (propiedad.getDuenho().esBanca()) {
                    if (jugadorTurno().getAvatar().getLugar() != casilla) {
                        consolaNormal.imprimir("El jugador " + jugador.getNombre() + " no está en la casilla " + nombreCasilla + " y no puede comprarla.\n");
                    } else {
                        //realizamos la compra de la casilla
                        propiedad.comprar(jugador);
                        consolaNormal.imprimir("El jugador " + jugador.getNombre() + " ha comprado la casilla " + nombreCasilla + ".\n");
                    }
                }
                else {
                    throw new NoEsPropiedadJugador("Error: La casilla " + nombreCasilla + " le pertenece a " + propiedad.getDuenho().getNombre() + "\n");
                }
            }
            else{
                throw new ErrorCasilla("La casilla " + nombreCasilla + " no es una propiedad y no se puede comprar.\n");
            }
            //si la casilla no pertenece a la banca informamos de su dueño
        }
        //en caso de que no este en el tablero informamos de que no existe dicha casilla
        else {
            throw new NoExisteCasilla("Error: La casilla "+nombreCasilla+" no existe en el tablero.\n");        }
    }

    @Override
    /** Método que implementa el comando edificar <tipo_edificio> para edificar un edificio en la casilla en la que se encuentra el jugador que tiene el turno, si tiene todos los Solares de dicho grupo.
     * @param tipoEdificio Tipo de edificio a edificar (Casa, Hotel, Piscina, Pista de deporte).
     * @throws ErrorJuego Si no hay jugadores en la partida.
     */
    public void edificarEdificio(String tipoEdificio) throws ErrorJuego {
        if(!(jugadorTurno().getAvatar().getLugar() instanceof Solar casillaAEdificar)){
            consolaNormal.imprimir("El jugador " + jugadorTurno().getNombre() + " no está en un solar y no puede edificar.\n");
            return;
        }

        // Le aplicamos el método Capitalize para que funcione correctamente con mayúsculas y minúsculas
        tipoEdificio = tipoEdificio.substring(0, 1).toUpperCase() + tipoEdificio.substring(1).toLowerCase();
        if(this.existeTipoEdificio(tipoEdificio)){
            int idEdificio = tablero.contarEdificiosTipo(tipoEdificio) + 1;
            casillaAEdificar.edificar(tipoEdificio, idEdificio);
        } else {
            consolaNormal.imprimir("No existe tal tipo de edificio\n");
        }
    }


    @Override
    /** Método que implementa el comando vender <tipo_edificio> <nombre_casilla> <numero_edificios> para vender edificios en una casilla concreta.
     *
     * @param tipoEdificio Tipo de edificio a vender (Casa, Hotel, Piscina, Pista de deporte).
     * @param nombreCasilla Nombre de la casilla en la que se encuentran los edificios a vender.
     * @param numeroEdificios Cantidad de edificios de ese tipo a vender.
     * @throws ErrorJuego Si no hay jugadores en la partida o si la casilla indicada no existe en el tablero.
     * @throws NoEsPropiedadJugador Si se intenta vender una casilla que no pertenece al jugador que tiene el turno.
     */
    public void venderEdificio(String tipoEdificio, String nombreCasilla, int numeroEdificios) throws ErrorJuego, NoEsPropiedadJugador {
            Casilla casilla = tablero.buscarCasilla(nombreCasilla);
            if(casilla == null){
                throw new NoExisteCasilla("Error: La casilla "+nombreCasilla+" no existe en el tablero.\n");
            }
            // Le aplicamos el método Capitalize para que funcione correctamente con mayúsculas y minúsculas
            tipoEdificio = tipoEdificio.substring(0, 1).toUpperCase() + tipoEdificio.substring(1).toLowerCase();
            if(!this.existeTipoEdificio(tipoEdificio)){
                consolaNormal.imprimir("No existe tal tipo de edificio\n");
            }
            else {
                if(casilla instanceof Solar aVender) {
                    if (jugadorTurno().equals(aVender.getDuenho())) {
                        aVender.venderEdificio(tipoEdificio, numeroEdificios);
                    } else {
                        throw new NoEsPropiedadJugador("Error: El jugador " + jugadorTurno().getNombre() + " no puede vender edificios en " + aVender.getNombre() + ".  No es una propiedad que le pertenece\n");
                    }
                }
                else{
                    consolaNormal.imprimir("La casilla " + nombreCasilla + " no es un solar y no se pueden vender edificios en ella.\n");
                }
            }
    }

    @Override
    /** Método que implementa el comando lanzar dados  para lanzar los dados con valores aleatorios.
     *
     * @throws ErrorJuego Si no hay jugadores en la partida.
     */
    public void lanzarDadosAleatorios() throws ErrorJuego {
        if (jugadores.isEmpty()) {
            consolaNormal.imprimir("No hay jugadores en la partida. No se pueden lanzar los dados.\n");
            return;
        }
        //realizamos la tirada de ambos dados y llamamos al metodo para poder avanzar las casillas correspondientes

        realizarTirada(-1, -1, jugadorTurno(), true);
    }

    @Override
    /** Método que implementa el comando lanzar dados <dado1>+<dado2> para lanzar los dados con valores forzados.
     *
     * @param tirada1 Valor del primer dado.
     * @param tirada2 Valor del segundo dado.
     * @throws ErrorJuego Si no hay jugadores en la partida.
     */
    public void lanzarDados(int tirada1, int tirada2) throws ErrorJuego {
        if (jugadores.isEmpty()) {
            consolaNormal.imprimir("No hay jugadores en la partida. No se pueden lanzar los dados.\n");
            return;
        }
        //realizamos la tirada de ambos dados con los valores forzados y llamamos al metodo para poder avanzar las casillas correspondientes

        realizarTirada(tirada1, tirada2, jugadorTurno(), false);
    }

    @Override
    /** Método que implementa el comando listar edificios grupo <nombre_grupo> para mostrar los edificios de un grupo en concreto.
     *
     * @param nombreGrupo Nombre del grupo del que se quieren listar los edificios.
     */
    public void listarEdificiosGrupo(String nombreGrupo) {
        boolean flagHoteles = false, flagPiscinas = false, flagPistas = false;
        // obtenemos las casillas del grupo indicado
        ArrayList<Solar> CasillasGrupo = tablero.getGrupos().get(nombreGrupo).getMiembros();
        // recorremos las casillas del grupo y printeamos los edificios que haya en cada una
        for (Solar casilla : CasillasGrupo) {
            //verificamos si hay edificios en la casilla para printearlos
            if (!casilla.edificiosCasilla().isEmpty()) {
                consolaNormal.imprimir("Propiedad: " + casilla.getNombre() + "\n");
                consolaNormal.imprimir("Hoteles: ");
                //verificamos si hay hoteles en la casilla para printearlos
                if (casilla.getHotel() == null) {
                    // en caso de que no haya hoteles ponemos un guion y activamos el flag
                    flagHoteles = true;
                    consolaNormal.imprimir("-");
                } else {
                    // en caso de que haya hoteles los printeamos recorriendo la lista de hoteles
                    Hotel hotel  = casilla.getHotel();
                    consolaNormal.imprimir("[Hotel-" + hotel.getIdentificador() + "],");
                    
                }
                consolaNormal.imprimir("\nCasas: ");
                //verificamos si hay casas en la casilla para printearlas
                if (casilla.getCasas().isEmpty()) {
                    consolaNormal.imprimir("-");
                } else {
                    // en caso de que haya casas las printeamos recorriendo la lista de casas
                    for (Casa casa : casilla.getCasas()) {
                        consolaNormal.imprimir("[Casa-" + casa.getIdentificador() + "],");
                    }
                }
                consolaNormal.imprimir("\nPiscinas: ");
                //verificamos si hay piscinas en la casilla para printearlas
                if (casilla.getPiscina() == null) {
                    // en caso de que no haya piscinas ponemos un guion y activamos el flag
                    flagPiscinas = true;
                    consolaNormal.imprimir("-");
                } else {
                    Piscina piscina = casilla.getPiscina();
                    // en caso de que haya piscinas las printeamos recorriendo la lista de piscinas
                    consolaNormal.imprimir("[Piscina-" + piscina.getIdentificador() + "],");
                }
                // printeamos las pistas de deporte
                consolaNormal.imprimir("\nPistas de Deporte: ");
                //verificamos si hay pistas de deporte en la casilla para printearlas
                if (casilla.getPistaDeporte() == null) {
                    // en caso de que no haya pistas de deporte ponemos un guion y activamos el flag
                    flagPistas = true;
                    consolaNormal.imprimir("-");
                } else {
                    Pista_De_Deporte pistaDeporte = casilla.getPistaDeporte();
                    // en caso de que haya pistas de deporte las printeamos recorriendo la lista de pistas de deporte
                    consolaNormal.imprimir("[Pista-" + pistaDeporte.getIdentificador() + "],");
                }
            }
        }
        consolaNormal.imprimir("\n");
        // en funcion de los flags activados printeamos los mensajes correspondientes
        if (flagHoteles) {
            consolaNormal.imprimir("Aún se pueden construir hoteles, piscinas y pistas de deporte en este grupo.\n");
        }
        if (flagPiscinas && !flagHoteles) {
            consolaNormal.imprimir("Aún se pueden construir piscinas y pistas de deporte en este grupo. Ya no se pueden construir casas ni hoteles\n");
        }
        if (flagPistas && !flagHoteles && !flagPiscinas) {
            consolaNormal.imprimir("Aún se pueden construir pistas de deporte en este grupo. Ya no se pueden construir casas,hoteles ni piscinas\n");
        }
        if (flagHoteles && flagPiscinas && flagPistas) {
            consolaNormal.imprimir("Ya no se pueden construir más edificios en este grupo.\n");
        }
        if (!flagHoteles && !flagPiscinas && !flagPistas) {
            consolaNormal.imprimir("Es posible construir cualquier edificio en este grupo. Consigue 4 casas primero.\n");
        }
    }

    @Override
    /** Método que implementa el comando listar edificios para mostrar todos los edificios del tablero.
     *
     */
    public void listarEdificios(){
        //recorremos todas las casillas del tablero y printeamos los edificios que haya en cada una
        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla casilla : lado) {
                if(casilla instanceof Solar solar) {
                    if (!solar.edificiosCasilla().isEmpty()) {
                        // en caso de que haya edificios los printeamos
                        for (Edificio edificio : solar.edificiosCasilla()) {
                            if (solar.edificiosCasilla().getLast().equals(edificio)) {
                                consolaNormal.imprimir(edificio.toString());
                            } else {
                                consolaNormal.imprimir(edificio + ",\n");
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    /** Método que implementa el comando listar enventa para mostrar las casillas que están en venta (Solo propiedades)
     *
     */
    public void listarCasillasEnVenta() {
        for(Propiedad propiedad: tablero.propiedadesBanca()){
            consolaNormal.imprimir(propiedad.casEnVenta());
        }
    }

    @Override
    /** Método que implementa el comando hipotecar <nombre_casilla> para deshipotecar una casilla.
     *
     * @param nombreCasilla Nombre de la casilla que se quiere hipotecar.
     * @throws ErrorJuego En caso de que no exista la casilla en el tablero.
     * @throws NoEsPropiedadJugador En caso de que la casilla no pertenezca al jugador que tiene el turno.
     * @throws ErrorHipoteca En caso de que ya estuviese hipotecada.
     */
    public void hipotecarCasilla(String nombreCasilla) throws ErrorJuego, ErrorHipoteca, NoEsPropiedadJugador {
        Propiedad hipo = (Propiedad) tablero.buscarCasilla(nombreCasilla);
        if (hipo == null) {
            throw new NoExisteCasilla("Error: La casilla " + nombreCasilla + " no existe en el tablero.\n");
        }
        if (hipo.getDuenho().getNombre().equals(jugadorTurno().getNombre())) {
            hipo.hipotecar();
        } else {
            throw new NoEsPropiedadJugador("Error: El jugador " + jugadorTurno().getNombre() + " no puede hipotecar " + hipo.getNombre() + ".  No es una propiedad que le pertenece\n");
        }
    }

    @Override
    /** Método que implementa el comando hipotecar <nombre_casilla> para deshipotecar una casilla.
     *
     * @param nombreCasilla Nombre de la casilla que se quiere deshipotecar.
     * @throws ErrorJuego En caso de que no exista la casilla en el tablero o el duenho no pueda pagar lo acometido para deshipotecar.
     * @throws NoEsPropiedadJugador En caso de que la casilla no pertenezca al jugador que tiene el turno.
     * @throws ErrorHipoteca En caso de que ya estuviese deshipotecada.
     */
    public void deshipotecarCasilla(String nombreCasilla) throws ErrorJuego, ErrorHipoteca, NoEsPropiedadJugador {
        Propiedad deshipo = (Propiedad) tablero.buscarCasilla(nombreCasilla);
        if(deshipo==null){
            throw new NoExisteCasilla("Error: La casilla "+nombreCasilla+" no existe en el tablero.\n");
        }
        if(deshipo.getDuenho().getNombre().equals(jugadorTurno().getNombre())){
            deshipo.deshipotecar();
        }
        else{
            throw new NoEsPropiedadJugador("Error: El jugador "+ jugadorTurno().getNombre() +" no puede deshipotecar " + deshipo.getNombre() +".  No es una propiedad que le pertenece\n");
        }
    }

    @Override
    /** Método que implementa el comando describir <nombre_casilla> para mostrar la información de una casilla en concreto.
     *
     * @param nombreCasilla Nombre de la casilla de la que se quiere obtener la información.
     * @throws NoExisteCasilla En caso de que no exista la casilla en el tablero.
     */
    public void describirCasilla(String nombreCasilla) throws NoExisteCasilla {
        //buscamos la casilla en el tablero
        Casilla casilla = tablero.buscarCasilla(nombreCasilla);
        //verificamos q sea diferente de null para printear su informacion
        if (casilla != null) {
            consolaNormal.imprimir(casilla.infoCasilla());
        }
        //en el caso contrario informamos de que no existe dicha casilla
        else {
            throw new NoExisteCasilla("Error: La casilla "+nombreCasilla+" no existe en el tablero.\n");
        }
    }

    @Override
    /** Método que implementa el comando describir jugador <nombre_jugador> para mostrar la información de un jugador en concreto.
     *
     * @param nombreJugador Nombre del jugador del que se quiere obtener la información.
     */
    public void describirJugador(String nombreJugador) {
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equals(nombreJugador)) {
                StringBuilder mensaje = new StringBuilder("{\n\tnombre: " + jugador.getNombre() +
                        ",\n\tavatar: " + jugador.getAvatar() +
                        ",\n\tfortuna: " + (int) jugador.getFortuna());
                //Recorremos la lista de propiedades del jugador para printearlas si las tiene
                if (!jugador.getPropiedades().isEmpty()) {
                    mensaje.append(",\n\tpropiedades: [");
                    for (Propiedad solar : jugador.getPropiedades()) {
                        mensaje.append(solar.getNombre());
                        if (solar != jugador.getPropiedades().getLast()) {
                            mensaje.append(", ");
                        }
                    }
                    mensaje.append("]");
                }
                //Recorremos la lista de edificios del jugador para printearlos si los tiene
                if (!jugador.getEdificios().isEmpty()) {
                    mensaje.append("\n\tedificios: [");
                    int j = 0;
                    //recorremos la lista de edificios del jugador para printearlas
                    for (Edificio edificio : jugador.getEdificios()) {
                        mensaje.append(edificio.getClass().getSimpleName()).append("-").append(edificio.getIdentificador());
                        if (j < jugador.getEdificios().size() - 1) {
                            mensaje.append(",");
                            j++;
                        }
                    }
                    mensaje.append("]\n\t");
                }
                consolaNormal.imprimir(mensaje + "\n}\n");
            }
        }
    }

    @Override
    /** Método que implementa el comando salir carcel para que el jugador pague la multa y salga de la carcel.
     *
     * @throws ErrorJuego En caso de que no haya jugadores en la partida.
     */
    public void salirCarcel() throws ErrorJuego {
        Jugador jugador = jugadorTurno();
        jugador.setEnCarcel(false);
        jugador.sumarGastos(Valor.IMPUESTO_CARCEL);
        jugador.sumarFortuna(-Valor.IMPUESTO_CARCEL);
        consolaNormal.imprimir(jugador.getNombre() + " paga " + Valor.IMPUESTO_CARCEL + " y sale de la carcel. Puede lanzar los dados\n");
    }

    /** Método que implementa el comando estadisticas partida para mostrar las estadísticas de la partida.
     *
     */
    @Override
    public void estadisticasPartida() {
        Casilla masvisitada = null;
        Casilla masrentable = null;
        float rentabilidadmax = 0;
        int maxvisitas = 0;
        // recorremos todas las casillas del tablero para obtener la casilla mas visitada y la mas rentable
        for(ArrayList <Casilla> lado : tablero.getPosiciones()){
            for (Casilla c : lado) {
                if (c.getVisitada() > maxvisitas) {
                    maxvisitas = c.getVisitada();
                    masvisitada = c;
                }
                // diferenciamos entre las casillas que pertenecen a la banca y las que no para obtener la mas rentable entre las que no pertenecen a la banca
                if(c instanceof Propiedad p){
                    if(!p.getDuenho().equals(banca)){
                        // calculamos la rentabilidad de la casilla y comprobamos si es mayor que la maxima actual actualizandola en caso afirmativo
                        if((p.getGenerado()/p.gastosTotales()) > rentabilidadmax){
                            rentabilidadmax = (p.getGenerado()/p.gastosTotales());
                            masrentable = c;
                        }
                    }
                }

            }
        }
        Grupo gruporentable=null;
        float rentabilidadmaxgrupo=0;
        ArrayList<String> nombresgrupos= new ArrayList<>();
        nombresgrupos.add("Marron");
        nombresgrupos.add("Cyan");
        nombresgrupos.add("Rosa");
        nombresgrupos.add("Salmon");
        nombresgrupos.add("Rojo");
        nombresgrupos.add("Amarillo");
        nombresgrupos.add("Verde");
        nombresgrupos.add("Azul");
        // recorremos todos los grupos del tablero para obtener el grupo mas rentable
        for (String nombregrupo : nombresgrupos) {
            // sacamos el grupo del tablero y comprobamos su rentabilidad
            Grupo g = tablero.getGrupos().get(nombregrupo);
            // si la rentabilidad del grupo es mayor que la maxima actual actualizamos la maxima y el grupo mas rentable
            if(g.rentabilidadgrupo()>rentabilidadmaxgrupo){
                rentabilidadmaxgrupo=g.rentabilidadgrupo();
                gruporentable=g;
            }
        }
        Jugador mayorvueltas = null;
        int maxvueltas = 0;
        // recorremos todos los jugadores para obtener el que mas vueltas ha dado
        for (Jugador j : jugadores) {
            // si el numero de vueltas del jugador es mayor que el maximo actual actualizamos el maximo y el jugador con mas vueltas
            if (j.getVueltas() > maxvueltas) {
                maxvueltas = j.getVueltas();
                mayorvueltas = j;
            }
        }
        Jugador mayorfortuna = JugadorMayorFortuna();
        // printeamos las estadisticas obtenidas en caso de que existan.
        System.out.println("{\n");
        if(masrentable!=null){
            System.out.println("casillaMasRentable: " + masrentable.getNombre() + ",\n ");
        }
        if(gruporentable!=null){
            System.out.println("grupoMasRentable: " + gruporentable + ",\n ");
        }
        if(masvisitada!=null){
            System.out.println("casillaMasFrecuentada: " + masvisitada.getNombre() + ",\n ");
        }
        if(mayorvueltas!=null){
            System.out.println("jugadorMasVueltas " + mayorvueltas.getNombre() + " ,\n ");
        }
        if(mayorfortuna!=null){
            System.out.println("jugadorEnCabeza: " + mayorfortuna.getNombre() + "\n ");
        }
        System.out.println("}\n");
    }


    /** Método privado para sacar el jugador con mayor fortuna de la partida**/
    private Jugador JugadorMayorFortuna() {
        float maxfortuna = 0;
        Jugador mayorfortuna = null;
        // recorremos todos los jugadores para obtener el que tiene mayor fortuna total (fortuna + valor de propiedades + valor de edificios)
        for (Jugador j : jugadores) {
            // calculamos la fortuna total del jugador
            // comenzamos con su fortuna actual
            float fortunaactual = j.getFortuna();
            // le sumamos el valor de sus propiedades
            ArrayList<Propiedad> propiedades = j.getPropiedades();
            for (Propiedad c : propiedades) {
                fortunaactual += c.getValor();
            }
            // le sumamos el valor de sus edificios
            ArrayList<Edificio> edificios = j.getEdificios();
            for (Edificio e : edificios) {
                fortunaactual += e.getPrecio();
            }
            // si la fortuna total del jugador es mayor que la maxima actual actualizamos la maxima y el jugador con mayor fortuna
            if (fortunaactual > maxfortuna) {
                maxfortuna = fortunaactual;
                mayorfortuna = j;
            }
        }
        return mayorfortuna;
    }


    @Override
    /** Método que implementa el comando estadisticas jugador <nombreJugador> para mostrar las estadísticas de un jugador específico.
     *
     * @param nombreJugador Nombre del jugador cuyas estadísticas se desean mostrar.
     */
    public void estadisticasJugador(String nombreJugador) {
        Jugador jugador = null;
        // buscamos el jugador con el nombre indicado
        for (Jugador j : jugadores) {
            if (j.getNombre().equals(nombreJugador)) {
                jugador = j;
                break;
            }
        }
        // en caso de que el jugador exista printeamos sus estadisticas
        if(jugador != null) {
            float dineroInvertido = 0;
            // sacamos el valor de gastos totales de todas las propiedades del jugador y los sumamos dentro de la variable dineroInvertido
            for(Propiedad casilla:jugador.getPropiedades()){
                dineroInvertido += casilla.gastosTotales();
            }
            // sacamos el valor de gastos totales por impuestos
            float pagoTasasEImpuestos = jugador.getDineroPorImpuestos();
            // sacamos el valor de dinero pagado por alquileres
            float pagodealquileres = jugador.getDineroPorAlquileres();
            // sacamos el valor de dinero cobrado por alquileres
            float cobrodealquileres = jugador.getCobrosPorAlquileres();
            // sacamos el valor de dinero cobrado por pasar por la casilla de salida
            float pasarporcasilladesalida = jugador.getVueltas() * SUMA_VUELTA;
            // sacamos el valor de premios por inversiones o bote
            float premiosinversionesobote = jugador.getPremiosInversionesOBote();
            // sacamos el numero de veces que el jugador ha ido a la carcel
            int vecesenlacarcel = jugador.getVecesCarcel();
            System.out.println("{\n");
            System.out.println("dineroInvertido: " + (int) dineroInvertido + ",\n");
            System.out.println("pagoTasasEImpuestos: " + (int) pagoTasasEImpuestos + ",\n");
            System.out.println("pagodealquileres: " + (int) pagodealquileres + ",\n");
            System.out.println("cobrodealquileres: " + (int) cobrodealquileres + ",\n");
            System.out.println("pasarporcasilladesalida: " + (int) pasarporcasilladesalida + ",\n");
            System.out.println("premiosinversionesobote: " + (int) premiosinversionesobote + ",\n");
            System.out.println("vecesenlacarcel: " + vecesenlacarcel + "\n");
            System.out.println("}\n");
        }
        // en caso de que el jugador no exista informamos de ello
        else{
            System.out.println("El jugador " + nombreJugador + " no existe en la partida.\n");
        }

    }

    /** Método privado para realizar la tirada de dados y mover el avatar del jugador en consecuencia, implementando los
     * comandos lanzar dados (OPCIONAL: <tirada1><tirada2>)
     *
     * @param tirada1 en caso de ser una tirada forzada, valor del primer dado.
     * @param tirada2 en caso de ser una tirada forzada, valor del segundo dado.
     * @param jugador el jugador que realiza la tirada.
     * @param random booleano que indica si la tirada es aleatoria o forzada.
     * @throws ErrorJuego cuando no hay jugadores en la partida o si falla algo en la función gestionarAccionesCartas() en caso de que este jugador tenga que sacar un carta en su turno.
     */
    private void realizarTirada(int tirada1, int tirada2, Jugador jugador, boolean random) throws ErrorJuego {
        tirado = false;
        if(random) {
            dado1.hacerTirada();
            dado2.hacerTirada();
        }
        else{
            dado1.hacerTirada(tirada1);
            dado2.hacerTirada(tirada2);
        }
        //comprobamos q el jugador no haya tirado ya y no este en la carcel
        if (!tirado && !jugador.getenCarcel()) {
            //movemos el avatar del jugador la suma de ambos dados
            jugador.getAvatar().moverAvatar(tablero.getPosiciones(), dado1.getValor() + dado2.getValor());
            //verificamos si se han sacado dobles o no y se actualiza el contador de lanzamientos
            tirado = !(dado1.sonDobles(dado2));
            if (!tirado) {
                lanzamientos += 1;
            }
            //si se han sacado dobles 3 veces seguidas el jugador va a la carcel
            if (lanzamientos == 3 && dado1.sonDobles(dado2)) {
                consolaNormal.imprimir("El jugador " + jugadorTurno().getNombre() + " ha tirado dobles tres veces seguidas y va a la carcel.\n");
                jugadorTurno().encarcelar(tablero.getPosiciones());
                tirado = true;
            }
            tablero.setHayCarta(false); // Preseteamos a que no hay carta
            gestionarAccionesCartas(); // Si hay carta ya se procesa aquí
            consolaNormal.imprimir(tablero.toString()); // Ya imprime la carta si la hay
            // comprobamos si el jugador no ha tirado ya y si esta en la carcel
        } else if (!tirado && jugador.getenCarcel()) {
            //actualizamos las tiradas en la carcel del jugador
            jugador.setTiradasCarcel(jugadorTurno().getTiradasCarcel() + 1);
            //verificamos si son dobles para comprobar si sale de la carcel y como ha sacado dobles reseteamos sus tiradas en la carcel y le dejamos q pueda mover su avatar y pueda volver a tirar
            if (dado1.sonDobles(dado2)) {
                consolaNormal.imprimir("El jugador " + jugadorTurno().getNombre() + " ha sacado dobles y sale de la carcel.\n");
                jugador.setEnCarcel(false);
                jugador.setTiradasCarcel(0);
                jugador.getAvatar().moverAvatar(tablero.getPosiciones(), dado1.getValor() + dado2.getValor());
                tablero.setHayCarta(false); // Preseteamos a que no hay carta
                gestionarAccionesCartas(); // Si hay carta ya se procesa aquí
                consolaNormal.imprimir(tablero.toString()); // Ya imprime la carta si la hay
                tirado = false;
            }
            //si no ha sacado dobles y ya ha tirado 3 veces en la carcel paga el impuesto y sale de la carcel moviendo su avatar
            if (!dado1.sonDobles(dado2) && jugadorTurno().getTiradasCarcel() > 2) {
                salirCarcel();
                jugador.getAvatar().moverAvatar(tablero.getPosiciones(), dado1.getValor() + dado2.getValor());
                tablero.setHayCarta(false); // Preseteamos a que no hay carta
                gestionarAccionesCartas(); // Si hay carta ya se procesa aquí
                consolaNormal.imprimir(tablero.toString()); // Ya imprime la carta si la hay
            }
        } else {
            consolaNormal.imprimir("El jugador " + jugador.getNombre() + " ya ha tirado los dados este turno.\n");
        }
    }

    /** Método privado para comprobar si el tipo de edificio existe.
     *
     * @param tipoEdificio String para comprobar si es un tipo de edificio existente en el juego.
     * @return boolean si ese tipo existe o no.
     */
    private boolean existeTipoEdificio(String tipoEdificio) {
        return switch (tipoEdificio) {
            case "Casa", "Casas", "Hotel", "Piscina", "Pista de deporte" -> true;
            default -> false;
        };
    }

    /** Método privado para obtener el jugador al que le toca jugar en el turno actual.
     *
     * @return Jugador al que le toca jugar
     * @throws ErrorJuego cuando no hay jugadores en la partida
     */
    private Jugador jugadorTurno() throws ErrorJuego{
        if(!jugadores.isEmpty()) {
            return jugadores.get(turno);
        }
        else{
            throw new ErrorJuego("Todavía no has creado ningún jugador, no puede realizar ese comando");
        }
    }


    /** Método privado que se ejecuta después de cada tirada para comprobar si el Avatar ha caído en una casilla de Accion, ejecutando su accion.
     *
     * @throws ErrorJuego cuando no hay jugadores en la partida o si el efecto de la Suerte o Comunidad puede dejar al jugador actual sin dinero.
     */
    private void gestionarAccionesCartas() throws ErrorJuego {
        Casilla lugarJugadorActual = jugadorTurno().getAvatar().getLugar();
        if(lugarJugadorActual instanceof Suerte suertecasilla) {
            tablero.setHayCarta(true); // Marcamos que se coge un carta
            suertecasilla.accion(suerte%Valor.NUM_SUERTE, jugadorTurno(), this.jugadores);
            CartaSuerte cartaActual = suertecasilla.getCartas().get(suerte%Valor.NUM_SUERTE);
            tablero.setCartaActual(cartaActual);
            suerte++;
        }
        else if(lugarJugadorActual instanceof CajaComunidad cajaComunidad){
            tablero.setHayCarta(true); // Marcamos que se coge un carta
            cajaComunidad.accion(comunidad%Valor.NUM_COMUNIDAD, jugadorTurno(), this.jugadores);
            CartaComunidad cartaActual = cajaComunidad.getCartas().get(comunidad%Valor.NUM_COMUNIDAD);
            tablero.setCartaActual(cartaActual);
            comunidad++;

        }
        else if(lugarJugadorActual instanceof Parking parking){
            parking.accion(jugadorTurno());
        }
    }

    /** Método que implemtenta el comando trato <jugador_receptor> cambiar: (<oferta>, <demanda>) donde oferta
     * y demanda pueden ser una propiedad o una cantidad de dinero, siendo posible cambiar dinero y propiedad por propiedad
     * o propiedad por dinero, pero nunca dinero por dinero o propiedad por dinero y dinero.
     *
     * @param partes String que contiene las partes del comando separadas por espacios
     * @throws NoEsPropiedadJugador cuando uno de los jugadores ofrece/se le demanda una propiedad que no le pertenece
     * @throws ErrorJuego cuando no hay jugadores en la partida, no existe alguna de las Casillas a intercambiar o cuando uno de los jugadores no tiene suficiente dinero.
     */
    public void trato(String[] partes) throws NoEsPropiedadJugador, ErrorJuego {
        String receptor = partes[1].replace(":", "");
        /* Checkeamos que exista el jugador al que le estamos haciendo el trato */
        Jugador receptorJugador = null;
        for(Jugador jugador: jugadores){
            if(jugador.getNombre().equals(receptor)) {
                receptorJugador = jugador;
            }
        }


        if(partes.length==5){
            String oferta = partes[3].replace("(", "").replace(",", "");
            String peticion = partes[4].replace(")", "");
            boolean ofertaesDinero = esNumero(oferta);
            boolean peticionesDinero = esNumero(peticion);


            if(receptorJugador== null){
                throw new NoExisteJugador("No se puede proponer el trato: El jugador " + receptor + " no existe.");
            }

            /* Validamos ofertas de propiedad por propiedad */

            if(!ofertaesDinero && !peticionesDinero){
                Propiedad ofertaPropiedad = (Propiedad) tablero.buscarCasilla(oferta);
                if(ofertaPropiedad == null){
                    throw new NoExisteCasilla("Error: No se puede proponer el trato: La casilla " + oferta + " no existe en el tablero.");
                }
                if(ofertaPropiedad.getDuenho() != jugadorTurno()){
                    throw new NoEsPropiedadJugador("Error: No se puede proponer el trato: No puedes ofrecer una propiedad que no te pertenece.");
                }
                Propiedad peticionPropiedad = (Propiedad)tablero.buscarCasilla(peticion);
                if(peticionPropiedad == null){
                    throw new NoExisteCasilla("Error: No se puede proponer el trato: La casilla " + peticion + " no existe en el tablero.");
                }
                if(!peticionPropiedad.getDuenho().getNombre().equals(receptor)){
                    throw new NoEsPropiedadJugador("Error: No se puede proponer el trato: "+ peticion + " no pertenece a " + receptor + ".");
                }
                Trato trato = new Trato("trato" + (numerotratos++), jugadorTurno(), receptorJugador, ofertaPropiedad, peticionPropiedad,0,0);
                receptorJugador.anhadirtrato(trato);

                consolaNormal.imprimir(receptor+", ¿te doy "+oferta+" y me das "+peticion+" ?");
            }

            /* Validamos oferta de propiedad por dinero */

            else if(!ofertaesDinero && peticionesDinero) {
                Casilla ofertaPropiedad = tablero.buscarCasilla(oferta);
                if(ofertaPropiedad == null){
                    throw new NoExisteCasilla("Error: No se puede proponer el trato: La casilla " + oferta + " no existe en el tablero.");
                }
                if(!(ofertaPropiedad instanceof Propiedad)) {
                    consolaNormal.imprimir("No se puede proponer el trato: La casilla " +ofertaPropiedad + " no es propiedad.");
                    return;
                }
                if (((Propiedad)ofertaPropiedad).getDuenho() != jugadorTurno()) {
                    throw new NoEsPropiedadJugador("Error: No se puede proponer el trato: No puedes ofrecer una propiedad que no te pertenece.");
                }
                float dineroPeticion = Float.parseFloat(partes[4].replace(")", ""));

                try {
                    if (receptorJugador.puedePagar(dineroPeticion)) {
                        Trato trato = new Trato("trato" + (numerotratos++), jugadorTurno(), receptorJugador, (Propiedad) ofertaPropiedad, null, 0, dineroPeticion);
                        receptorJugador.anhadirtrato(trato);

                        consolaNormal.imprimir(receptor + ", ¿te doy " + oferta + " y me das " + peticion + " ?");
                    }
                }
                catch (NoPuedePagar e){ // Manejamos la excepción porque no queremos que se declare en Bancarrota, es solo una verificación.
                    consolaNormal.imprimir("Trato cancelado automáticamente: " + receptor + " no tiene suficiente dinero para aceptar el trato.");
                }
            }
            else if(ofertaesDinero && !peticionesDinero){
                Casilla peticionPropiedad = tablero.buscarCasilla(peticion);
                if(peticionPropiedad == null){
                    throw new NoExisteCasilla("Error: No se puede proponer el trato: La casilla " + peticion + " no existe en el tablero.");
                }

                if(!(peticionPropiedad instanceof Propiedad)) {
                    consolaNormal.imprimir("No se puede proponer el trato: La casilla " + peticionPropiedad + " no es propiedad.");
                    return;
                }

                if(!((Propiedad)peticionPropiedad).getDuenho().getNombre().equals(receptor)){
                    throw new NoEsPropiedadJugador ("Error: No se puede proponer el trato: "+ peticion + " no pertenece a " + receptor + ".");
                }
                float dineroOferta = Float.parseFloat(partes[3].replace("(", "").replace(",",""));

                try {
                    if (receptorJugador.puedePagar(dineroOferta)) {
                        Trato trato = new Trato("trato"+(numerotratos++), jugadorTurno(), receptorJugador, null, (Propiedad) peticionPropiedad, dineroOferta,0);
                        receptorJugador.anhadirtrato(trato);

                        consolaNormal.imprimir(receptor + ", ¿te doy "+oferta+" y me das "+peticion+" ?");

                    }
                } catch(NoPuedePagar e){ // Manejamos la excepción porque no queremos que se declare en Bancarrota, es solo una verificación.
                    consolaNormal.imprimir("Trato cancelado automáticamente: " + receptor + " no tiene suficiente dinero para aceptar el trato.");
                    return;
                }

            }
            else{
                consolaNormal.imprimir("No se puede proponer el trato: No son válidos los intercambios con solo cash, debes incluír al menos una propiedad.");
            }
         }
        /* Verificamos tratos con dos elementos de uno de los lados*/
        else if (partes.length==7) {
            /* Los que la oferta es doble */

             if (partes[4].equals("y")) {
                 // trato maria: cambiar (Solar1 y 200, Solar2)
                 String peticion = partes[6].replace(")", "");
                 String oferta1 = partes[3].replace("(", "");
                 String oferta2 = partes[5].replace(",", "");
                 Casilla ofertaPropiedad = null;
                 Casilla peticionPropiedad = tablero.buscarCasilla(peticion);
                 if(peticionPropiedad == null){
                     consolaNormal.imprimir("Error: No se puede proponer el trato: La casilla " + peticion + " no existe en el tablero.");
                 }
                 if(!(peticionPropiedad instanceof Propiedad)) {
                     consolaNormal.imprimir("No se puede proponer el trato: La casilla " + peticionPropiedad + " no es propiedad.");
                     return;
                 }
                 float dinerooferta = 0;


                 // Miramos en que orden están las peticiones <dinero> <y> <propiedad> o <propiedad> <y> <dinero>
                 if(esNumero(oferta1) && esNumero(oferta2)){
                     consolaNormal.imprimir("No se puede proponer un trato <propiedad> por <dinero y dinero>. Haz solo <propiedad> por <dinero_total>");
                     return;
                 }
                 else if(esNumero(oferta1)){ // Si el dinero va primero, ya sabemos que la propiedad es lo segundo
                     ofertaPropiedad = tablero.buscarCasilla(oferta2);
                     if(ofertaPropiedad == null){
                         consolaNormal.imprimir("Error: No se puede proponer el trato: La casilla " + oferta1 + " no existe en el tablero.");
                     }
                     if(!(ofertaPropiedad instanceof Propiedad)) {
                         consolaNormal.imprimir("No se puede proponer el trato: La casilla " +ofertaPropiedad + " no es propiedad.");
                         return;
                     }
                     dinerooferta = Float.parseFloat(oferta1);
                 }
                 else{ // Si el dinero va después, ya sabemos que la propiedad es lo primero
                     ofertaPropiedad = tablero.buscarCasilla(oferta1);
                     if(ofertaPropiedad == null){
                         consolaNormal.imprimir("Error: No se puede proponer el trato: La casilla " + oferta1 + " no existe en el tablero.");
                     }
                     if(!(ofertaPropiedad instanceof Propiedad)) {
                         consolaNormal.imprimir("No se puede proponer el trato: La casilla " +ofertaPropiedad + " no es propiedad.");
                         return;
                     }
                     dinerooferta = Float.parseFloat(oferta2);
                 }

                 if (!((Propiedad)ofertaPropiedad).getDuenho().equals(jugadorTurno())) {
                     throw new NoEsPropiedadJugador("Error: No se puede proponer el trato: No puedes ofrecer una propiedad que no te pertenece.");
                 }
                 if (!((Propiedad)peticionPropiedad).getDuenho().getNombre().equals(receptor)) {
                     throw new NoEsPropiedadJugador("Error: No se puede proponer el trato: " + partes[4] + " no pertenece a " + receptor + ".");
                 }

                 try {
                     if (receptorJugador.puedePagar(dinerooferta)) {
                         Trato trato = new Trato("trato" + (numerotratos++), jugadorTurno(), receptorJugador, (Propiedad)ofertaPropiedad, (Propiedad)peticionPropiedad, dinerooferta,0);
                         receptorJugador.anhadirtrato(trato);

                         consolaNormal.imprimir(receptor + ", ¿te doy " + oferta1 + " y " + oferta2 + " y me das " + peticion + " ?");
                     }
                 } catch (NoPuedePagar e) { // Manejamos la excepción porque no queremos que se declare en Bancarrota, es solo una verificación.
                     consolaNormal.imprimir("Trato cancelado automáticamente: " + receptor + " no tiene suficiente dinero para aceptar el trato.");
                 }
             }
             /* Los que la petición es doble */
             //trato maria: cambiar (Solar1, Solar2 y 200)
             else {
                 String oferta = partes[3].replace("(", "").replace(",", "");
                 String peticion1 = partes[4];
                 String peticion2 = partes[6].replace(")", "");
                 Casilla ofertaPropiedad = tablero.buscarCasilla(oferta);
                 if(ofertaPropiedad == null){
                     consolaNormal.imprimir("Error: No se puede proponer el trato: La casilla " + oferta + " no existe en el tablero.");
                 }
                 if(!(ofertaPropiedad instanceof Propiedad)) {
                     consolaNormal.imprimir("No se puede proponer el trato: La casilla " + oferta + " no existe en el tablero.");
                     return;
                 }
                 Casilla peticionPropiedad = null;
                 float dineroPeticion = 0;

                 // Miramos en que orden están las peticiones <dinero> <y> <propiedad> o <propiedad> <y> <dinero>
                 if(esNumero(peticion1) && esNumero(peticion2)){
                     consolaNormal.imprimir("No se puede proponer un trato <propiedad> por <dinero y dinero>. Haz solo <propiedad> por <dinero_total>");
                     return;
                 }
                 else if(esNumero(peticion1)){ // Si el dinero va primero, ya sabemos que la propiedad es lo segundo
                     peticionPropiedad = tablero.buscarCasilla(peticion2);
                     if(peticionPropiedad == null){
                         consolaNormal.imprimir("Error: No se puede proponer el trato: La casilla " + peticion2 + " no existe en el tablero.");
                     }
                     if(!(peticionPropiedad instanceof Propiedad)) {
                         consolaNormal.imprimir("No se puede proponer el trato: La casilla " + peticion2 + " no existe en el tablero.");
                         return;
                     }
                    dineroPeticion = Float.parseFloat(peticion1);
                 }
                 else{ // Si el dinero va después, ya sabemos que la propiedad es lo primero
                     peticionPropiedad = tablero.buscarCasilla(peticion1);

                     if(peticionPropiedad == null){
                         consolaNormal.imprimir("Error: No se puede proponer el trato: La casilla " + peticion2 + " no existe en el tablero.");
                     }

                     if(!(peticionPropiedad instanceof Propiedad)) {
                         consolaNormal.imprimir("No se puede proponer el trato: La casilla " + peticion1 + " no es propiedad.");
                         return;
                     }
                     dineroPeticion = Float.parseFloat(peticion2);
                 }

                 if (!((Propiedad)ofertaPropiedad).getDuenho().equals(jugadorTurno())) {
                     throw new NoEsPropiedadJugador("Error: No se puede proponer el trato: No puedes ofrecer una propiedad que no te pertenece.");
                 }
                 if (!((Propiedad)peticionPropiedad).getDuenho().getNombre().equals(receptor)) {
                     throw new NoEsPropiedadJugador("Error: No se puede proponer el trato: " + partes[4] + " no pertenece a " + receptor + ".");
                 }

                 try {
                     if (receptorJugador.puedePagar(dineroPeticion)) {
                         Trato trato = new Trato("trato" + (numerotratos++), jugadorTurno(), receptorJugador, (Propiedad)ofertaPropiedad, (Propiedad)peticionPropiedad, 0, dineroPeticion);
                         receptorJugador.anhadirtrato(trato);

                         consolaNormal.imprimir(receptor + ", ¿te doy " + oferta + " y me das " + peticion1 + " y " + peticion2 + " ?");
                     }
                 } catch (NoPuedePagar e) { // Manejamos la excepción porque no queremos que se declare en Bancarrota, es solo una verificación.
                     consolaNormal.imprimir("Trato cancelado automáticamente: " + receptor + " no tiene suficiente dinero para aceptar el trato.");
                 }


             }
         }
    }

    /** Función que implementa el comando <aceptar> <tratoid> para aceptar un trato recibido por el jugador en turno.
     *
     * @param idtrato identificador de trato a aceptar
     * @throws ErrorJuego si no existe ningún jugador en la partida.
     */
    public void aceptarTrato(String idtrato) throws ErrorJuego{
        for(Trato trato: jugadorTurno().getTratos()){
            if(trato.getIdtrato().equals(idtrato)){
                trato.ejecutarTrato();
                jugadorTurno().eliminartrato(idtrato);
                return;
            }
        } // si llega aquí es que no ha encontrado el trato
        consolaNormal.imprimir("El jugador " + jugadorTurno().getNombre() + " no ha recibido ningún trato con el id: " + idtrato);
    }


    /** Función que implementa el comando <eliminar> <tratoid> para eliminar un trato recibido por el jugador en turno.
     *
     * @param idtrato identificador de trato a eliminar
     * @throws ErrorJuego si no existe ningún jugador en la partida.
     */
    public void  eliminarTrato(String idtrato) throws ErrorJuego {
        jugadorTurno().eliminartrato(idtrato);
    }


    /** Función que implementa el comando <tratos> para listar los tratos recibidos por el jugador en turno.
     *
     * @throws ErrorJuego si no existe ningún jugador en la partida.
     */
    public void listarTratos() throws ErrorJuego {
        for (Trato trato : jugadorTurno().getTratos()) {
            consolaNormal.imprimir(trato.toString());
            if(!trato.equals(jugadorTurno().getTratos().getLast())){
                consolaNormal.imprimir(",\n");
            }
        }
    }


    /** Función para comprobar si una cadena es un número o no (en punto flotante).
     *
     * @param s
     * @return boolean indicando si es un número
     */
    public boolean esNumero(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

