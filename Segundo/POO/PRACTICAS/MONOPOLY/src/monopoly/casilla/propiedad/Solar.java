// java
package monopoly.casilla.propiedad;

import monopoly.edificios.*;
import monopoly.excepciones.casilla.ErrorHipoteca;
import monopoly.excepciones.juego.jugador.BancarrotaException;
import monopoly.excepciones.juego.jugador.NoPuedePagar;
import partida.*;
import static monopoly.Juego.consolaNormal;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Representa un solar (propiedad) del tablero que puede pertenecer a un grupo y
 * contener distintos tipos de edificios: casas, hotel, piscina y pista de deporte.
 * <p>
 * Gestiona la construcción/venta de edificios, el cálculo de alquileres y la
 * hipoteca de la propiedad. No modifica la lógica original; solo añade documentación
 * y comentarios inline para facilitar la lectura.
 */
public class Solar extends Propiedad {
    private Grupo grupo;
    private ArrayList<Casa> casas;
    private Hotel hotel;
    private Piscina piscina;
    private Pista_De_Deporte pistaDeporte;
    private float alquilerCasa;
    private float alquilerHotel;
    private float alquilerPiscina;
    private float alquilerPistaDeporte;

    /**
     * Constructor que inicializa el solar con valores base y prepara las colecciones.
     *
     * @param nombre   nombre de la casilla
     * @param posicion posición en el tablero
     * @param valor    precio de adquisición
     * @param alquiler alquiler base de la casilla
     * @param banca    referencia al jugador banca
     */
    public Solar(String nombre, int posicion, float valor, float alquiler, Jugador banca) {
        super(nombre, posicion, valor, alquiler, banca);
        casas = new ArrayList<>();
        hotel = null;
        piscina = null;
        pistaDeporte = null;
    }

    /** Devuelve el grupo al que pertenece este solar. */
    public Grupo getGrupo() {
        return grupo;
    }

    /** Asigna el grupo al que pertenece este solar. */
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    /** Devuelve la lista de casas construidas en este solar. */
    public ArrayList<Casa> getCasas() {
        return casas;
    }

    /** Devuelve el hotel construido (si existe). */
    public Hotel getHotel() {
        return hotel;
    }

    /** Devuelve la piscina construida (si existe). */
    public Piscina getPiscina() {
        return piscina;
    }

    /** Devuelve la pista de deporte construida (si existe). */
    public Pista_De_Deporte getPistaDeporte() {
        return pistaDeporte;
    }

    /**
     * Comprueba si el avatar del dueño está actualmente en la casilla.
     *
     * @return true si el avatar del dueño está en la casilla; false en caso contrario
     */
    private boolean esMismoAvatar() {
        if(getAvatares().isEmpty()){
            return false;
        }

        // Recorremos los avatares presentes en la casilla y comparamos con el del dueño
        for (Avatar av : getAvatares()) {
            if (av.equals(getDuenho().getAvatar())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Intenta edificar un edificio del tipo indicado en esta casilla.
     * Comprueba condiciones: avatar, propiedad del grupo y no estar hipotecada.
     * Realiza cobros y actualiza listas de edificios y gastos del dueño.
     *
     * @param tipoEdificio nombre del edificio a construir ("Casa","Hotel","Piscina","Pista de deporte")
     * @param idEdificio   identificador que se asignará al nuevo edificio
     * @throws NoPuedePagar    si el jugador no puede afrontar el pago
     * @throws BancarrotaException si la operación causa bancarrota
     */
    public void edificar(String tipoEdificio, int idEdificio) throws NoPuedePagar, BancarrotaException {
        if (!esMismoAvatar() || !grupo.esDuenhoGrupo(getDuenho()) || estaHipotecada()) {
            consolaNormal.imprimir("No se cumplen las condiciones para edificar");
            return;
        }

        try {
            if (getDuenho().puedePagar(grupo.getPrecioTipo(tipoEdificio))) {
                if (tipoEdificio.equals("Casa")) {
                    if (cabeEdificio(tipoEdificio)) {
                        Casa casa = new Casa(idEdificio, alquilerCasa, grupo.getPrecioCasa(), this, getDuenho());
                        casas.add(casa);
                        // Actualizamos la fortuna y gastos del dueño y añadimos el edificio a su listado
                        getDuenho().sumarFortuna(-casa.getPrecio());       // Restamos el precio de la casa
                        getDuenho().sumarGastos(casa.getPrecio());         // Sumamos como gasto
                        getDuenho().anhadirEdificio(casa);                 // Añadimos al dueño
                        consolaNormal.imprimir("Se ha construido un/a " + tipoEdificio + " en la casilla " + this.getNombre() + ".\n");
                    }
                }

                if (tipoEdificio.equals("Hotel")) {
                    if (cabeEdificio(tipoEdificio)) {
                        // Construcción de hotel: se venden 4 casas si procede y se añade el hotel
                        hotel = new Hotel(idEdificio, alquilerHotel, grupo.getPrecioHotel(), this, getDuenho());
                        venderEdificio("Casas", 4);
                        getDuenho().sumarFortuna(-hotel.getPrecio());       // Restamos el precio del hotel
                        getDuenho().sumarGastos(hotel.getPrecio());         // Sumamos al gasto
                        getDuenho().anhadirEdificio(hotel);                 // Añadimos al dueño
                        consolaNormal.imprimir("Se ha construido un/a " + tipoEdificio + " en la casilla " + this.getNombre() + ".\n");
                    } else {
                        consolaNormal.imprimir("No se cumplen las condiciones para eficiar un hotel");
                    }
                }

                if (tipoEdificio.equals("Piscina")) {
                    if (cabeEdificio(tipoEdificio)) {
                        piscina = new Piscina(idEdificio, alquilerPiscina, grupo.getPrecioPiscina(), this, getDuenho());
                        getDuenho().sumarFortuna(-piscina.getPrecio());       // Restamos el precio de la piscina
                        getDuenho().sumarGastos(piscina.getPrecio());         // Sumamos al gasto
                        getDuenho().anhadirEdificio(piscina);                 // Añadimos al dueño
                        consolaNormal.imprimir("Se ha construido un/a " + tipoEdificio + " en la casilla " + this.getNombre() + ".\n");
                    } else {
                        consolaNormal.imprimir("No se cumplen las condiciones para eficiar un piscina");
                    }
                }

                if (tipoEdificio.equals("Pista de deporte")) {
                    if (cabeEdificio(tipoEdificio)) {
                        pistaDeporte = new Pista_De_Deporte(idEdificio, alquilerPistaDeporte, grupo.getPrecioPistaDeporte(), this, getDuenho());
                        getDuenho().sumarFortuna(-pistaDeporte.getPrecio());       // Restamos el precio de la pista
                        getDuenho().sumarGastos(pistaDeporte.getPrecio());         // Sumamos al gasto
                        getDuenho().anhadirEdificio(pistaDeporte);                 // Añadimos al dueño
                        consolaNormal.imprimir("Se ha construido un/a " + tipoEdificio + " en la casilla " + this.getNombre() + ".\n");
                    } else {
                        consolaNormal.imprimir("No se cumolen las condiciones para edificar una Pista de Deporte");
                    }
                }

            }
        }catch (NoPuedePagar e) { // No tiene sentido que le deba dinero a nadie, entonces simplemente no se puede edificar y ya
            consolaNormal.imprimir("No tienes suficiente dinero para edificar un/a " + tipoEdificio + " en la casilla " + this.getNombre() + ".\n");
        }
    }

    /**
     * Comprueba si es posible edificar el tipo solicitado según las reglas:
     * - Casas: hasta 4
     * - Hotel: 4 casas y sin hotel
     * - Piscina: requiere hotel previo y sin piscina
     * - Pista de deporte: requiere piscina previa y sin pista
     *
     * @param tipoEdificio tipo de edificio
     * @return true si cabe construir; false en caso contrario
     */
    public boolean cabeEdificio(String tipoEdificio) {
        if (tipoEdificio.equals("Casa")) {
            return casas.size() < 4;
        }

        if (tipoEdificio.equals("Hotel")) {
            return casas.size() == 4 && hotel == null;
        }

        if (tipoEdificio.equals("Piscina")) {
            return hotel != null && piscina == null;
        }

        if (tipoEdificio.equals("Pista de deporte")) {
            return piscina != null && pistaDeporte == null;
        }

        return false;
    }

    /**
     * Devuelve el número de edificios del tipo indicado presentes en la casilla.
     *
     * @param tipo nombre del tipo ("Casa","Hotel","Piscina","Pista de deporte")
     * @return cantidad encontrada o -1 si el tipo no existe
     */
    public int numTipo(String tipo) {
        switch (tipo) {
            case "Casa":
                return casas.size();
            case "Hotel":
                return (hotel != null) ? 1 : 0;
            case "Piscina":
                return (piscina != null) ? 1 : 0;
            case "Pista de deporte":
                return (pistaDeporte != null) ? 1 : 0;
            default:
                return -1;
        }
    }

    /**
     * Vende edificios del tipo indicado. Este método se encarga tanto de eliminar
     * edificios de la casilla como de ajustar la fortuna del dueño y su listado de edificios.
     *
     * @param tipoEdificio tipo de edificio a vender (admite variaciones de mayúsculas/minúsculas)
     * @param numCasas     número de casas a vender cuando procede
     */
    public void venderEdificio(String tipoEdificio, int numCasas) {
        // Normalizamos la cadena tipoEdificio para comparar correctamente
        switch (tipoEdificio.substring(0, 1).toUpperCase() + tipoEdificio.substring(1).toLowerCase()) {
            case "Casas":
                // Si hay menos casas de las que se quieren vender, avisamos
                if (numTipo("Casa") < numCasas) {
                    consolaNormal.imprimir("La casilla " + this.getNombre() + " no tiene tantas casas para vender.\n");
                    return; //EXCEPTION
                }

                int numCasasInicial = numCasas;
                // Iterador para eliminar casas de la lista de la casilla
                Iterator<Casa> casasIt = casas.iterator();
                while (casasIt.hasNext() && numCasas > 0) {
                    Casa casa = casasIt.next();
                    casasIt.remove();
                    numCasas--;
                }

                // Eliminamos las casas del listado de edificios del dueño
                numCasas = numCasasInicial;
                Iterator<Edificio> duenhoCasasIt = getDuenho().getEdificios().iterator();
                while (duenhoCasasIt.hasNext() && numCasas > 0) {
                    Edificio casa = duenhoCasasIt.next();
                    if (casa.getSolar().equals(this)) {
                        duenhoCasasIt.remove();
                        numCasas--;
                    }
                }

                // Si no hay hotel, el dueño recibe el dinero de la venta de casas
                if (numTipo("Hotel") == 0) {
                    getDuenho().sumarFortuna(numCasasInicial * this.grupo.getPrecioCasa());
                    consolaNormal.imprimir(numCasasInicial + " casas en " + this.getNombre() + " han sido vendidas. La fortuna de " + this.getDuenho().getNombre() + " aumenta en " + (numCasasInicial * this.getGrupo().getPrecioCasa()) + ".\n");
                }
                break;

            case "Casa":
                // Comprobamos y vendemos una sola casa
                if (numTipo("Casa") == 0) {
                    consolaNormal.imprimir("La casilla +" + this.getNombre() + " no tiene ninguna casa para vender.");
                    return; //EXCEPTION
                } else {
                    getDuenho().eliminarEdificio(casas.getFirst());         //Obtiene la primera casa y la elimina del dueño
                    casas.removeFirst();                               //Elimina la casa del array de casas de la casilla
                    getDuenho().sumarFortuna(this.grupo.getPrecioCasa());   //Suma el precio de la casa a la fortuna del dueño
                    consolaNormal.imprimir("Una casa " + this.getNombre() + " ha sido vendida. La fortuna de " + this.getDuenho().getNombre() + " aumenta en " + this.grupo.getPrecioCasa() + ".\n");
                }
                break;

            case "Hotel":
                if (numTipo("Hotel") == 0) {
                    consolaNormal.imprimir("La casilla +" + this.getNombre() + " no tiene ningún hotel para vender.");
                    return; //EXCEPTION
                } else {
                    getDuenho().eliminarEdificio(hotel);       // Elimina hotel del dueño
                    hotel = null;
                    getDuenho().sumarFortuna(this.grupo.getPrecioHotel());       // Añade dinero al dueño
                    consolaNormal.imprimir("Un hotel en " + this.getNombre() + " ha sido vendido. La fortuna de " + this.getDuenho().getNombre() + " aumenta en " + this.grupo.getPrecioHotel() + ".\n");
                    // Si había piscina asociada, también se vende
                    if (this.numTipo("Piscina") > 0) {
                        venderEdificio("Piscina", 0);
                        consolaNormal.imprimir(("También se ha vendido la piscina asociada al hotel.\n"));
                    }
                }
                break;

            case "Piscina":
                if (numTipo("Piscina") == 0) {
                    consolaNormal.imprimir("La casilla +" + this.getNombre() + " no tiene ninguna piscina para vender.");
                    return; //EXCEPTION
                } else {
                    getDuenho().eliminarEdificio(piscina);      // Elimina piscina del dueño
                    piscina = null;
                    getDuenho().sumarFortuna(this.grupo.getPrecioPiscina());     // Añade dinero al dueño
                    consolaNormal.imprimir("Una piscina en " + this.getNombre() + " ha sido vendida. La fortuna de " + this.getDuenho() + " aumenta en " + this.grupo.getPrecioPiscina() + ".\n");
                    // Si había pista asociada, también se vende
                    if (this.numTipo("Pista de deporte") > 0) {
                        venderEdificio("Pista de deporte", 0);
                        consolaNormal.imprimir(("También se ha vendido la pista de deporte asociada a la piscina.\n"));
                    }
                }
                break;

            case "Pista de deporte":
                if (numTipo("Pista de deporte") == 0) {
                    consolaNormal.imprimir("La casilla +" + this.getNombre() + " no tiene ninguna pista de deporte para vender.");
                    return; //EXCEPTION
                } else {
                    getDuenho().eliminarEdificio(pistaDeporte);   // Elimina la pista del dueño
                    pistaDeporte = null;                              // Elimina la pista de la casilla
                    getDuenho().sumarFortuna(this.grupo.getPrecioPistaDeporte());  // Añade dinero al dueño
                    consolaNormal.imprimir("Una pista de deporte en " + this.getNombre() + " ha sido vendida. La fortuna de " + this.getDuenho() + " aumenta en " + this.grupo.getPrecioPistaDeporte() + ".\n");
                }
                break;

            default:
                break;
        }
    }

    /**
     * Devuelve una lista con todos los edificios presentes en la casilla.
     *
     * @return lista de Edificio en la casilla
     */
    public ArrayList<Edificio> edificiosCasilla() {
        ArrayList<Edificio> Edificios = new ArrayList<>();
        for (Casa c : casas) {
            Edificios.add(c);
        }
        if (hotel != null) {
            Edificios.add(hotel);
        }
        if (piscina != null) {
            Edificios.add(piscina);
        }
        if (pistaDeporte != null) {
            Edificios.add(pistaDeporte);
        }
        return Edificios;
    }

    /**
     * Ajusta los importes de alquiler asociados a cada tipo de edificio en este solar.
     *
     * @param casa   alquiler por cada casa
     * @param hotel  alquiler del hotel
     * @param piscina alquiler de la piscina
     * @param pista  alquiler de la pista de deporte
     */
    public void ajustarAlquileres(float casa, float hotel, float piscina, float pista) {
        this.alquilerCasa = casa;
        this.alquilerHotel = hotel;
        this.alquilerPiscina = piscina;
        this.alquilerPistaDeporte = pista;
    }

    /**
     * Calcula el importe total de alquiler de la casilla considerando casas y otros edificios.
     *
     * @return importe total a cobrar
     */
    @Override
    public float Valor() {
        float total = getAlquiler();
        total += casas.size() * alquilerCasa;
        if (hotel != null) {
            total += alquilerHotel;
        }
        if (piscina != null) {
            total += alquilerPiscina;
        }
        if (pistaDeporte != null) {
            total += alquilerPistaDeporte;
        }
        return total;
    }


    /**
     * Cuenta cuántos solares no hipotecados tiene el dueño.
     *
     * @param Duenho jugador propietario
     * @return número de solares activos del dueño
     */
    @Override
    public int contarCasillas(Jugador Duenho) {
        int i = 0;
        for (Propiedad prop : Duenho.getPropiedades()) { // Recorremos todas las propiedades del dueño
            if (prop instanceof Solar && !prop.estaHipotecada()) { // Se cuenta si es Solar y no está hipotecada
                i++;
            }
        }
        return i;
    }

    /**
     * Evalúa la casilla cuando un jugador cae en ella: si hay alquiler a cobrar,
     * comprueba si el jugador puede pagarlo; si el dueño es la banca, se usa el valor de compra.
     *
     * @param actual jugador que cae en la casilla
     * @param banca  referencia al jugador banca
     * @param tirada valor de la tirada (no usado)
     * @return true si la acción puede proceder (p.ej. puede pagar o no aplica cobro)
     * @throws NoPuedePagar si el jugador no puede pagar
     */
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) throws NoPuedePagar {
        if (!estaHipotecada() && !getDuenho().esBanca()) {
            return actual.puedePagar(this.Valor());
        } else if (getDuenho().equals(banca)) {
            return actual.puedePagar(getValor());
            // El jugador puede comprar la propiedad si no tiene dueño
        }
        return true; // El jugador ha pagado el alquiler o no se le ha cobrado
    }

    /**
     * Cobra el alquiler al moroso y lo paga al dueño si procede.
     *
     * @param duenho dueño que recibe el pago
     * @param moroso jugador que debe pagar
     * @param tirada valor de la tirada (no usado)
     * @throws NoPuedePagar si el moroso no puede pagar
     */
    @Override
    public void alquiler(Jugador duenho, Jugador moroso, int tirada) throws NoPuedePagar {
        if (evaluarCasilla(moroso, duenho, tirada)) {
            moroso.pagar(Valor());
            duenho.cobrar(Valor());
        }
    }

    /**
     * Calcula los gastos totales invertidos en los edificios de esta casilla.
     *
     * @return suma de precios de todos los edificios construidos
     */
    @Override
    public float gastosTotales(){
        float total=0;
        for(Casa c: casas){
            total+=c.getPrecio();
        }
        if(hotel!=null){
            total+=hotel.getPrecio();
        }
        if(piscina!=null){
            total+=piscina.getPrecio();
        }
        if(pistaDeporte!=null){
            total+=pistaDeporte.getPrecio();
        }
        return total;
    }


    /**
     * Construye un edificio sin realizar comprobaciones de pago (método utilitario).
     *
     * @param tipoEdificio tipo de edificio a construir
     * @param identificador identificador para el edificio
     */
    public void construirEdificio(String tipoEdificio, int identificador) {
        if(cabeEdificio(tipoEdificio)){
            switch (tipoEdificio) {
                case "Casa":
                    casas.add(new Casa(identificador, alquilerCasa, grupo.getPrecioCasa(), this, getDuenho()));
                case "Hotel":
                    hotel = new Hotel(identificador, alquilerHotel, grupo.getPrecioHotel(), this, getDuenho());
                case "Piscina":
                    piscina = new Piscina(0, alquilerPiscina, grupo.getPrecioPiscina(), this, getDuenho());
                case "Pista_De_Deporte":
                    pistaDeporte =  new Pista_De_Deporte(0, alquilerPistaDeporte, grupo.getPrecioPistaDeporte(), this, getDuenho());
                default:
                    return; // Lanzar NoExisteEdificio
            }
        }
        else{
            return; // Lanzar NoSePuedeEdificarException
        }
    }

    /**
     * Devuelve la representación para listados de venta.
     */
    @Override
    public String casEnVenta(){
        String mensaje = "{\n\tNombre:" + this.getNombre() +
                "\n\tTipo: Solar" +
                "\n\tGrupo: " + this.getGrupo() +
                "\n\tValor:" + this.getValor() +
                "\n}";
        return mensaje;
    }

    /**
     * Devuelve información detallada de la casilla, mostrando valores y alquileres
     * y, si tiene dueño, el nombre del mismo.
     */
    @Override
    public String infoCasilla() {
        if (this.getDuenho().getNombre().equals("banca")) {
            String mensaje = "{\n\tTipo: Solar" +
                    "\n\tGrupo: " + this.getGrupo() +
                    "\n\tValor:" + this.getValor() +
                    "\n\tAlquiler:" + this.getAlquiler() +
                    "\n\tValor Hotel:" + this.getGrupo().getPrecioHotel() +
                    "\n\tValor Casa:" + this.getGrupo().getPrecioCasa() +
                    "\n\tValor Piscina:" + this.getGrupo().getPrecioPiscina() +
                    "\n\tValor Pista de deporte:" + this.getGrupo().getPrecioPistaDeporte() +
                    "\n\tAlquiler Casa:" + alquilerCasa +
                    "\n\tAlquiler Hotel:" + alquilerHotel +
                    "\n\tAlquiler Piscina:" + alquilerPiscina +
                    "\n\tAlquiler Pista de deporte:" + alquilerPistaDeporte +
                    "\n}";
            return mensaje;

        } else {
            String mensaje = "{\n\tTipo: Solar" +
                    "\n\tGrupo: " + this.getGrupo() +
                    "\n\tPropietario:" + this.getDuenho().getNombre() +
                    "\n\tValor:" + this.getValor() +
                    "\n\tAlquiler:" + this.getAlquiler() +
                    "\n\tValor Hotel:" + this.getGrupo().getPrecioHotel() +
                    "\n\tValor Casa:" + this.getGrupo().getPrecioCasa() +
                    "\n\tValor Piscina:" + this.getGrupo().getPrecioPiscina() +
                    "\n\tValor Pista de deporte:" + this.getGrupo().getPrecioPistaDeporte() +
                    "\n\tAlquiler Casa:" + alquilerCasa +
                    "\n\tAlquiler Hotel:" + alquilerHotel +
                    "\n\tAlquiler Piscina:" + alquilerPiscina +
                    "\n\tAlquiler Pista de deporte:" + alquilerPistaDeporte +
                    "\n}";
            return mensaje;
        }
    }

    /**
     * Intenta hipotecar la casilla. Si tiene edificios, informa y no permite hipoteca.
     *
     * @throws ErrorHipoteca si la propiedad ya está hipotecada
     */
    @Override
    public void hipotecar() throws ErrorHipoteca {
        if (!this.edificiosCasilla().isEmpty()) {
            consolaNormal.imprimir("No se puede hipotecar la casilla " + getNombre() + " porque tiene edificios construidos.\n");
            if (numTipo("Casa") > 0) {
                consolaNormal.imprimir("Casas construidas: " + numTipo("Casa") + "\n");
            }
            if (numTipo("Hotel") > 0) {
                consolaNormal.imprimir("Hoteles construidos: " + numTipo("Hotel") + "\n");
            }
            if (numTipo("Piscina") > 0) {
                consolaNormal.imprimir("Piscinas construidas: " + numTipo("Piscina") + "\n");
            }
            if (numTipo("Pista de deporte") > 0) {
                consolaNormal.imprimir("Pistas de deporte construidas: " + numTipo("Pista de deporte") + "\n");
            }
            return;
        }
        if(!estaHipotecada()){
            getDuenho().sumarFortuna(getHipoteca());
            setHipotecada(true);
            consolaNormal.imprimir("La propiedad " + this.getNombre() + " ha sido hipotecada. El dueño " + getDuenho().getNombre() + " ha recibido " + getHipoteca() + "€.\n");
        } else{
            throw new ErrorHipoteca("Error: La propiedad " + this.getNombre() + " ya está hipotecada.\n");
        }
    }
}

