// java
package monopoly.casilla.propiedad;

import monopoly.casilla.Casilla;
import static monopoly.Juego.consolaNormal;

import monopoly.edificios.Edificio;
import monopoly.excepciones.casilla.ErrorHipoteca;
import monopoly.excepciones.juego.jugador.BancarrotaException;
import monopoly.excepciones.juego.jugador.NoPuedePagar;
import partida.*;

/**
 * Clase base para todas las propiedades del tablero (Solares, Servicios, Transportes, ...).
 * Encapsula datos comunes: valor de compra, dueño, alquiler base, estado de hipoteca y operaciones
 * típicas como comprar, hipotecar, deshipotecar y transferir propiedad.
 *
 * No modifica la lógica original: solo añade documentación y comentarios para facilitar la comprensión.
 */
public abstract class Propiedad extends Casilla {
    private float valor;
    private Jugador duenho;
    private float alquiler;
    private float generado;
    private boolean hipotecada;
    private float hipoteca;

    /**
     * Inicializa una propiedad con sus valores básicos.
     *
     * @param nombre   nombre de la casilla
     * @param posicion posición en el tablero
     * @param valor    precio de adquisición
     * @param alquiler alquiler base de la casilla
     * @param banca    referencia al jugador banca (propietario por defecto)
     */
    public Propiedad(String nombre, int posicion, float valor,float alquiler, Jugador banca){
        super(nombre, posicion);
        this.generado = 0 ;
        this.alquiler = alquiler;
        this.valor = valor;
        this.hipoteca = valor/2;   // valor de reembolso por hipoteca
        this.hipotecada = false;
        this.duenho = banca;       // la banca es dueña inicialmente
    }

    /** Cuenta las casillas del tipo concreto que posee un jugador (implementado por subclases). */
    public abstract int contarCasillas(Jugador Duenho);

    public float getAlquiler() {
        return alquiler;
    }

    public Jugador getDuenho(){
        return duenho;
    }

    public float getValor(){
        return valor;
    }

    public float getHipoteca(){
        return hipoteca;
    }

    public void setHipotecada(boolean estado){
        hipotecada = estado;
    }

    public void setDuenho(Jugador nuevoDuenho){
        duenho = nuevoDuenho;
    }

    public boolean estaHipotecada(){
        return hipotecada;
    }

    /**
     * Compra la propiedad a la banca si el jugador puede pagar el precio.
     * Lanza NoPuedePagar/Bancarrota según las comprobaciones internas del jugador.
     *
     * @param jugador jugador que desea comprar la propiedad
     * @throws NoPuedePagar, BancarrotaException delegadas desde pagar/puedePagar si procede
     */
    public void comprar(Jugador jugador) throws NoPuedePagar, BancarrotaException {
        if(duenho.esBanca()){
            if(jugador.puedePagar(valor)){
                jugador.pagar(valor);
                duenho = jugador;
                jugador.anhadirPropiedad(this);
            }
            else{
                consolaNormal.imprimir("Fallo el puedePagar.");
            }
        }
        else{
            consolaNormal.imprimir("fallo el esbanca");
        }
    }

    /** Comprueba si la propiedad pertenece al jugador indicado. */
    boolean perteneceAJugador(Jugador jugador){
        return !duenho.esBanca() && duenho.equals(jugador);
    }

    /**
     * Hipoteca la propiedad: si no está hipotecada, el dueño recibe el importe de hipoteca.
     *
     * @throws ErrorHipoteca si la propiedad ya está hipotecada
     */
    public void hipotecar() throws ErrorHipoteca {
        if(!hipotecada){
            duenho.sumarFortuna(hipoteca);
            hipotecada = true;
            consolaNormal.imprimir("La propiedad " + this.getNombre() + " ha sido hipotecada. El dueño " + duenho.getNombre() + " ha recibido " + hipoteca + "€.\n");
        } else{
            throw new ErrorHipoteca("Error: La propiedad " + this.getNombre() + " ya está hipotecada.\n");
        }
    }

    /**
     * Deshipoteca la propiedad: si está hipotecada, el dueño debe pagar la cantidad de hipoteca.
     *
     * @throws ErrorHipoteca si la propiedad no está hipotecada
     * @throws NoPuedePagar, BancarrotaException delegadas desde pagar/puedePagar si procede
     */
    public void deshipotecar() throws ErrorHipoteca, NoPuedePagar, BancarrotaException {
        if(hipotecada){
            if(duenho.puedePagar(hipoteca)){
                duenho.pagar(hipoteca);
                hipotecada = false;
                consolaNormal.imprimir("La propiedad " + this.getNombre() + " ha sido deshipotecada. El dueño " + duenho.getNombre() + " ha pagado " + hipoteca + "€.\n");
            }
        } else{
            throw new ErrorHipoteca("Error: La propiedad " + this.getNombre() + " no está hipotecada.\n");
        }
    }

    /** Representación de la propiedad para listados de venta (implementada por subclases). */
    public abstract String casEnVenta();

    public float getGenerado(){
        return generado;
    }

    /**
     * Evalúa la casilla cuando un jugador cae en ella.
     * Implementación concreta depende del tipo de propiedad.
     */
    public abstract boolean evaluarCasilla(Jugador jugador, Jugador banca, int tirada) throws NoPuedePagar, BancarrotaException;

    /**
     * Ejecuta el cobro de alquiler entre duenho y moroso (implementación concreta por subclases).
     */
    public abstract void alquiler(Jugador duenho, Jugador moroso, int tirada) throws NoPuedePagar, BancarrotaException;

    /**
     * Calcula el importe total de alquiler de la propiedad.
     * Para solares incluye edificios; para otros tipos será su implementación específica.
     */
    public abstract float Valor();

    /**
     * Transfiere la propiedad a un nuevo dueño: actualiza listados de propiedades y,
     * si es un Solar, transfiere también los edificios asociados.
     *
     * @param nuevoDuenho jugador que adquiere la propiedad
     */
    public void tradearPropiedad(Jugador nuevoDuenho){
        duenho.eliminarPropiedad(this);
        nuevoDuenho.anhadirPropiedad(this);
        Jugador viejoDuenho = duenho;
        duenho = nuevoDuenho;
        if(this instanceof Solar){
            Solar solar = (Solar) this;
            for(Edificio edificio : solar.edificiosCasilla()) {
                duenho.eliminarEdificio(edificio);
                nuevoDuenho.anhadirEdificio(edificio);
            }
        }
        consolaNormal.imprimir("La propiedad de " + viejoDuenho.getNombre() + ", " + this.getNombre() + " ha sido transferida a " + nuevoDuenho.getNombre() + ".\n");
    }

    /** Devuelve los gastos totales invertidos en la propiedad (por defecto su valor). */
    public float gastosTotales(){
        return valor;
    }
}
