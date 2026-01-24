package monopoly.casilla.propiedad;

import monopoly.edificios.Pista_De_Deporte;
import partida.*;
import java.util.ArrayList;

/**
 * Representa un grupo de solares del tablero (por color).
 * Gestiona la colección de miembros, precios de construcción por tipo
 * y proporciona utilidades para comprobar estado del grupo (completo, hipotecado, etc.).
 *
 * No modifica la lógica original; añade documentación y comentarios inline.
 */
public class Grupo {

    // Atributos

    /** Lista de solares que forman el grupo. */
    private ArrayList<Solar> miembros; //Casillas miembros del grupo.

    /** Identificador de color del grupo (mayúsculas). */
    private String colorGrupo; //Color del grupo

    /** Número de casillas que pertenecen al grupo. */
    private int numCasillas; //Número de casillas del grupo.

    /** Precio de construcción por tipo de edificio en este grupo. */
    private float precioCasa; //Precio de las casas en ese grupo.
    private float precioHotel; //Precio de los hoteles en ese grupo.
    private float precioPiscina; //Precio de las piscinas en ese grupo.
    private float precioPistaDeporte; //Precio de las pistas de deporte en ese

    /** Constructor vacío (mantiene compatibilidad). */
    public Grupo() {
    }

    // Getters y Setters

    public ArrayList<Solar> getMiembros() {
        return miembros;
    }
    public void setMiembros(ArrayList<Solar> miembros) {
        this.miembros = miembros;
    }
    public String getColorGrupo() {
        return colorGrupo;
    }
    public void setColorGrupo(String colorGrupo) { this.colorGrupo = colorGrupo;}
    public int getNumCasillas() {
        return numCasillas;
    }
    public void setNumCasillas(int numCasillas) {
        this.numCasillas = numCasillas;
    }
    public float getPrecioCasa() { return  precioCasa; }
    public void setPrecioCasa(float precioCasa) { this.precioCasa = precioCasa; }
    public float getPrecioHotel() { return precioHotel; }
    public void setPrecioHotel(float precioHotel) { this.precioHotel = precioHotel; }
    public float getPrecioPiscina() { return precioPiscina; }
    public void setPrecioPiscina(float precioPiscina) { this.precioPiscina = precioPiscina; }
    public float getPrecioPistaDeporte() { return precioPistaDeporte; }
    public void setPrecioPistaDeporte(float precioPistaDeporte) { this.precioPistaDeporte = precioPistaDeporte; }

    /**
     * Constructor para grupos formados por dos casillas.
     * Inicializa miembros, color y precios calculados a partir del color.
     *
     * @param cas1 primera casilla miembro
     * @param cas2 segunda casilla miembro
     * @param colorGrupo color del grupo (cadena esperada en mayúsculas)
     */
    public Grupo(Solar cas1, Solar cas2, String colorGrupo) {
        this.miembros=new ArrayList<>();
        miembros.add(cas1);
        miembros.add(cas2);
        this.colorGrupo=colorGrupo;
        this.numCasillas=miembros.size();
        // Calculamos precios de construcción según el color del grupo
        this.precioCasa=precioConstruccion("Casa");
        this.precioHotel=precioConstruccion("Hotel");
        this.precioPiscina=precioConstruccion("Piscina");
        this.precioPistaDeporte=precioConstruccion("Pista de deporte");
        // Asignamos referencia al grupo en cada solar miembro
        for(Solar s: miembros){
            s.setGrupo(this);
        }
    }

    /**
     * Constructor para grupos formados por tres casillas.
     *
     * @param cas1 primera casilla miembro
     * @param cas2 segunda casilla miembro
     * @param cas3 tercera casilla miembro
     * @param colorGrupo color del grupo (cadena esperada en mayúsculas)
     */
    public Grupo(Solar cas1, Solar cas2, Solar cas3, String colorGrupo) {
        this.miembros = new ArrayList<>();
        this.miembros.add(cas1);
        this.miembros.add(cas2);
        this.miembros.add(cas3);
        this.colorGrupo = colorGrupo;
        this.numCasillas = miembros.size();
        // Inicializamos precios según el color
        this.precioCasa=precioConstruccion("Casa");
        this.precioHotel=precioConstruccion("Hotel");
        this.precioPiscina=precioConstruccion("Piscina");
        this.precioPistaDeporte=precioConstruccion("Pista de deporte");
        for(Solar s: miembros){
            s.setGrupo(this);
        }
    }

    /**
     * Añade una casilla al grupo y actualiza contador.
     *
     * @param miembro solar a añadir
     */
    public void anhadirCasilla(Solar miembro) {
        this.miembros.add(miembro);
        this.numCasillas++;
    }

    /**
     * Comprueba si el jugador pasado NO es dueño de todas las casillas del grupo.
     * Nota: el método devuelve false si el jugador posee alguna de las casillas.
     *
     * @param jugador jugador que se evalúa
     * @return true si el jugador no es dueño del grupo completo; false si posee alguna casilla
     */
    public boolean esDuenhoGrupo(Jugador jugador) {
        for (Propiedad casilla : miembros) {
            if (!casilla.perteneceAJugador(jugador)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Devuelve el nombre del grupo con la primera letra en mayúscula (para impresión).
     */
    @Override
    public String toString() {
        return colorGrupo.substring(0, 1).toUpperCase() + colorGrupo.substring(1).toLowerCase(); // Imprime el color del grupo como Marron y no MARRON
    }

    /**
     * Calcula el precio de construcción para un tipo de edificio según el color del grupo.
     * Los valores están codificados y dependen de la cadena `colorGrupo`.
     *
     * @param tipoConstruccion "Casa","Hotel","Piscina" o "Pista de deporte"
     * @return precio para el tipo indicado (0 si no coincide)
     */
    public float precioConstruccion(String tipoConstruccion) {
        float precio=0;
        switch (this.colorGrupo) {
            case "MARRON":
                switch(tipoConstruccion){
                    case "Casa":
                        return 500000;
                    case "Hotel":
                        return 500000;
                    case "Piscina":
                        return 100000;
                    case "Pista de deporte":
                        return 200000;
                }
            case "CYAN":
                switch(tipoConstruccion){
                    case "Casa":
                        return 500000;
                    case "Hotel":
                        return 500000;
                    case "Piscina":
                        return 100000;
                    case "Pista de deporte":
                        return 200000;
                }
            case "ROSA":
                switch(tipoConstruccion){
                    case "Casa":
                        return 1000000;
                    case "Hotel":
                        return 1000000;
                    case "Piscina":
                        return 2000000;
                    case "Pista de deporte":
                        return 400000;
                }
            case "SALMON":
                switch(tipoConstruccion){
                    case "Casa":
                        return 1000000;
                    case "Hotel":
                        return 1000000;
                    case "Piscina":
                        return 2000000;
                    case "Pista de deporte":
                        return 400000;
                }
            case "ROJO":
                switch(tipoConstruccion){
                    case "Casa":
                        return 1500000;
                    case "Hotel":
                        return 1500000;
                    case "Piscina":
                        return 300000;
                    case "Pista de deporte":
                        return 600000;
                }
            case "AMARILLO":
                switch(tipoConstruccion){
                    case "Casa":
                        return 1500000;
                    case "Hotel":
                        return 1500000;
                    case "Piscina":
                        return 300000;
                    case "Pista de deporte":
                        return 600000;
                }
            case "VERDE":
                switch(tipoConstruccion){
                    case "Casa":
                        return 2000000;
                    case "Hotel":
                        return 2000000;
                    case "Piscina":
                        return 400000;
                    case "Pista de deporte":
                        return 800000;
                }
            case "AZUL":
                switch(tipoConstruccion){
                    case "Casa":
                        return 2000000;
                    case "Hotel":
                        return 2000000;
                    case "Piscina":
                        return 400000;
                    case "Pista de deporte":
                        return 800000;
                }

        }
        return precio;
    }

    /**
     * Comprueba si todas las casillas del grupo permiten la construcción completa.
     * Se usa la comprobación de cada solar sobre la posibilidad de edificar la pista
     * (nota: la cadena utilizada se corresponde con la lógica existente).
     *
     * @return true si el grupo está completo; false si no lo está o no hay miembros
     */
    public boolean esGrupoCompleto() {
        if(this.getMiembros().isEmpty()) {
            return false;
        }
        for (Solar s : this.getMiembros()) {
            if (!s.cabeEdificio("Pista De Deporte")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Comprueba si alguna casilla del grupo está hipotecada.
     *
     * @return true si existe alguna casilla hipotecada; false en caso contrario
     */
    public boolean esGrupoHipotecado() {
        if(this.getMiembros().isEmpty()) {
            return false;
        }
        for (Propiedad prop: this.getMiembros()) {
            if (prop.estaHipotecada()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve el precio asociado a un tipo de edificio en el grupo.
     *
     * @param EdificioTipo nombre del tipo ("Casa","Hotel","Piscina","Pista de deporte")
     * @return precio correspondiente o 0 si no existe
     */
    public float getPrecioTipo(String EdificioTipo) {
        switch (EdificioTipo) {
            case "Casa":
                return this.getPrecioCasa();
            case "Hotel":
                return this.getPrecioHotel();
            case "Piscina":
                return this.getPrecioPiscina();
            case "Pista de deporte":
                return this.getPrecioPistaDeporte();
            default:
                return 0;
        }

    }

    /**
     * Calcula la rentabilidad del grupo como generado / gastos totales.
     * Itera sobre miembros sumando gastos y generado de cada casilla.
     *
     * @return ratio de rentabilidad (puede lanzar ArithmeticException si gastos == 0 en contexto externo)
     */
    public float rentabilidadgrupo(){
        float rentabilidad=0, gastos=0, generado=0;
        for(Solar casilla: this.miembros){
            gastos+= casilla.gastosTotales();
            generado+= casilla.getGenerado();
        }
        rentabilidad= generado/gastos;
        return rentabilidad;
    }
}
