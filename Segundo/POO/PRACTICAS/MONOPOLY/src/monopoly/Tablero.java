package monopoly;


import monopoly.carta.*;
import monopoly.casilla.Casilla;
import monopoly.casilla.Impuesto;
import monopoly.casilla.accion.CajaComunidad;
import monopoly.casilla.accion.Parking;
import monopoly.casilla.accion.Suerte;
import monopoly.casilla.*;
import monopoly.casilla.propiedad.*;
import partida.*;

import java.util.ArrayList;
import java.util.HashMap;


public class Tablero {
    /**
     * posiciones: ArrayList de ArrayLists de Casillas que representan las posiciones del tablero.
     */
    private ArrayList<ArrayList<Casilla>> posiciones;
    /**
     *  grupos: HashMap que almacena los grupos de solares del tablero con clave String (color del grupo).
     */
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    /**
     * banca: Jugador que representa la banca del juego.
     */
    private Jugador banca;
    /**
     * hayCarta: booleano que indica si se sacó una carta en el último lanzamiento de dados.
     */
    private boolean hayCarta;
    /**
     * cartaActual: Carta que representa la carta sacada en el último lanzamiento de dados.
     */
    private Carta cartaActual;


    /**Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
     *
     * @param banca: jugador banca
     */
    public Tablero(Jugador banca) {
        this.hayCarta = false;
        this.cartaActual = null;
        this.banca = banca;
        this.posiciones = new ArrayList<>();
        this.grupos = new HashMap<>();
        this.generarCasillas();
    }

    /**
     * Getter de posiciones
     * @return
     */
    public ArrayList<ArrayList<Casilla>> getPosiciones() {
        return posiciones;
    }

    /**
     * Getter de grupos
     * @return
     */
    public HashMap<String, Grupo> getGrupos() {
        return grupos;
    }

    /**
     * Getter de banca
     * @return
     */
    public Jugador getBanca() {
        return banca;
    }


    /**
     * Getter de hayCarta
     * @return
     */
    public boolean getHayCarta() {
        return hayCarta;
    }

    /**
     * Getter de cartaActual
     * @return
     */
    public Carta getCartaActual() {
        return cartaActual;
    }

    /**
     * Setter de posiciones
     * @param banca: nueva banca
     */
    public void setBanca(Jugador banca) {
        this.banca = banca;
    }

    /**
     * Setter de posiciones
     * @param posiciones: nuevo ArrayList de ArrayLists de Casillas
     */
    public void setPosiciones(ArrayList<ArrayList<Casilla>> posiciones) {
        this.posiciones = posiciones;
    }

    /**
     * Setter de grupos
     * @param grupos: nuevo HashMap de grupos de solares.
     */
    public void setGrupos(HashMap<String, Grupo> grupos) {
        this.grupos = grupos;
    }

    /**
     * Setter de cartaActual
     * @param cartaActual: nueva cartaActual
     */
    public void setCartaActual(Carta cartaActual) {
        this.cartaActual = cartaActual;
    }


    /**
     * Setter de hayCarta
     * @param hayCarta: nuevo valor de hayCarta
     */
    public void setHayCarta(boolean hayCarta) {
        this.hayCarta = hayCarta;
    }


    /** Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
     *
     */
    private void generarCasillas() { //COMPROBAR VALORES
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }


    /** Método para asignar el grupo a las casillas de solar.
     *
     */
    private void asignarGrupo(String color) {
        // recorre las casillas de un grupo asignandoles el color de su propio grupo a cada una de ellas
        for (Solar s : this.grupos.get(color).getMiembros()) {
            s.setGrupo(this.grupos.get(color));
        }
    }

    /** Método para inicializar todo el lado norte del tablero [Solar12-Solar17].
     *
     */
    private void insertarLadoNorte() {
        //crea el arraylist de casillas del lado norte e inserta las casillas correspondientes con sus atributos correspondientes
        ArrayList<Casilla> norte = new ArrayList<>();
        Solar solar12 = new Solar("Solar12", 21, 2200000, 180000, banca);
        norte.add(solar12);
        solar12.ajustarAlquileres(2200000, 10500000, 2100000, 2100000);
        norte.add(new Suerte("Suerte",  22, this));
        Solar solar13 = new Solar("Solar13", 23, 2200000, 180000, banca);
        norte.add(solar13);
        solar13.ajustarAlquileres(2200000, 10500000, 2100000, 2100000);
        Solar solar14 = new Solar("Solar14", 24, 2400000, 200000, banca);
        norte.add(solar14);
        solar14.ajustarAlquileres(2325000, 11000000 , 2200000, 2200000);
        norte.add(new Transporte("Trans3", 25, 500000,250000, banca));
        Solar solar15 = new Solar("Solar15", 26, 2600000, 220000, banca);
        norte.add(solar15);
        solar15.ajustarAlquileres(2450000, 11500000, 2300000, 2300000);
        Solar solar16 = new Solar("Solar16", 27, 2600000, 220000, banca);
        norte.add(solar16);
        solar16.ajustarAlquileres(2450000, 11500000, 2300000, 2300000);
        norte.add(new Servicio("Serv2", 28, 500000, 50000, banca));
        Solar solar17 = new Solar("Solar17", 29, 2800000, 240000, banca);
        norte.add(solar17);
        solar17.ajustarAlquileres(2600000, 12000000, 2400000, 2400000);
        // inserta dentro del hashmap grupos los diferentes grupos con sus casillas correspondientes
        this.grupos.put("Rojo", new Grupo(solar12, solar13, solar14, "ROJO"));
        asignarGrupo("Rojo");
        this.grupos.put("Amarillo", new Grupo(solar15, solar16, solar17, "AMARILLO"));
        asignarGrupo("Amarillo");
        //por ultimo añade al arraylist de los arraylist de casillas el arraylist q posee el total de casillas del lado norte
        posiciones.add(2, norte);
        //la posicion 2 porque el lado sur ya está en la posicion 0 y el oeste en la posicion 1
    }

    /** Método para inicializar todo el lado sur del tablero [Solar1-Solar5].
     *
     */
    private void insertarLadoSur() {
        // declara y crea el arraylist de casillas del lado sur e inserta las casillas correspondientes con sus atributos correspondientes
        ArrayList<Casilla> sur = new ArrayList<>();
        Solar solar1 = new Solar("Solar1", 1, 600000,20000, banca);
        sur.add(solar1);
        solar1.ajustarAlquileres(400000, 2500000,50000,50000);
        sur.add(new CajaComunidad("Caja", 2, this));
        Solar solar2 = new Solar("Solar2", 3, 600000,40000, banca);
        sur.add(solar2);
        solar2.ajustarAlquileres(400000, 4500000,900000,900000);
        sur.add(new Impuesto("Imp1", 4, 2000000));
        sur.add(new Transporte("Trans1", 5, 500000,250000, banca));
        Solar solar3 = new Solar("Solar3", 6, 1000000, 60000, banca);
        sur.add(solar3);
        solar3.ajustarAlquileres(1000000, 5500000,1100000,1100000);
        sur.add(new Suerte("Suerte", 7, this));
        Solar solar4 = new Solar("Solar4",  8, 1000000, 60000, banca);
        sur.add(solar4);
        solar4.ajustarAlquileres(1000000, 5500000,1100000,1100000);
        Solar solar5 = new Solar("Solar5", 9, 1200000, 80000, banca);
        sur.add(solar5);
        solar5.ajustarAlquileres(1250000, 6000000,1200000,1200000);

        // se inserta dentro del hashmap grupos los diferentes grupos con sus casillas correspondientes y se le asigna el grupo a cada casilla
        this.grupos.put("Marron", new Grupo(solar1, solar2, "MARRON"));
        asignarGrupo("Marron");
        this.grupos.put("Cyan", new Grupo(solar3, solar4, solar5, "CYAN"));
        asignarGrupo("Cyan");
        //por ultimo añade al arraylist de los arraylist de casillas el arraylist q posee el total de casillas del lado sur en la primera posicion
        posiciones.addFirst(sur);
    }


    /** Método para inicializar todo el lado oeste del tablero [Carcel-Parking].
     *
     */
    private void insertarLadoOeste() {
        ArrayList<Casilla> oeste = new ArrayList<>();
        // inserta las casillas que se ubicaran en el oeste de nuestro tablero con sus atributos correspondientes
        oeste.add(new Especial("Carcel", 10));

        Solar solar6 = new Solar("Solar6", 11, 1400000, 100000, banca);
        oeste.add(solar6);
        solar6.ajustarAlquileres(1500000, 7500000,1500000,1500000);
        oeste.add(new Servicio("Serv1", 12, 500000, 50000 ,banca));
        Solar solar7 = new Solar("Solar7", 13, 1400000, 100000, banca);
        oeste.add(solar7);
        solar7.ajustarAlquileres(1500000, 7500000,1500000,1500000);
        Solar solar8 = new Solar("Solar8", 14, 1600000, 120000, banca);
        oeste.add(solar8);
        solar8.ajustarAlquileres(1750000, 9000000,1800000,1800000);
        oeste.add(new Transporte("Trans2", 15, 500000, 250000, banca));
        Solar solar9 = new Solar("Solar9", 16, 1800000, 140000, banca);
        oeste.add(solar9);
        solar9.ajustarAlquileres(1850000, 9500000,1900000,1900000);
        oeste.add(new CajaComunidad("Caja", 17, this));
        Solar solar10 = new Solar("Solar10", 18, 1800000, 140000, banca);
        oeste.add(solar10);
        solar10.ajustarAlquileres(1850000, 9500000,1900000,1900000);
        Solar solar11 = new Solar("Solar11", 19, 2200000, 160000, banca);
        oeste.add(solar11);
        solar11.ajustarAlquileres(2000000, 10000000,2000000,2000000);
        oeste.add(new Parking("Parking", 20));
        //insertamos los diferentes grupos con sus casillas correspondientes en el hashmap grupos asignandto tmb cada color de grupo a sus casillas correspondientes
        this.grupos.put("Rosa", new Grupo(solar6, solar7, solar8, "ROSA"));
        asignarGrupo("Rosa");
        this.grupos.put("Salmon", new Grupo(solar9, solar10, solar11, "SALMON"));
        asignarGrupo("Salmon");
        //por ultimo insertamos el lado oeste en el arraylist de arraylists de casillas en la posicion 1
        posiciones.add(1, oeste);
    }


    /** Método para inicializar todo el lado este del tablero [IrCarcel-Salida].
     *
     */
    private void insertarLadoEste() {
        ArrayList<Casilla> este = new ArrayList<>();
        //insertamos las casillas dentro de su grupo correspondientes con sus atributos ya establecidos
        este.add(new Especial("IrCarcel",  30));
        Solar solar18 = new Solar("Solar18", 31, 3000000, 260000, banca);
        este.add(solar18);
        solar18.ajustarAlquileres(2750000, 12750000, 2550000, 2550000);
        Solar solar19 = new Solar("Solar19", 32, 3000000, 260000, banca);
        este.add(solar19);
        solar19.ajustarAlquileres(2750000, 12750000, 2550000, 2550000);
        este.add(new CajaComunidad("Caja",  33, this));
        Solar solar20 = new Solar("Solar20", 34, 3200000, 280000, banca);
        este.add(solar20);
        solar20.ajustarAlquileres(3000000, 14000000, 2800000, 2800000);
        este.add(new Transporte("Trans4",  35, 500000, 250000, banca));
        este.add(new Suerte("Suerte", 36, this));
        Solar solar21 = new Solar("Solar21", 37, 3500000, 300000, banca);
        este.add(solar21);
        solar21.ajustarAlquileres(3250000, 17000000, 3400000, 3400000);
        este.add(new Impuesto("Imp2", 38, 2000000));
        Solar solar22 = new Solar("Solar22", 39, 4000000, 500000, banca);
        este.add(solar22);
        solar22.ajustarAlquileres(4250000, 20000000, 4000000, 4000000);
        este.add(new Especial("Salida",40));
        // insertamos los diferentes grupos con sus casillas correspondientes en el hashmap grupos asignando tmb cada color de grupo a sus casillas correspondientes
        this.grupos.put("Verde", new Grupo(solar18, solar19, solar20, "VERDE"));
        asignarGrupo("Verde");
        this.grupos.put("Azul", new Grupo(solar21, solar22, "AZUL"));
        asignarGrupo("Azul");
        //por ultimo añade al arraylist de los arraylist de casillas el arraylist q posee el total de casillas del lado este en la posicion 3
        posiciones.add(3, este);
    }

    /**
     * Reescritura del método toString para mostrar el tablero por consola.
     * @return String que representa el tablero.
     */
    @Override
    public String toString() {
        String tablero = "";
        tablero = tablero.concat("\n");
        // Imprimos |nombre| con Súperlineado y Subrayado de la última casilla del lado oeste (Parking)
        tablero = tablero.concat("|" + Valor.UNDER_OVER + this.colorearCasilla(this.getParking()) + "|" + Valor.RESET);


        // Vamos a imprimir todas las casillas del lado norte (Solar12-Solar17)
        for (Casilla casilla : getLado("NORTE")) {
            // Imprimimos |nombre| de cada casilla del lado norte con subrayado y superlineado
            tablero = tablero.concat(Valor.UNDER_OVER + this.colorearCasilla(casilla) + "|" + Valor.RESET);

        }

        // Imprimimos |nombre| con el estilo anterior de la primera casilla del lado este (IrCarcel)
        tablero = tablero.concat(Valor.UNDER_OVER + this.colorearCasilla(this.getIrCarcel()) + "|\n");

        // Iteramos desde la penúltima hasta la segunda casilla del lado OESTE, intercalando espacios y casillas del lado ESTE
        for (int i = 1; i < (this.getLado("OESTE").size()) - 1; i++) {
            // Se imprime |nombre| de cada casilla del lado OESTE (desde la penúltima hasta la segunda)
            tablero = tablero.concat("|" + Valor.UNDERLINE + this.colorearCasilla(this.getLado("OESTE").get((this.getLado("OESTE").size()) - i - 1)) + "|");

            // Si hay carta se dibuja la carta, si no espacios en blanco
            if(hayCarta) {
                tablero = tablero.concat(dibujarCarta().get(i-1));
            }
            else {
                // Se calculan los espacios en blanco para cubrir cada casilla del Norte o del Sur, para que sea cuadrado deben tener el mismo tamaño ya que hay las misma casillas en el Norte y en Sur
                int maximo = Math.max(MaxAnchuraCasilla(getLado("NORTE")), MaxAnchuraCasilla(getLado("SUR")));
                // Imprimimos tantos espacios como número de casillas
                tablero = tablero.concat(" ".repeat(getLado("NORTE").size() * maximo + Valor.NUM_BARRAS));
            }

            // Se imprime |nombre| de la casilla complementaria del lado ESTE (vamos desde la segunda hasta la penúltima)
            tablero = tablero.concat("|" + Valor.UNDERLINE + this.colorearCasilla(this.getLado("ESTE").get(i)) + "|\n");
        }

        //Imprimimos |nombre| de la primera casilla del lado OESTE (la Carcel)
        tablero = tablero.concat("|" + Valor.UNDERLINE + this.colorearCasilla(this.getCarcel()) + "|");

        //Se imprime |nombre| de las casilla del lado SUR en orden inverso (Solar1-Solar5)
        for (int i = this.getLado("SUR").size() - 1; i >= 0; i--) {
            Casilla casilla = this.getLado("SUR").get(i);
            tablero = tablero.concat(Valor.UNDER_OVER + this.colorearCasilla(casilla) + "|");
        }

        //Se imprime |nombre| de la última casilla del lado ESTE (la Salida)
        tablero = tablero.concat(Valor.UNDERLINE + this.colorearCasilla(this.getInicio()) + "|\n");
        return tablero;
    }

    /**
     * Reescritura del método equals para determinar que 2 tableros son iguales si tienen la mismas posiciones, grupos y banca
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tablero tablero = (Tablero) obj;
        return posiciones.equals(tablero.posiciones) && grupos.equals(tablero.grupos) && banca.equals(tablero.banca);
    }

    /**
     * Método que busca una casilla por su nombre
     * @param nombre nombre de la casilla a buscar
     * @return la casilla encontrada o null si no existe
     */
    public Casilla buscarCasilla(String nombre) {
        for (ArrayList<Casilla> lado : posiciones) { //Recorremos todos los lados
            for (Casilla casilla : lado) { //Recorremos todas las casillas de cada lado
                if (casilla.getNombre().equals(nombre)) {
                    return casilla;
                }
            }
        }
        return null;
    }


    /**
     * Método auxiliar para obtener un lado del tablero por su nombre
     * @param lado nombre del lado (NORTE, SUR, OESTE, ESTE)
     * @return lista de casillas del lado o null si no existe
     */
    public ArrayList<Casilla> getLado(String lado) {
        //se retorna el arraylist de casillas correspondiente al lado solicitado
        return switch (lado) {
            case "NORTE" -> posiciones.get(2);
            case "SUR" -> posiciones.get(0);
            case "OESTE" -> posiciones.get(1);
            case "ESTE" -> posiciones.get(3);
            default -> null;
        };
    }

    /**
     * Método auxiliar para obtener la casilla 'IrCarcel'
     * @return la casilla IrCarcel
     */
    public Casilla getIrCarcel() {
        return this.getLado("ESTE").getFirst(); //La casilla de ir carcel está en el lado este, en la posición 0.
    }

    /**
     * Obtiene la casilla de inicio/salida
     * @return la casilla de salida
     */
    public Casilla getInicio() {
        return this.getLado("ESTE").get(10); //La casilla de salida está en el lado este, en la posición 10 (empezando a contar desde 0).
    }

    /**
     * Obtiene la casilla de la cárcel
     * @return la casilla Carcel
     */
    public Casilla getCarcel() {
        return this.getLado("OESTE").getFirst(); //La casilla de la cárcel está en el lado oeste, en la posición 0.
    }

    /**
     * Obtiene la casilla Parking
     * @return la casilla Parking
     */
    public Casilla getParking() {
        return this.getLado("OESTE").get(10); //La casilla del parking está en el lado oeste, en la posición 10.
    }


    /**
     * Método para colorear todo el texto que se muestra en una casilla ( si no forma parte de un grupo se colorea en el color
     * predeterminado)
     * EJ: ───────  <- Esto en negro/color casilla superior
     *    |Solar12 &M| <- Todo esto coloreado
     *    ───────── <- Todo esto coloreado
     */
    public String colorearCasilla(Casilla casilla) {
        if(casilla instanceof Solar solar) {
            if (solar.getGrupo() != null) {
                switch (solar.getGrupo().getColorGrupo()) {
                    case "MARRON":
                        return Valor.BLACK + casilla.getNombre() + generateSpaces(casilla) + mostrarPlayers(casilla) + Valor.RESET;
                    case "CYAN":
                        return Valor.CYAN + casilla.getNombre() + generateSpaces(casilla) + mostrarPlayers(casilla) + Valor.RESET;
                    case "ROSA":
                        return Valor.PURPLE + casilla.getNombre() + generateSpaces(casilla) + mostrarPlayers(casilla) + Valor.RESET;
                    case "SALMON":
                        return Valor.ORANGE + casilla.getNombre() + generateSpaces(casilla) + mostrarPlayers(casilla) + Valor.RESET;
                    case "ROJO":
                        return Valor.RED + casilla.getNombre() + generateSpaces(casilla) + mostrarPlayers(casilla) + Valor.RESET;
                    case "AMARILLO":
                        return Valor.YELLOW + casilla.getNombre() + generateSpaces(casilla) + mostrarPlayers(casilla) + Valor.RESET;
                    case "VERDE":
                        return Valor.GREEN + casilla.getNombre() + generateSpaces(casilla) + mostrarPlayers(casilla) + Valor.RESET;
                    case "AZUL":
                        return Valor.BLUE + casilla.getNombre() + generateSpaces(casilla) + mostrarPlayers(casilla) + Valor.RESET;
                }
            }
        }
        return casilla.getNombre() + generateSpaces(casilla) + mostrarPlayers(casilla) + Valor.RESET;
    }


    /**
     * Método para imprimir los ids de los jugadores que hay en una casilla
     * Formato &P1P2P3... con Pn el ID del jugador n, Ej: &FM o &Q
     */
    public String mostrarPlayers(Casilla casilla) {
        String fichas = "";
        if (casilla.getAvatares() != null && !casilla.getAvatares().isEmpty()) {
            //Podemos el espacio para separar el nombre de la casilla y los id de los jugadores " &P1P2"
            fichas = " &";
            for (Avatar avatar : casilla.getAvatares()) {
                fichas = fichas.concat(avatar.getId());
            }
        }
        return fichas;
    }

    /**
     * Método que devuelve la anchura máxima de cada casilla de un lado del tablero, para que todas sean del mismo tamaño
     * @param lado ArrayList de casillas del lado a medir
     * @return int con la anchura máxima de las casillas del lado
     */
    public int MaxAnchuraCasilla(ArrayList<Casilla> lado) {
        int max = 0;
        for (Casilla casilla : lado) {
            // Medimos cuando ocupa "nombrecasilla &P1P2P3
            int aux = casilla.getNombre().length() + (mostrarPlayers(casilla).length());
            if (aux > max) {
                max = aux;
            }
        }
        return max;
    }

    /**
     * Método para calcular la cantidad de espacios entre el final del nombre de la casilla y el "|" o el " &Ids" para que esa casilla
     * llegue a ocuapar el MaxAnchura de esa casilla.
     * @param casilla casilla a calcular los espacios
     * @return String con los espacios necesarios
     */
    public String generateSpaces(Casilla casilla) {
        // Comprobamos en que lado está la casilla
        String mensaje = "";
        if (this.getLado("NORTE").contains(casilla) || this.getLado("SUR").contains(casilla)) { //NORTE
            // El lado norte y sur van a tener que tener las mismas proporciones para que el tablero sea cuadrado, así que tomamos el máximo entre ambos lados
            ArrayList<Casilla> lado = (MaxAnchuraCasilla(getLado("NORTE")) > MaxAnchuraCasilla(getLado("SUR"))) ? getLado("NORTE") : getLado("SUR");
            // Comprobamos que no llegue ya al máximo con nombre &Ids
            if (MaxAnchuraCasilla(lado) - (casilla.getNombre().concat(mostrarPlayers(casilla))).length() <= 0) {
                return mensaje;
            }
            // Si no llega al máximo, añadimos los espacios que faltan hasta llegar al máximo
            else {
                for (int i = (casilla.getNombre().concat(mostrarPlayers(casilla))).length(); i < MaxAnchuraCasilla(lado); i++) {
                    mensaje = mensaje.concat(" ");
                }
                return mensaje;
            }
        }
        // Si la casilla está en lado OESTE tendrá la proporción del lado OESTE
        else if (this.getLado("OESTE").contains(casilla)) {
            // Comprobamos que no llegue ya al máximo con nombre &Ids
            if (MaxAnchuraCasilla(getLado("OESTE")) - (casilla.getNombre().concat(mostrarPlayers(casilla))).length() <= 0) {
                return mensaje;
            }
            // Si no llega al máximo, añadimos los espacios que faltan hasta llegar al máximo del lado OESTE
            else {
                for (int i = (casilla.getNombre().concat(mostrarPlayers(casilla))).length(); i < MaxAnchuraCasilla(getLado("OESTE")); i++) {
                    mensaje = mensaje.concat(" ");
                }
                return mensaje;
            }
        }
        // Si la casilla está en lado ESTE tendrá la proporción del lado ESTE
        else if (this.getLado("ESTE").contains(casilla)) {
            // Comprobamos que no llegue al máximo con nombre &Ids
            if (MaxAnchuraCasilla(getLado("ESTE")) - (casilla.getNombre().concat(mostrarPlayers(casilla))).length() <= 0) {
                return mensaje;
            }
            // Si no llega al máximo , añadimos los espacios que faltan hasta llegar al máximo del lado ESTE
            else {
                for (int i = (casilla.getNombre().concat(mostrarPlayers(casilla))).length(); i < MaxAnchuraCasilla(getLado("ESTE")); i++) {
                    mensaje = mensaje.concat(" ");
                }
                return mensaje;
            }
        }
        else { // Posible caso de error, si la casilla no perteneciese a ningún lado se retornará null
            return null;
        }
    }

    /**
     * Método para obtener todas las propiedades que son de la banca
     */
    public ArrayList<Propiedad> propiedadesBanca() {
        ArrayList<Propiedad> propiedades = new ArrayList<>(); //Lista para almacenar las propiedades de la banca
        for (ArrayList<Casilla> lado : posiciones) { //Recorremos todos los lados
            for (Casilla prop : lado) { //Y todas las casillas de cada lado
                //Si el dueño es la banca y es una casilla que se puede comprar entonces se añade a las propiedades de la banca
                if (prop instanceof Propiedad) {
                    if (((Propiedad) prop).getDuenho() == banca) {
                        propiedades.add((Propiedad) prop);
                    }
                }
            }
        }
        return propiedades;
    }

    /**Metodo que cuenta el numero de edificios de un tipo concreto en el tablero
     * * Ej: contarEdificiosTipo("Casa") devuelve el numero de casas en el tablero
     * @param tipo
     * @return numEdificios
     */
    public int contarEdificiosTipo(String tipo){
        int numEdificios=0;
        for(ArrayList<Casilla> lado: posiciones){
            for(Casilla casilla: lado){
                if(casilla instanceof Solar) {
                    Solar s = (Solar) casilla;
                    numEdificios += s.numTipo(tipo);
                }
            }
        }
        return numEdificios;
    }

    /** Metodo que incrementa el valor de todas las casillas solares del tablero (trás 4 vueltas)
     * @implNote  Está sin terminar.
     * Método sumarValor() en solar no existe actualmente.
     */
    public void incrementarValorSolares(){
        for(ArrayList<Casilla> lado: posiciones){
            for(Casilla casilla: lado){
                if(casilla instanceof Solar) {
                    Solar s = (Solar) casilla;
                    //s.sumarValor(casilla.getValor());
                }
            }
        }
    }

    /** Método para devolver el ArrayList de líneas que representan una carta para sacarla por pantalla
     * cubrindo el hueco en blanco que queda en medio del tablero.
     *
     * @return
     */
    private ArrayList<String> dibujarCarta(){
        if(hayCarta = true) {
            ArrayList<String> dibujoCarta = new ArrayList<>();
            // El hueco que tenemos disponible son las anchuras de las casillas del NORTE o SUR (restamos 2 para que la carta no toque los bordes del tablero y quede separación)
            int AnchoCarta = Math.max(MaxAnchuraCasilla((getLado("NORTE"))), MaxAnchuraCasilla(getLado("SUR")))*(getLado("NORTE").size()-2) + Valor.NUM_BARRAS;
            String espacioAntes = " ".repeat(Math.max(MaxAnchuraCasilla((getLado("NORTE"))), MaxAnchuraCasilla(getLado("SUR"))));
            String espacioDespues = espacioAntes;
            int numLineas = 0;
            String margenSuperior = espacioAntes+"|"+"=".repeat(AnchoCarta - 2)+"|"+espacioDespues;
            numLineas++;
            dibujoCarta.add(margenSuperior);
            if (cartaActual instanceof CartaSuerte) {
                // Escribimos SUERTE centrado en una columna de AnchoCarta - 2 caracteres (por los bordes)
                String texto = "🍀SUERTE🍀";
                int longitud = texto.length(); // Los emojis son dos caracteres (en código ANSI), pero realmente si se imprimen como un caracter el doble de gordo (por eso lo dejo en lenght)
                String espaciosIzquierda = " ".repeat((AnchoCarta - 2 - longitud) / 2);
                String espaciosDerecha = (AnchoCarta - 2 - longitud) % 2 == 0 ? espaciosIzquierda : espaciosIzquierda + " ";
                dibujoCarta.add(espacioAntes + Valor.UNDER_OVER + Valor.GREEN + "|" + espaciosIzquierda + texto + espaciosDerecha + "|" + Valor.RESET + espacioDespues);
            } else if (cartaActual instanceof CartaComunidad) {
                String texto = "🏛️COMUNIDAD🏛️";
                int longitud = texto.length()-2*("🏛️").length()+4; // Estes emojis valen 3 porque son más complejos, pero ocupan 2 igual por eso sumo 4
                String espaciosIzquierda = " ".repeat((AnchoCarta - 2 - longitud) / 2);
                String espaciosDerecha = (AnchoCarta - 2 - longitud) % 2 == 0 ? espaciosIzquierda : espaciosIzquierda + " ";
                dibujoCarta.add(espacioAntes + Valor.UNDER_OVER + Valor.YELLOW + "|" + espaciosIzquierda + texto + espaciosDerecha + "|" + Valor.RESET + espacioDespues);
            }
            numLineas++;

            // Ahora vamos a escribir en lo que falta de código el mensaje de la carta.
            for (String linea : separarTextoCarta(cartaActual.getImpresion(), AnchoCarta - 2)) {
                dibujoCarta.add(espacioAntes + "|" + linea + "|" + espacioDespues);
                numLineas++;
            }

            // Ahora vamos a reajustar nuestro ArrayList con otro que tenga tantas líneas vacías como para llenar todo el hueco del tablero.
            //    (Líneas de espacio arriba)
            //  ==============================
            //  |          SUERTE            |
            //  -----------------------------
            //  |                            |
            //  |   Texto de la carta...     |
            //  ==============================
            //   (Líneas de espacio abajo)

            ArrayList<String> resultado = new ArrayList<>();


            //El alto del hueco disponible es el número de casillas del lado ESTE menos las dos que ocupan IrCarcel y Salida.
            // También se podría hacer con el lado OESTE.
            int AltoCarta = getLado("ESTE").size() - 2;

            for (int i = 0; i < (AltoCarta - numLineas) / 2; i++) {
                resultado.add(espacioAntes + " ".repeat(AnchoCarta) + espacioDespues);
            }
            for (String linea : dibujoCarta) {
                resultado.add(linea);
            }

            while (resultado.size() < AltoCarta) {
                resultado.add(espacioAntes + " ".repeat(AnchoCarta) + espacioDespues);
            }

            return resultado;
        }
        return null;
    }

    /**
     * Método para separar el texto de una carta en varias líneas si es demasiado largo. Haz que devuelva un ArrayList con cada línea sin utilizar un '\n'
     * @param texto
     * @param anchoMaximo
     * @return
     */
    private ArrayList<String> separarTextoCarta(String texto, int anchoMaximo) {
        ArrayList<String> resultado = new ArrayList<>();
        String[] palabras = texto.split(" ");
        StringBuilder lineaActual = new StringBuilder();

        for (String palabra : palabras) {
            if (lineaActual.length() + palabra.length() + 1 <= anchoMaximo) {
                if (lineaActual.length() > 0) {
                    lineaActual.append(" ");
                }
                lineaActual.append(palabra);
            }
            else {
                resultado.add(lineaActual.toString() + " ".repeat(anchoMaximo - lineaActual.length()));
                lineaActual = new StringBuilder(palabra);
            }
        }
        if (lineaActual.length() > 0) {
            // En la última línea añadimos subrayado
            resultado.add(Valor.UNDERLINE + lineaActual.toString() + " ".repeat(anchoMaximo - lineaActual.length())+ Valor.RESET);
        }

        return resultado;
    }

}
