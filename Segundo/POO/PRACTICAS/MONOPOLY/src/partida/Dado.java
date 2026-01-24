// java
package partida;
import java.util.Random; //Necesario para incluir la clase random
import static monopoly.Juego.consolaNormal;

/**
 * Representa un dado de seis caras usado en la partida.
 * Mantiene el valor actual de la tirada y proporciona métodos
 * para realizar tiradas y comprobar si dos dados son dobles.
 */
public class Dado {
    // El dado solo tiene un atributo en nuestro caso: su valor.
    private int valor;

    /**
     * Construye un dado sin valor inicial (valor por defecto 0).
     */
    public Dado(){
    }

    /**
     * Devuelve el valor actual del dado.
     *
     * @return valor del dado (1..6), o 0 si no se ha inicializado
     */
    public int getValor(){
        return valor;
    }

    /**
     * Establece el valor del dado si está en el rango válido.
     *
     * @param valor valor a asignar (debe ser 1..6). Si no está en rango, no hace nada.
     */
    public void setValor(int valor){
        if(valor>0 && valor<7) { //El dado tiene valores entre 1 y 6
            this.valor = valor;
        }
    }

    /**
     * Simula una tirada estableciendo el dado al valor dado.
     * Imprime el resultado por la consola de juego.
     *
     * @param a valor fijo de la tirada (1..6)
     */
    public void hacerTirada(int a) {
        setValor(a);
        consolaNormal.imprimir("Dado: " + valor);
    }

    /**
     * Genera una tirada aleatoria entre 1 y 6, la asigna al dado
     * e imprime el resultado por la consola de juego.
     */
    public void hacerTirada(){
        Random rnd = new Random();
        setValor(rnd.nextInt(6) + 1); //Ajustamos el valor del dado al siguiente numero generado por rnd, limitado al rango
        consolaNormal.imprimir("Dado: " + valor);
    }

    /**
     * Comprueba si este dado y otro dado representan dobles
     * (mismo valor en ambos dados).
     *
     * @param d2 dado con el que comparar
     * @return true si ambos dados tienen el mismo valor, false en caso contrario
     */
    public boolean sonDobles(Dado d2){
        return this.getValor() == d2.getValor();
    }
}


