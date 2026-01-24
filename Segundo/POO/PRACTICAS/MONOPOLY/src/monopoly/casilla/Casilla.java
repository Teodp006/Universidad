// java
package monopoly.casilla;

import partida.*;
import java.util.ArrayList;

/**
 * Representa una casilla del tablero.
 * Mantiene el nombre, la posición, los avatares presentes y un contador de visitas.
 * Clase abstracta: cada tipo concreto de casilla debe implementar infoCasilla().
 */
public abstract class Casilla {
    /**
     * Nombre visible de la casilla.
     */
    private String nombre;
    /**
     * Posición fija de la casilla en el tablero (índice).
     */
    private int posicion;
    /**
     * Lista de avatares actualmente situados en esta casilla.
     */
    private ArrayList<Avatar> avatares;
    /**
     * Contador de cuántas veces ha sido visitada esta casilla.
     */
    private int visitada;

    /**
     * Construye una casilla con nombre y posición, inicializando la lista de avatares
     * y el contador de visitas a cero.
     *
     * @param nombre   nombre de la casilla
     * @param posicion posición en el tablero
     */
    public Casilla(String nombre, int posicion) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.avatares = new ArrayList<>();
        this.visitada = 0;
    }

    /**
     * Añade un avatar a la casilla (cuando un jugador llega).
     *
     * @param av avatar a añadir
     */
    // Método utilizado para añadir un avatar al array de avatares en casilla.
    public void anhadirAvatar(Avatar av) {
        avatares.add(av);
    }

    /**
     * Elimina un avatar de la casilla (cuando un jugador se marcha).
     *
     * @param av avatar a eliminar
     */
    // Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        avatares.remove(av);
    }

    /**
     * Devuelve la lista de avatares presentes en la casilla.
     * La lista es la misma instancia interna (modificable).
     *
     * @return lista de avatares
     */
    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }

    /**
     * Nombre de la casilla.
     *
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Posición de la casilla en el tablero.
     *
     * @return posición
     */
    public int getPosicion() {
        return posicion;
    }

    /**
     * Comprueba si un avatar concreto está en la casilla.
     *
     * @param avatar avatar a comprobar
     * @return true si está presente, false en caso contrario
     */
    boolean estaAvatar(Avatar avatar) {
        return avatares.contains(avatar);
    }

    /**
     * Devuelve el contador de visitas acumuladas.
     *
     * @return número de visitas
     */
    public int getVisitada() { return visitada; }

    /**
     * Representación por defecto: devuelve el nombre de la casilla.
     *
     * @return nombre como cadena
     */
    @Override
    public String toString() {
        return nombre;
    }

    /**
     * Dos casillas se consideran iguales si tienen la misma posición en el tablero.
     *
     * @param obj objeto a comparar
     * @return true si son la misma casilla (misma posición)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Casilla casilla = (Casilla) obj;
        return posicion == casilla.posicion;
    }

    /**
     * Frecuencia de visita (alias de getVisitada).
     *
     * @return contador de visitas
     */
    public int frecuenciaVisita(){
        return visitada;
    }

    /**
     * Incrementa el contador de visitas en uno.
     * Llamar cuando un jugador visita la casilla.
     */
    public void incrementarVisita(){
        visitada++;
    }

    /**
     * Información textual específica de la casilla.
     * Implementado por las subclases concretas.
     *
     * @return texto descriptivo de la casilla
     */
    public abstract String infoCasilla();
}



