package monopoly;


public class Valor {
    //Se incluyen una serie de constantes útiles para no repetir valores.
    public static final float FORTUNA_BANCA = Float.POSITIVE_INFINITY; // Cantidad que tiene inicialmente la Banca
    public static final float FORTUNA_INICIAL = 15000000; // Cantidad que recibe cada jugador al comenzar la partida
    public static final float FORTUNA_POBRE = 2000000-1; // Cantidad que recibe cada jugador al comenzar la partida
    public static final float SUMA_VUELTA = 2000000; // Cantidad que recibe un jugador al pasar pos la Salida
    public static final float IMPUESTO_CARCEL = 500000; // Cantidad que debe pagar un jugador para salir de la carcel
    public static final int NUM_CASILLAS = 40; //Numero de casillas del tablero
    public static final int NUM_SUERTE = 7;
    public static final int NUM_COMUNIDAD = 6;

    //Colores del texto:
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[97m";
    // Agregamos el color naranja
    public static final String ORANGE = "\u001B[38;2;255;165;0m"; // Color naranja

    //Colores de fondo:
    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";

    //Estilo de subrayado:
    public static final String UNDERLINE = "\033[4m";
    public static final String UNDER_OVER = "\u001B[53;4m";

    //Numero de barras "|" en cada lado
    public static final int NUM_BARRAS = 8; // Cantidad que debe pagar un jugador para salir de la carcel
}
