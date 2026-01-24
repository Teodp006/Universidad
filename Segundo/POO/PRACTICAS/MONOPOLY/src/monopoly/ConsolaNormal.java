// java
package monopoly;

import java.util.Scanner;

/**
 * Implementación de la interfaz Consola que utiliza la entrada y salida
 * estándar (System.in / System.out) para leer y escribir mensajes.
 */
public class ConsolaNormal implements Consola {

    private final Scanner scanner;

    /**
     * Construye una ConsolaNormal inicializando el Scanner sobre System.in.
     * Se reutiliza la misma instancia para evitar abrir múltiples scanners
     * sobre la misma entrada.
     */
    public ConsolaNormal() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Muestra un mensaje por consola y lee una línea de la entrada estándar.
     *
     * @param mensaje texto que se mostrará antes de leer la entrada
     * @return la línea leída desde la entrada estándar (sin el carácter de nueva línea)
     */
    @Override
    public String leer(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    /**
     * Imprime un mensaje en la salida estándar seguido de un salto de línea.
     *
     * @param mensaje texto a imprimir
     */
    @Override
    public void imprimir(String mensaje) {
        System.out.println(mensaje);
    }
}
