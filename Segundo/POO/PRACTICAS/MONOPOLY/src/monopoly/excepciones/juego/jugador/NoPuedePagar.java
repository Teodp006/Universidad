// java
package monopoly.excepciones.juego.jugador;

import monopoly.casilla.propiedad.*;
import monopoly.edificios.*;
import monopoly.excepciones.casilla.ErrorHipoteca;
import partida.Jugador;
import static monopoly.Juego.consolaNormal;

/**
 * Excepción que representa que un jugador no puede pagar una deuda en el juego.
 * Contiene la cantidad pendiente y proporciona utilidades para intentar
 * solucionar la deuda mediante hipotecas o venta de edificios.
 */
public class NoPuedePagar extends ErrorJugador {

    /**
     * Cantidad pendiente que el jugador debe pagar.
     */
    private float cantidadPendiente;

    /**
     * Construye la excepción con un mensaje y la cantidad pendiente.
     *
     * @param message mensaje descriptivo del error
     * @param cantidadPendiente cantidad que falta por pagar
     */
    public NoPuedePagar(String message, float cantidadPendiente) {
        super(message);
        // Se corrige la asignación para usar el parámetro correcto
        this.cantidadPendiente = cantidadPendiente;
    }

    /**
     * Devuelve la cantidad pendiente asociada a esta excepción.
     *
     * @return cantidad pendiente
     */
    public float getCantidadPendiente() {
        return cantidadPendiente;
    }

    /**
     * Intento interactivo de solucionar las deudas del jugador endeudado.
     * Se muestra el listado de propiedades, su posible valor de hipoteca y
     * el valor de venta de edificios (si procede). El método permite al
     * jugador seleccionar propiedades para hipotecar o vender edificios hasta
     * cubrir la cantidad indicada o hasta que no haya más opciones.
     *
     * Nota: la interacción se realiza mediante `consolaNormal`. El método puede
     * lanzar `ErrorHipoteca` si se intenta hipotecar/operar de forma inválida.
     *
     * @param endeudado jugador que tiene la deuda
     * @param acreedor  jugador al que se debe el dinero
     * @param cantidad  importe que debe cubrirse
     * @throws ErrorHipoteca si ocurre un error relacionado con hipotecas
     */
    public void solucionarDuedas(Jugador endeudado, Jugador acreedor, float cantidad) throws ErrorHipoteca {
        // Ahora mismo está en negativo
        endeudado.sumarFortuna(-cantidad);
        acreedor.sumarFortuna(cantidad);
        while (cantidad > 0) {
            consolaNormal.imprimir("El jugador " + endeudado.getNombre() + " debe pagar " + cantidad + " a " + acreedor.getNombre());

            // Mostramos las propiedades del jugador junto con información útil
            for (Propiedad propiedad : endeudado.getPropiedades()) {
                if (!propiedad.estaHipotecada()) {
                    // Si la propiedad es un Solar mostramos además los edificios y su valor
                    switch (propiedad.getClass().getSimpleName()) {
                        case "Solar":
                            float valorEdificios = 0;
                            Solar solar = (Solar) propiedad;
                            consolaNormal.imprimir(solar.getNombre() + ":");
                            for (Edificio edificio : solar.edificiosCasilla()) {
                                valorEdificios += edificio.getPrecio();
                                consolaNormal.imprimir("\tEdificio: " + edificio.getClass().getSimpleName() + edificio.getIdentificador() + ", Valor de venta: " + edificio.getPrecio());
                            }
                            // Mostramos también el valor que se obtendría por hipoteca
                            consolaNormal.imprimir("Valor de Hipoteca: " + propiedad.getHipoteca());
                            break;
                        default:
                            // Para otros tipos de propiedad mostramos el posible valor de hipoteca
                            consolaNormal.imprimir(propiedad.getNombre() + ": Valor de Hipoteca: " + propiedad.getHipoteca());
                            break;
                    }
                }
            }

            // Pedimos al usuario que seleccione una propiedad para operar (hipotecar/vender)
            String respuesta = consolaNormal.leer("Seleccione una propiedad para hipotecar o vender edificios (ingrese el nombre): ");

            boolean propiedadEncontrada = false;
            // Buscamos la propiedad seleccionada y aplicamos la acción correspondiente
            for (Propiedad propiedad : endeudado.getPropiedades()) {
                if (propiedad.getNombre().equals(respuesta)) {
                    propiedadEncontrada = true;
                    if (propiedad instanceof Solar solar) {
                        // Si el solar tiene edificios, pedimos tipo y número a vender
                        if (!solar.edificiosCasilla().isEmpty()) {
                            String respEdificio = consolaNormal.leer("Introduzca el tipo de edificios a vender y el número separados por un espacio (Ejemplo: Casa 2): ");
                            String tipoEdificio = respEdificio.split(" ")[0];
                            int numEdificios = Integer.parseInt(respEdificio.split(" ")[1]);

                            // Intentamos vender los edificios solicitados
                            if (solar.numTipo(tipoEdificio) < numEdificios) {
                                // Llamada a la lógica interna para vender edificios
                                solar.venderEdificio(tipoEdificio, numEdificios);
                                // Ajustamos la cantidad restante en función del tipo vendido
                                switch (tipoEdificio) {
                                    case "Casa":
                                        cantidad -= solar.getGrupo().getPrecioCasa();
                                        break;
                                    case "Hotel":
                                        cantidad -= solar.getGrupo().getPrecioHotel();
                                        break;
                                    case "Piscina":
                                        cantidad -= solar.getGrupo().getPrecioPiscina();
                                        break;
                                    case "Pista de deporte":
                                        cantidad -= solar.getGrupo().getPrecioPistaDeporte();
                                        break;
                                    default:
                                        consolaNormal.imprimir("Tipo de edificio no válido.");
                                        break;
                                }
                            } else {
                                consolaNormal.imprimir("No hay suficientes edificios de ese tipo para vender en " + propiedad.getNombre());
                            }
                        } else {
                            // Si no hay edificios, hipotecamos la propiedad
                            propiedad.hipotecar();
                            cantidad -= propiedad.getHipoteca();
                        }
                    } else {
                        // Para propiedades que no son solares simplemente hipotecamos
                        propiedad.hipotecar();
                        cantidad -= propiedad.getHipoteca();
                    }
                    consolaNormal.imprimir("La casilla " + respuesta +" ha sido procesada. Cantidad restante por pagar: " + (cantidad>0 ? cantidad : 0 + " ya eres libre, puedes seguir jugando con una fortuna de " + endeudado.getFortuna() + "€."));
                    break; // Nos saltamos el for, ya que hemos encontrado la propiedad
                }
            }
            if(!propiedadEncontrada) {
                consolaNormal.imprimir("La casilla introducida no es válida o no pertenece al jugador.");
            }
        }
    }
}
