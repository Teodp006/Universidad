// java
package partida;

import java.util.ArrayList;
import monopoly.*;
import monopoly.casilla.Especial;
import monopoly.casilla.Casilla;
import monopoly.casilla.propiedad.*;
import monopoly.edificios.Edificio;
import monopoly.excepciones.juego.jugador.BancarrotaException;
import monopoly.excepciones.juego.jugador.NoPuedePagar;
import static monopoly.Juego.consolaNormal;

/**
 * Representa a un jugador en la partida.
 * Mantiene información del avatar, fortuna, propiedades, edificios y estadísticas.
 */
public class Jugador {

    //Atributos:
    private String nombre; //Nombre del jugador
    private Avatar avatar; //Avatar que tiene en la partida.
    private float fortuna; //Dinero que posee.
    private float gastos; //Gastos realizados a lo largo del juego.
    private boolean enCarcel; //Será true si el jugador está en la carcel
    private boolean bancarrota; //Será true si el jugador está en bancarrota
    private int tiradasCarcel; //Cuando está en la carcel, contará las tiradas sin éxito que ha hecho allí para intentar salir (se usa para limitar el numero de intentos).
    private int vueltas; //Cuenta las vueltas dadas al tablero.
    private int vecesCarcel;
    private float dineroPorImpuestos;
    private float dineroPorAlquileres;
    private float cobrosPorAlquileres;
    private float premiosInversionesOBote;
    private ArrayList<Propiedad> propiedades; //Propiedades que posee el jugador.
    private ArrayList<Edificio> edificios; //Edificios que posee el jugador.
    private ArrayList<Trato> tratos; //Tratos disponibles que tiene el jugador para aceptar.

    /**
     * Constructor vacío usado para crear la banca.
     * Inicializa valores por defecto y colecciones.
     */
    public Jugador(){
        this.nombre = "Banca";
        this.avatar = null;
        this.fortuna= Valor.FORTUNA_BANCA; //La banca tiene dinero infinito
        this.gastos=0;
        this.enCarcel= false;
        this.tiradasCarcel=0;
        this.vueltas=-1; //Empieza en -1 porque al salir de la salida ya le se suma una
        this.propiedades= new ArrayList<Propiedad>();
        this.edificios= new ArrayList<Edificio>();
        this.vecesCarcel=0;
        this.dineroPorImpuestos=0;
        this.dineroPorAlquileres=0;
        this.cobrosPorAlquileres=0;
    }

    /**
     * Constructor principal.
     *
     * @param nombre nombre del jugador
     * @param tipoAvatar tipo de avatar que tendrá
     * @param inicio casilla de inicio del avatar
     * @param avCreados lista de avatares ya creados (evita duplicados)
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados) {
        this.nombre = nombre;
        this.avatar = new Avatar(tipoAvatar,this,inicio, avCreados);
        this.fortuna= Valor.FORTUNA_INICIAL;
        this.gastos=0;
        this.enCarcel= false;
        this.tiradasCarcel=0;
        this.vueltas=-1; //Empieza en -1 porque al salir de la salida ya le se suma una
        this.propiedades= new ArrayList<Propiedad>();
        this.edificios= new ArrayList<Edificio>();
        this.vecesCarcel=0;
        this.dineroPorImpuestos=0;
        this.dineroPorAlquileres=0;
        this.cobrosPorAlquileres=0;
        this.tratos= new ArrayList<Trato>();
    }

    //Seters y geters

    /**
     * @return nombre del jugador
     */
    public String getNombre(){
        return nombre;
    }

    /**
     * @return avatar asociado al jugador
     */
    public Avatar getAvatar(){
        return avatar;
    }

    /**
     * @return fortuna actual del jugador
     */
    public float getFortuna(){
        return fortuna;
    }

    /**
     * @return gastos acumulados del jugador
     */
    public float getgastos(){
        return gastos;
    }

    /**
     * @return true si el jugador está en la cárcel
     */
    public boolean getenCarcel(){
        return enCarcel;
    }

    /**
     * @return número de tiradas en la cárcel
     */
    public int getTiradasCarcel(){
        return tiradasCarcel;
    }

    /**
     * @return vueltas completadas alrededor del tablero
     */
    public int getVueltas(){
        return vueltas;
    }

    /**
     * @return lista de propiedades del jugador
     */
    public ArrayList<Propiedad> getPropiedades(){
        return propiedades;
    }

    /**
     * @return lista de edificios del jugador
     */
    public ArrayList<Edificio> getEdificios(){
        return edificios;
    }

    public int getVecesCarcel() {return vecesCarcel;}
    public float getDineroPorImpuestos() {return dineroPorImpuestos;}
    public float getDineroPorAlquileres() {return dineroPorAlquileres;}
    public float getCobrosPorAlquileres() {return cobrosPorAlquileres;}
    public float getPremiosInversionesOBote() {return premiosInversionesOBote;}
    public ArrayList<Trato> getTratos() {return tratos;}
    public boolean estaEnBancarrota(){return bancarrota;}

    /**
     * Establece el nombre del jugador.
     *
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    /**
     * Establece el avatar del jugador si el tipo es válido.
     *
     * @param avatar avatar a asignar
     */
    public void setAvatar(Avatar avatar){ //Se comprueba que sea un avatar existente
        if(avatar.getTipo().equals("sombrero") || avatar.getTipo().equals("esfinge")
                || avatar.getTipo().equals("pelota") || avatar.getTipo().equals("coche")){
            this.avatar = avatar;
        }
    }

    public void setFortuna(float Fortuna){this.fortuna = Fortuna;}
    public void setGastos(float gastos){this.gastos= gastos;}
    public void setEnCarcel(Boolean enCarcel){
        this.enCarcel= enCarcel;
    }
    public void setTiradasCarcel(int tiradasCarcel){this.tiradasCarcel= tiradasCarcel;}
    public void setVueltas(int Vueltas){
        this.vueltas= Vueltas;
    }
    public void setPropiedades(ArrayList<Propiedad> propiedades){this.propiedades= propiedades;}
    public void setEdificios(ArrayList<Edificio> edificios){this.edificios= edificios;}
    public void setVecesCarcel(int vecesCarcel) {this.vecesCarcel = vecesCarcel;}
    public void setDineroPorImpuestos(float dineroPorImpuestos) {this.dineroPorImpuestos = dineroPorImpuestos;}
    public void setDineroPorAlquileres(float dineroPorAlquileres) {this.dineroPorAlquileres = dineroPorAlquileres;}
    public void setCobrosPorAlquileres(float cobrosPorAlquileres) {this.cobrosPorAlquileres = cobrosPorAlquileres;}
    public void setPremiosInversionesOBote(float premiosInversionesOBote) {this.premiosInversionesOBote = premiosInversionesOBote;}


    //Otros métodos:

    /**
     * Añade una propiedad al jugador.
     *
     * @param casilla propiedad a añadir
     */
    public void anhadirPropiedad(Propiedad casilla) {
        this.propiedades.add(casilla);
    }

    /**
     * Elimina una propiedad del jugador.
     *
     * @param casilla propiedad a eliminar
     */
    public void eliminarPropiedad(Propiedad casilla) {
        this.propiedades.remove(casilla);
    }

    /**
     * Añade un edificio al jugador.
     *
     * @param edificio edificio a añadir
     */
    public void anhadirEdificio(Edificio edificio) {
        this.edificios.add(edificio);
    }

    /**
     * Elimina un edificio del jugador.
     *
     * @param edificio edificio a eliminar
     */
    public void eliminarEdificio(Edificio edificio) {
        this.edificios.remove(edificio);
    }

    /**
     * Suma (o resta si el valor es negativo) fortuna al jugador.
     *
     * @param valor cantidad a sumar o restar
     */
    public void sumarFortuna(float valor) {
        this.fortuna +=valor;
    }

    /**
     * Suma gastos al jugador.
     *
     * @param valor cantidad a añadir a gastos
     */
    public void sumarGastos(float valor) {
        this.gastos += valor;
    }

    /**
     * Encarcela al jugador: mueve su avatar a la casilla de cárcel y actualiza estado.
     *
     * @param pos posiciones del tablero necesarias para localizar la cárcel
     */
    public void encarcelar(ArrayList<ArrayList<Casilla>> pos) {
        Especial carcel = (Especial)pos.get(1).get(0); //Casilla de la carcel.
        carcel.anhadirAvatar(avatar);//Coloca el avatar del jugador en la casilla de la carcel.
        this.avatar.getLugar().eliminarAvatar(avatar); //Elimina el avatar de la casilla en la que estaba.
        this.avatar.setLugar(carcel); //Actualiza la posición del avatar.
        this.enCarcel = true; //Indica que el jugador está en la carcel.
        this.tiradasCarcel = 0; //Resetea las tiradas en la carcel a 0.
        carcel.incrementarVisita(); // Sumamos una visita a la carcel
        this.sumarvecescarcel();
    }

    /**
     * Dos jugadores son iguales si comparten el mismo nombre.
     *
     * @param obj objeto a comparar
     * @return true si son iguales
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Jugador jugador = (Jugador) obj;
        return nombre.equals(jugador.nombre);
    }

    /**
     * Representación en texto del jugador.
     *
     * @return cadena con nombre e id de avatar
     */
    @Override
    public String toString() {
        return "{\n\t" +
                "Nombre: " + nombre + "\n\t" +
                "Avatar: " + avatar.getId() +
                "\n}";
    }

    /**
     * Comprueba si el jugador puede pagar una cantidad.
     *
     * @param cantidad cantidad a comprobar
     * @return true si tiene suficiente fortuna inmediata
     * @throws NoPuedePagar si puede cubrir la cantidad con ventas/hipotecas pero no con cash
     * @throws BancarrotaException si no dispone de medios suficientes y entra en bancarrota
     */
    public boolean puedePagar(float cantidad) throws NoPuedePagar, BancarrotaException {
        if(this.fortuna>=cantidad){
            return true;
        }
        else if(cantidad <= dineroPropiedades() + fortuna ){
            throw new NoPuedePagar("El jugador " + nombre + " no tiene cash suficiente",cantidad);
        }
        else{
            throw new BancarrotaException("El jugador " + nombre + " no puede pagar la cantidad de " + cantidad + " y entra en bancarrota. Se elimina de la partida");
        }
    }

    /**
     * Calcula el dinero que se podría obtener hipotecando propiedades no hipotecadas
     * y vendiendo edificios.
     *
     * @return cantidad total estimada
     */
    private float dineroPropiedades(){
        float total=0;
        for(Propiedad propiedad: this.getPropiedades()){
            if(!propiedad.estaHipotecada()){
                total += propiedad.getHipoteca();
                if(propiedad instanceof Solar){
                    Solar solar = (Solar) propiedad;
                    for(Edificio edificio: solar.edificiosCasilla()){
                        total += edificio.getPrecio();
                    }
                }
            }
        }
        return total;
    }

    // metodos para actualizar estadisticas del jugador

    /**
     * Incrementa el contador de veces en la cárcel.
     */
    public void sumarvecescarcel(){
        this.vecesCarcel++;
    }

    /**
     * Suma impuestos cobrados por el jugador.
     *
     * @param cantidad importe a sumar
     */
    public void sumarimpuestos(float cantidad) {
        this.dineroPorImpuestos += cantidad;
    }

    /**
     * Suma alquileres pagados por el jugador.
     *
     * @param cantidad importe a sumar
     */
    public void sumaralquileres(float cantidad) {
        this.dineroPorAlquileres += cantidad;
    }

    /**
     * Suma los cobros por alquileres recibidos.
     *
     * @param cantidad importe a sumar
     */
    public void sumarCobrosAlquileres(float cantidad) {
        this.cobrosPorAlquileres += cantidad;
    }

    /**
     * Suma premios, inversiones o bote.
     *
     * @param cantidad importe a sumar
     */
    public void sumarpremiosinversionesbote(float cantidad) {
        this.premiosInversionesOBote += cantidad;
    }

    /**
     * @return true si el jugador es la Banca
     */
    public boolean esBanca(){
        return this.nombre.equals("Banca");
    }

    /**
     * Paga una cantidad: resta fortuna y actualiza estadísticas.
     *
     * @param valor cantidad a pagar
     */
    public void pagar(float valor){
        this.fortuna -= valor;
        this.dineroPorAlquileres += valor;
        this.gastos += valor;
    }

    /**
     * Cobra una cantidad: aumenta fortuna y cobros por alquileres.
     *
     * @param valor cantidad a cobrar
     */
    public void cobrar(float valor){
        this.fortuna +=valor;
        this.cobrosPorAlquileres += valor;
    }

    /**
     * Añade un trato propuesto por el jugador.
     *
     * @param trato trato a añadir
     */
    public void anhadirtrato(Trato trato) {
        this.tratos.add(trato);
    }

    /**
     * Elimina un trato por su id. Si no existe, imprime un mensaje por consola.
     *
     * @param trato id del trato a eliminar
     */
    public void eliminartrato(String trato) {
        for(Trato t: this.tratos){
            if(t.getIdtrato().equals(trato)){
                this.tratos.remove(t);
                consolaNormal.imprimir("Se ha eliminado el trato " + trato + " del jugador " + nombre);
                return;
            }
        }
        // Si llega aquí es porque no se encontró el trato
        consolaNormal.imprimir("El jugador " + nombre + " no ha propuesto ningún trato con el id: " + trato);
    }
}

