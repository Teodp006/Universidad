#include "abb.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "lista.h"
#define MAXLINE 1024
#define FDEFAULTNAME "personajes_output.txt"
#define MAX_NEW_FILE_LENGHT 50

// FUNCIONES PRIVADAS

/*Función para insertar un personaje con nombre [buffer] en la lista [listaPersonajes] de tipo [tipoPersonaje]
*
* @param *listaPersonajes puntero a la lista donde se desea guardar
* @param *buffer puntero al "nombre" del personaje que se va a guardar
* @param tipoPersonaje entero que codifica 1: Parents, 2: Siblings, 3: Killed, 4: Married/Engaged
*/
void _insertarPesonajeLista(TLISTA *listaPersonajes, char* buffer, short int tipoPersonaje){

    TIPOELEMENTOLISTA personajeLista;

    char mensaje[15];

    /*Modificamos el mensaje a mostrar en función según la lista donde vayamos a insertar el personaje*/
    switch(tipoPersonaje){
        case 1:
            strcpy(mensaje, "Parents");
            break;
        case 2:
            strcpy(mensaje, "Siblings");
            break;
        case 3:
            strcpy(mensaje, "Killed");
            break; 
        case 4:
            strcpy(mensaje, "MarriedEngaged");
            break;
    }

    /*Reservamos memoria para el nombre de dicho personaje*/
    personajeLista.name = (char*)malloc(strlen(buffer)+1);

    /*Guardamos el nombre real, en el campo correspondiente a la estructura*/
    strcpy(personajeLista.name, buffer);

    /*Aquí se podrían insertar más reservas de memoria, guardar más variables, en futuras
    extensiones de las listas de personajes*/

    /*Guardamos la estructura como nodo de la lista*/
    insertarElementoLista(listaPersonajes, finLista(*listaPersonajes), personajeLista);


    /*Pedimos información para el siguiente personaje y lo guardamos en buffer
    para que se actualice también su valor en el while() de AnhadirPersonaje*/
    printf("    %s (fin para terminar): ", mensaje);
    scanf(" %[^\n\r]", buffer);

}

/* Divide un Token en Subtokens separados por ',' e inserta cada Subtoken en la lista
* proporcionada
*
* @param *listaPersonajes puntero a la lista donde se van a insertar los Personajes
* @param *Token puntero al token (no modificable) que se va a procesar en subtokens
*
* @returns Numero de caracteres leídos/longitud de *Token
*/
int _cargarLista(TLISTA *listaPersonajes, const char* Token){
    /*Variable donde guardamos la longitud del Token procesado, para poder devolverlo*/
    int NumCars = strlen(Token);
    /*Variable donde se van a guardar de manera iterativa cada uno de los subTokens que componen el Token*/
    char *subToken;
    /*Dirección auxiliar donde guardaremos la información de Token*/
    char *aux = (char*)malloc(sizeof(char)*strlen(Token));
    strcpy(aux, Token);

    /*Insertamos el primer elemento, existe porque no detectamos '-' en el código anterior*/
    if((subToken = strtok(aux, ",")) != NULL){
        /*Utilizamos variable Local para que no se nos sobreesriba la información*/
        TIPOELEMENTOLISTA personajeLista;

        personajeLista.name = (char*)malloc(sizeof(char)*strlen(subToken));
        strcpy(personajeLista.name, subToken);

        insertarElementoLista(listaPersonajes,finLista(*listaPersonajes), personajeLista);
    }
    
    /*A partir de aquí tenemos que ir mirando si contiene más elementos la Token o solo tenía un
    elemento */
    while((subToken = strtok(NULL, ",") )!= NULL){
        /*Guardamos los demás elementos en otra variable Local, para que no interfiera con las estructuras
        anteriores */
        TIPOELEMENTOLISTA personajeLista;

        personajeLista.name = (char*)malloc(sizeof(char)*strlen(subToken));
        strcpy(personajeLista.name, subToken);

        insertarElementoLista(listaPersonajes,finLista(*listaPersonajes), personajeLista);
    }
    return NumCars;
}


/*Función para liberar el campo nombre de todos los personajes de [listaPersonajes], antes de liberar la lista que los contiene
*
* @param *listaPersonajes puntero a la lista que se desea liberar todos sus personajes
*/
void _liberarPersonajeLista(TLISTA *listaPersonajes){
    /*Definimos los punteros que nos ayudan a recorrer la lista*/
    TPOSICION personajeActual = primeroLista(*listaPersonajes);
    TPOSICION personajeFinal = finLista(*listaPersonajes);

    /*Elemento de la lista que guarda la información de un personaje*/
    TIPOELEMENTOLISTA personajeLista;

    while(personajeActual != personajeFinal){
        /*Accedemos a la información de cada personaje*/
        recuperarElementoLista(*listaPersonajes, personajeActual, &personajeLista);

        /*Liberamos sus campos*/
        free(personajeLista.name);

        /*Avanzamos al siguiente personaje*/
        personajeActual = siguienteLista(*listaPersonajes, personajeActual);
    }
}

/* Función para convertir el tipo dato CharType, enum de tipos de Personajes posibles
* a la versión cadena de caracteres de cada uno de los tipos
*
* @param TipoPersonaje variable donde se guarda un entero del 1 al 5, asociado con una constante del enum
* @return Nombre de la constante convertido string
*
* @exception NULL si [TipoPersonaje] < 1 o > 5
*/
char* _toString (CharType TipoPersonaje){
    switch (TipoPersonaje)
    {
    case PERSONA:
        return "persona\0";
        break;
    case GIGANTE:
        return "gigante\0";
        break;
    case LOBO:
        return "lobo\0";
        break;
    case DRAGON:
        return "dragon\0";
        break;
    case CRIATURA:
        return "criatura\0";
        break;
    default:
        return NULL;
        break;
    }
}
/*Funcion para convertir una cadena de caracteres a un tipo enumerado [CharType]
*
* @param *String puntero a cadena de caracteres
*
* @return TipoPersonaje variable donde se guarda un entero del 1 al 5, asociado con una constante del enum
* @exception -1 si [String] != "persona", "gigante", "lobo", "dragon" o "criatura"
*/
CharType _toEnum(char* String){
    CharType TipoPersonaje = -1;
    if (strcmp(String, "persona") == 0){
        TipoPersonaje = PERSONA;
        
    }
    else if (strcmp(String, "gigante") == 0){
        TipoPersonaje = GIGANTE;
    }
    else if (strcmp(String, "lobo") == 0){
        TipoPersonaje = LOBO;
    }
    else if (strcmp(String, "dragon") == 0){
        TipoPersonaje = DRAGON;
    }
    else if (strcmp(String, "criatura") == 0){
        TipoPersonaje = CRIATURA;
    }
    return TipoPersonaje;
}

/* Función para eliminar el '\n' de la línea y substituirlo por un '\0'
*/
void _strip_line (char * linea) {
    linea[strcspn(linea ,"\r\n")] = 0;
}

/* Función para imprimir el campo nombre de todos los personajes de [listaPersonajes] con el formato: nombre1, nombre2, nombre3
*
* @param listaPersonajes lista contenedora de los personajes
*/
void _imprimirLista(TLISTA listaPersonajes){
    /*Definimos los punteros que nos ayudan a recorrer la lista*/
    TPOSICION personajeActual = primeroLista(listaPersonajes);

    /*Elemento de la lista que guarda la información de un personaje*/
    TIPOELEMENTOLISTA personajeLista;

    while(siguienteLista(listaPersonajes, personajeActual) != finLista(listaPersonajes)){
        /*Accedemos al elemento de la lista*/
        recuperarElementoLista(listaPersonajes, personajeActual, &personajeLista);

        /*Imprimimos "<nombre>, " */
        printf("%s, ", personajeLista.name);

        /*Movemos el puntero al siguiente personaje*/
        personajeActual = siguienteLista(listaPersonajes, personajeActual);
    }
    /*Accedemos al último elemento*/
    recuperarElementoLista(listaPersonajes, personajeActual, &personajeLista);

    /*Imprimimos: "<nombre>\n" */
    printf("%s\n", personajeLista.name);
}

/*Funcion para imprimir una lista por pantalla y liberar sus campos, si no tiene nada se escribirá
* "|-" y no se liberará nada
*
* @param *archivo puntero al archivo donde se va a imprimir
* @param *listaPersonajes puntero a la lista que se va a imprimir y liberar su contenido
*/
void _imprimirLiberarListaArchivo(FILE* archivo, TLISTA *listaPersonajes){
    if(!esListaVacia(*listaPersonajes)){
        /*Definimos los punteros que nos ayudan a recorrer la lista*/
        TPOSICION personajeActual = primeroLista(*listaPersonajes);

        /*Elemento de la lista que guarda la información de un personaje*/
        TIPOELEMENTOLISTA personajeLista;

        /*Imprimimos el primer delimitador de la lista*/
        fprintf(archivo, "|");

        while(siguienteLista(*listaPersonajes, personajeActual) != finLista(*listaPersonajes)){
            /*Accedemos al elemento de la lista*/
            recuperarElementoLista(*listaPersonajes, personajeActual, &personajeLista);

            /*Imprimimos "<nombre>, " */
            fprintf(archivo, "%s,", personajeLista.name);

            /*Movemos el puntero al siguiente personaje*/
            personajeActual = siguienteLista(*listaPersonajes, personajeActual);
        }
        /*Imprimimos el último elemento sin la coma final*/
        recuperarElementoLista(*listaPersonajes, personajeActual, &personajeLista);
        
        /*Imprimimos "<nombre>" */
        fprintf(archivo, "%s", personajeLista.name);

        /*Liberamos la lista*/
        _liberarPersonajeLista(listaPersonajes);
    }
    else{
        /*Si la lista está vacía imprimimos "|-"*/
        fprintf(archivo, "|-");
    }
}

/* Función que sirve para imprimir toda la información existente de un personaje
* existente en el nodo (Si algún campo es NULL o la lista está vacía no lo imprimirá)
*
* @param personaje estructura que guarda los datos del personaje.
*
*/
void _imprimirPersonaje(TIPOELEMENTOABB personaje){
    /*Imprimimos el nombre del personaje*/
    printf("Name: %s\n", personaje.name);

    /*Imprimimos el tipo enumerado después de comprobar que es correcto, evitar imprimir basura o datos erróneos*/
    if(PERSONA <= personaje.character_type && personaje.character_type <= CRIATURA){
        printf("\tCharacter_type: %s\n", _toString(personaje.character_type));
    }
    
    /*Solo imprimimos si tiene sangre real*/
    if(personaje.royal == 1){
        printf("\tRoyal: TRUE\n");
    }
    /*Imprimimos la casa, en caso de que tenga*/
    if(personaje.house!=NULL){
        printf("\tHouse: %s\n", personaje.house);
    }
    /*Imprimimos todas las listas que tengan contenido*/
    if(!esListaVacia(personaje.parents)){
        printf("\tParents:  ");_imprimirLista(personaje.parents);
    }
    if(!esListaVacia(personaje.siblings)){
        printf("\tSiblings: ");_imprimirLista(personaje.siblings);
    }
    if(!esListaVacia(personaje.killed)){
        printf("\tKilled: ");_imprimirLista(personaje.killed);
    }
    if(!esListaVacia(personaje.marriedEngaged)){
        printf("\tMarried/Engaged: ");_imprimirLista(personaje.marriedEngaged);
    }
    /*Imprimimos la descripcion*/
    printf("\tDescription: %s\n", personaje.description);
}

/* Función de llamda recursiva utilziada para escribir todos los datos de los personajes
* contenidos en el [arbol] en un [archivo]
* @param *arbol puntero a el árbol que actúa como Base de Datos
* @param *archivo puntero al archivo donde se quieren escribir los datos
*
*/
void _escribirDatos(TABB *arbol, FILE *archivo){
    if(!esAbbVacio(*arbol)){
        /*Para esta función realizaremos un recorrido posorden, el que menos reacolocaciones necesita
            de los dos métodos "aleatorios" para esta base de datos: preorden y posorden*/
            TIPOELEMENTOABB personaje;

            if(izqAbb(*arbol)!=NULL){
                /*Primero miramos a la izquierda de nuestra raíz actual*/
                TABB arbolAux = izqAbb(*arbol);
                _escribirDatos(&arbolAux, archivo);
            }
            if(derAbb(*arbol)!=NULL){
                /*Luego a la derecha*/
                TABB arbolAux = derAbb(*arbol);
                _escribirDatos(&arbolAux, archivo);
            }
            /*Recuperamos la raíz*/
            leerElementoAbb(*arbol, &personaje);
            /*Guardamos uno a uno los datos del personaje en el archivo*/

            /*El nombre*/
            fprintf(archivo, "%s", personaje.name);

            /*El tipo de personaje en formato String*/
            fprintf(archivo, "|%s", _toString(personaje.character_type));

            /*La casa, en caso de que no tenga se guarda '-'*/
            fprintf(archivo, "|%s", (personaje.house == NULL) ? "-" : personaje.house);

            /*Si tiene sangre real (1) o no (0)*/
            fprintf(archivo, "|%d", personaje.royal);

            /*La lista de parents, si tiene, si no se escribe "-" (Ya implementado en la función)*/
            /*Como trabajamos con punteros, debemos liberar cada elemento de la lista antes
            de eliminar luego el nodo que contiene al personaje*/
            _imprimirLiberarListaArchivo(archivo, &personaje.parents);

            /*La lista de siblings, si tiene, si no se escribe "-"*/
            _imprimirLiberarListaArchivo(archivo, &personaje.siblings);
            
            /*La lista de killed, si tiene, si no se escribe "-"*/
            _imprimirLiberarListaArchivo(archivo, &personaje.killed);

            /*La lista de Married/Engaged, si tiene, si no se escribe "-"*/
            _imprimirLiberarListaArchivo(archivo, &personaje.marriedEngaged);

            /*Solo nos queda la descripción*/
            fprintf(archivo, "|%s", (personaje.description == NULL) ? "-" : personaje.description);
            fprintf(archivo, "\n");
    }
}

/* Función recursiva para recorrer todo el árbol e imprimir todos los personajes
* que sean de tipo [tipoPersonaje].
* @param arbol árbol que actúa como Base de Datos
* @param tipoPersonaje tipo de personaje a listar
*/
void _listarTipoPersonajeRec(TABB arbol, CharType tipoPersonaje, int *contador){

    if(!esAbbVacio(arbol)){
        /*Recorremos el árbol en inorden para imprimir en orden alfabético*/
        TIPOELEMENTOABB personaje;

        if(izqAbb(arbol)!=NULL){
            /*Si hay elementos menores, seguimos buscando el elemento más pequeño*/
            _listarTipoPersonajeRec(izqAbb(arbol), tipoPersonaje, contador);
        }

        /*Imprimimos la raíz si es del tipo indicado*/
        leerElementoAbb(arbol, &personaje);
        if(personaje.character_type == tipoPersonaje){
            _imprimirPersonaje(personaje);
            /*Aumentamos el contador*/
            (*contador)++;
        }

        if(derAbb(arbol)!=NULL){
            /*Si hay elementos mayores a la derecha, los buscamos después de imprimir la raíz*/
            _listarTipoPersonajeRec(derAbb(arbol), tipoPersonaje, contador);
        }
    }
}

/* Función para guardar el listaDestino una copia exacta de listaOrigen
*
* @param listaDestino lista vacía donde se van a copiar los datos
* @param listaOrigen lista que se quiere duplicar
*
*/
void _copiarLista(TLISTA listaDestino, TLISTA listaOrigen){
    /*Definimos los punteros que nos ayudan a recorrer la lista*/
    TPOSICION personajeActual = primeroLista(listaOrigen);
    TPOSICION personajeFinal = finLista(listaOrigen);

    /*Elemento de la lista uno que se a copiar*/
    TIPOELEMENTOLISTA personaje1;
    /*Elemento que se utiliza para copiar en la lista 2*/
    TIPOELEMENTOLISTA personaje2;

    while(personajeActual != personajeFinal){
        /*Accedemos a la información de cada personaje*/
        recuperarElementoLista(listaOrigen, personajeActual, &personaje1);

        /*Copiamos el campo TIPOELEMENTOLISTA*/
        personaje2.name = (char*)malloc(sizeof(char)*(strlen(personaje1.name)+1));
        strcpy(personaje2.name, personaje1.name);

        /*Insertamos el personaje en la nueva lista*/
        insertarElementoLista(&listaDestino, finLista(listaDestino), personaje2);

        /*Avanzamos al siguiente personaje*/
        personajeActual = siguienteLista(listaOrigen, personajeActual);
    }
}

//////////////////////////////////////////


// FUNCIONES PÚBLICAS


/* Función para añadir un personaje al árbol
* 
* @param arbol Puntero al árbol que actúa como base de datos
*
*/
void AnhadirPersonaje(TABB * arbol) {
    /*Variable Nodo del ÁrbolBinario de Búsqueda donde se guarda el personaje*/
    TIPOELEMENTOABB personaje;

    /*Variable para alojar las cadenas de caracteres antes de asociarles memoria dinámica*/
    char buffer[1024];

    /*Pedimos en nombre del personaje*/
    printf("    Name: ");
    scanf(" %[^\n\r]", buffer);

    /*Reserva de memoria dinámica para el nombre del personaje*/
    personaje.name = (char*) malloc((strlen(buffer) + 1) * sizeof (char));
    strcpy(personaje.name, buffer);

    /*Comprobamos que el personaje no exista en el árbol*/
    if(!esMiembroAbb(*arbol, personaje)){
        /*Pedimos más datos*/

        /*Guardamos el tipo de personaje*/
        printf("    Char_type (1: persona , 2: gigante , 3: lobo , 4: dragon , 5: criatura): ");
        scanf(" %d", (int*)&personaje.character_type);

        /*Validamos que sea un tipo de personaje existente*/
        while(personaje.character_type < PERSONA || personaje.character_type > CRIATURA){
            printf("\033[1;31m\033[1m\t\tERROR:\033[0mWrong Char_type. Type a number between 1 and 5: ");
            scanf(" %d", (int*)&personaje.character_type);
        }

        /*Guardamos la información de la casa*/
        printf("    House (- si desconocido): ");
        scanf(" %[^\n\r]", buffer);

        /*Si no se conoce la casa se guarda el valor NULL*/
        if(strcmp(buffer, "-") == 0){
            personaje.house = NULL;
        }
        else{
            personaje.house = (char*) malloc((strlen(buffer) + 1) * sizeof (char));
            strcpy(personaje.house, buffer);
        }

        /*Guardamos si tiene sangre real o no*/
        printf("    Royal (0/1): ");
        scanf(" %hd", &personaje.royal);

        /*Validamos que el dato sea correcto*/
        while(personaje.royal < 0 || personaje.royal > 1){
            printf("\033[1;31m\033[1m\t\tERROR:\033[0mWrong value, 0 or 1: ");
            scanf(" %hd", &personaje.royal);
        }

        /*Guardamos la información de los padres del personaje*/

        /*Creamos la lista vacía*/
        crearLista(&personaje.parents);

        /*Comprobamos que el usuario quiera introducir algún padre*/
        printf("    Parents (fin para terminar): ");
        scanf(" %[^\n\r]", buffer);

        /*Agregamos padres del personaje mientras que no introduzca "fin"*/
        while(strcmp(buffer, "fin") !=0){
            _insertarPesonajeLista(&personaje.parents, buffer, 1);
        }
        /*Guardamos la información de los hijos del personaje*/

        /*Creamos la lista vacía*/
        crearLista(&personaje.siblings);

        /*Comprobamos que el usuario quiera introducir algún padre*/
        printf("    Siblings (fin para terminar): ");
        scanf(" %[^\n\r]", buffer);

        /*Agregamos hijos del personaje mientras que no introduzca "fin"*/
        while(strcmp(buffer, "fin") !=0){
            _insertarPesonajeLista(&personaje.siblings, buffer, 2);
        }

        /*Guardamos la información de los asesinados por el personaje*/

        /*Creamos la lista vacía*/
        crearLista(&personaje.killed);

        /*Comprobamos que el usuario quiera introducir algún padre*/
        printf("    Killed (fin para terminar): ");
        scanf(" %[^\n\r]", buffer);

        /*Agregamos los asesinados por el personaje mientras que no introduzca "fin"*/
        while(strcmp(buffer, "fin") !=0){
            _insertarPesonajeLista(&personaje.killed, buffer, 3);
        }

        /*Guardamos la información de los casados/comprometidos con el personaje*/

        /*Creamos la lista vacía*/
        crearLista(&personaje.marriedEngaged);

        /*Comprobamos que el usuario quiera introducir algún padre*/
        printf("    Married/Engaged (fin para terminar): ");
        scanf(" %[^\n\r]", buffer);

        /*Agregamos casados del personaje mientras que no introduzca "fin"*/
        while(strcmp(buffer, "fin") !=0){
            _insertarPesonajeLista(&personaje.marriedEngaged, buffer, 4);
        }

        /*Guardamos la descripción del personaje*/
        printf("Description: ");
        scanf(" %[^\n\r]", buffer);
        personaje.description = (char*) malloc((strlen(buffer) + 1) * sizeof (char));
        strcpy(personaje.description, buffer);


        /*Insertamos el personaje en el árbol de personajes*/
        insertarElementoAbb(arbol, personaje);
        printf("Personaje %s insertado correctamente\n", personaje.name);

    }
    else{
        printf("El personaje %s ya existe en el árbol\n", personaje.name);
        free(personaje.name);
    }
}


/* Función para eliminar un personaje del árbol por su clave 
* 
* @param arbol Puntero al árbol que actúa como base de datos
*
*/
void EliminarPersonaje(TABB * arbol) {
    /*Comprobamos que haya posibles elementos a eliminar en el arbol*/
    if(!esAbbVacio(*arbol)){
        /*Variable donde se guarda la clave con la que se busca al elemento que se desea eliminar.
        Si se desea se puede cambiar la clave de ordenación del arbol y modificar el contenido
        de esta función para un caso más amplio, por ahora buscaremos por el nombre*/

        /*Variable donde se guarda el nombre del personaje a aliminar*/
        TIPOCLAVE nombre;

        /*Variable que corresponde al personaje a eliminar del Árbol binario*/
        TIPOELEMENTOABB personaje;

        /*Reservamos memoria para el nombre*/
        nombre = (TIPOCLAVE)malloc(sizeof(char)*SIZE_NAME);

        /*Pedimos datos al usuario*/
        printf("Nombre del personaje a eliminar: ");
        scanf(" %[^\n\r]", nombre);

        /*Reservamos memoria para el puntero de la estrucura del personaje*/
        personaje.name = (char*)malloc(sizeof(char)*(strlen((char*)nombre)+1));

        /*Guardamos la clave en el personaje*/
        strcpy(personaje.name, (char*)nombre);

        /*Comprobamos que está en el arbol*/
        if(esMiembroAbb(*arbol, personaje)){
            /*Guarda todos los demás datos en la estructura del personaje*/
            buscarNodoAbb(*arbol, personaje.name, &personaje);

            /*Accedemos a todas las listas del personaje (Parents, Siblings, Killed ,Married/Engaged)
            para liberar cada uno de los campos de la estructura que contienen los nodos de la lista*/
            if(longitudLista(personaje.parents) > 0){
                /*Si tiene elementos liberamos los campos de cada uno de los elementos*/
                _liberarPersonajeLista(&personaje.parents);
            }

            /*Liberamos los personajes de la lista de hijos*/
            if(longitudLista(personaje.siblings) > 0){
                _liberarPersonajeLista(&personaje.siblings);
            }

            /*Liberamos los personajes de la lista de asesinados*/
            if(longitudLista(personaje.killed) > 0){
                _liberarPersonajeLista(&personaje.killed);
            }

            /*Liberamos los personajes de la lista de enamorados/casados*/
            if(longitudLista(personaje.marriedEngaged) > 0){
                _liberarPersonajeLista(&personaje.marriedEngaged);
            }

            /*El resto de campos que contiene el personaje ya se liberan con la
            liberación del Nodo del Árbol Binario de Búsqueda*/
            suprimirElementoAbb(arbol, personaje);
            printf("El personaje %s se elimino correctamente de la Base de Datos\n", nombre);
        }
        else{
            printf("\033[1;32m\033[1mADVICE:\033[0m El personaje %s no se encuentra en la Base de Datos\n", nombre);
        }
        /*Liberamos la memoria reservada que no vamos a utilizar*/
        free(nombre);
    }
    else{
    printf("\033[1;32m\033[1mADVICE:\033[0m No existe ningún personaje en la Base de Datos\n");
    }
}


/* Función para imprimir todos los personajes del árbol por orden alfabético (según su clave, en este caso el nombre) 
* 
* @param arbol Puntero al árbol que actúa como base de datos
*
*/
void ListarPersonajes(TABB arbol){
    /*Comprobamos que haya posibles elementos a eliminar en el arbol*/
    if(!esAbbVacio(arbol)){
        /*Para esta función realizaremos un recorrido inorden, para imprimir en orden alfabético*/
        TIPOELEMENTOABB personaje;

        if(izqAbb(arbol)!=NULL){
            /*Si hay elementos menores, seguimos buscando el elemento más pequeño*/
            ListarPersonajes(izqAbb(arbol));
        }

        /*Imprimimos la raíz*/
        leerElementoAbb(arbol, &personaje);
        _imprimirPersonaje(personaje);

        if(derAbb(arbol)!=NULL){
            /*Si hay elementos mayores a la derecha, los buscamos después de imprimir la raíz*/
            ListarPersonajes(derAbb(arbol));
        }
    }
    else{
    printf("\033[1;32m\033[1mADVICE:\033[0m No existe ningún personaje en la Base de Datos\n");
    }
}

/* Función para leer de un archivo formateado: nombre|tipo|casa|real(0/1)|parents1,parents2...|siblings1,siblings2...|killed1,killed2...|married1,married2...|description
* y guardar cada línea como un personaje en el arbol
*
* @param arbol Puntero al árbol que actúa como base de datos
* @param numParams número de parámetros introducidos en la línea de comandos
* @param **Params array de cadenas de caracteres con los parámetros introducidos en la línea de comandos
*
*/
void cargar_archivo(TABB *arbol, int numParams, char **Params){
    FILE *archivo = NULL;
    /*Numero de caracteres de cada línea hasta el comienzo de la siguiente token que representa una lista
    y está subdividida en subtokens separados por ',' */
    int NumCars = 0;

    /*Variable para almacenar el personaje temporalmente*/
    TIPOELEMENTOABB personaje;

    /*Variables para leer y procesar el archivo*/
    char linea[MAXLINE],*token;

    if (numParams == 3){
        if((archivo = fopen (Params[2], "r")) != NULL ){
            while(fgets(linea, MAXLINE, archivo) != NULL){
                /*Eliminamos el '\n' de la cadena "linea"*/
                _strip_line(linea);
                /*Guardamos en token, cada uno de los campos separados por '|' que tenemos en la línea*/
                token = strtok(linea, "|");
                /*Leemos tantos caracteres como ocupa el nombre más uno |*/
                NumCars += strlen(token)+1;
                /*El primer campo se corresponde con el nombre: Reservamos memoria y copiamos el valor*/
                personaje.name = (char*)malloc(sizeof(char)*strlen(token));
                strcpy(personaje.name,token);

                /*El segundo campo se corresponde con el tipo de caracter, debemos transformarlo a entero, para guardarlo en el enum*/
                token = strtok(NULL, "|");
                /*Longitud del token actual + 1 (Separador '|') de caracteres leídos*/
                NumCars += strlen(token)+1;
                personaje.character_type = _toEnum(token);

                /*Comprovamos que exista su casa*/
                token = strtok(NULL, "|");
                if(strcmp(token, "-") == 0){
                    personaje.house = NULL;
                    /*Solo hemos leído '-|'*/
                    NumCars += 2;

                }
                else{
                    personaje.house = (char*)malloc(sizeof(char)*strlen(token));
                    strcpy(personaje.house,token);
                    /*Sumamos los caracteres leídos*/
                    NumCars += strlen(token)+1;
                }
                /*El cuarto campo es si es real o no, para ello también lo tenemos que transformar a entero*/
                token = strtok(NULL, "|");
                sscanf(token, "%d", (int*)&personaje.royal);
                /*Aquí también leímos solo 2 caracteres '0|'*/
                NumCars += 2;

                /*El siguiente token corresponde a la lista de padres, para ello
                hemos creado la siguiente función para hacerlo más fácil*/
                token = strtok(NULL, "|");
                /*La lista se crea en cualquier caso*/
                crearLista(&personaje.parents);
                /*Comprovamos si está vacía o no*/
                if(strcmp(token, "-") != 0){
                    /*Sumamos los caracteres procesados en esta operacion más el separador final "|"*/
                    NumCars += _cargarLista(&personaje.parents, token) + 1;
                }
                else{
                    /*En este caso solo avanzamos dos caracteres "-|"*/
                    NumCars += 2;
                }

                /*Como modificamos el puntero global de strtok, ahora tenemos que
                volver a empezar desde el comienzo del token siguiente (por eso se suma NumCars)*/
                crearLista(&personaje.siblings);
                /*Mismo proceso que parents*/
                token = strtok(linea+NumCars, "|");
                if(strcmp(token, "-") != 0){
                    /*Sumamos los caracteres procesados en esta operacion más el separador final "|"*/
                    NumCars += _cargarLista(&personaje.siblings, token) + 1;
                }
                else{
                    /*En este caso solo avanzamos dos caracteres "-|"*/
                    NumCars += 2;
                }

                crearLista(&personaje.killed);
                /*Seguimos con la lista de Killed*/
                token = strtok(linea+NumCars, "|");
                if(strcmp(token, "-") != 0){
                    /*Sumamos los caracteres procesados en esta operacion más el separador final "|"*/
                    NumCars += _cargarLista(&personaje.killed, token) + 1;
                }
                else{
                    /*En este caso solo avanzamos dos caracteres "-|"*/
                    NumCars += 2;
                }

                /*Por último la lista de Married/Engaged*/
                crearLista(&personaje.marriedEngaged);
                token = strtok(linea+NumCars, "|");
                if(strcmp(token, "-") != 0){
                    /*Sumamos los caracteres procesados en esta operacion*/
                    NumCars += _cargarLista(&personaje.marriedEngaged, token) + 1;
                }
                else{
                    /*En este caso solo avanzamos dos caracteres "-|"*/
                    NumCars += 2;
                }
                /*Leemos el último token y guardamos la descripcion*/
                token = strtok(linea+NumCars, "|");
                personaje.description = (char*)malloc(sizeof(char)*strlen(token));
                strcpy(personaje.description,token);
                /*El último parámetro no acaba en "|"*/
                NumCars += strlen(personaje.description);
                /*Reseteamos el valor de NumCars*/
                NumCars = 0;
                
                insertarElementoAbb(arbol, personaje);
                
            }
            /*Cerramos el archivo para guardar su contenido*/
            fclose(archivo);
        }
    }
    else{
        printf("\033[1;33m\033[1mWARNING:\033[0m Formato a utilizar %s \"-f <nombre_archivo>\"\n", Params[0]);
    }
}


/* Función para guardar los datos del árbol en el siguiente formato:
* nombre|tipo|casa|real(0/1)|parents1,parents2...|siblings1,siblings2...|killed1,killed2...|married1,married2...|description
* y liberar todos los nodos del mismo (para no tener que volver a recorrerlo con otro método de destrucción)
*
* @param arbol Puntero al árbol que actúa como base de datos
* @param numParams número de parámetros introducidos en la línea de comandos
* @param **Params array de cadenas de caracteres con los parámetros introducidos en la línea de comandos
*/
void guardar_archivo(TABB *arbol, int numParams, char **Params){
    FILE *archivo = NULL;
    char nombre_archivo[MAX_NEW_FILE_LENGHT];
    if (numParams == 3) {
         strcpy(nombre_archivo, Params[2]);
    }
    else{
        *nombre_archivo = 0;
    }
    /*Comprobamos que se ha entregado un nombre de archivo válido*/
    while((archivo = fopen (nombre_archivo, "w")) == NULL ){
        printf("\033[1;31m\033[1mERROR:\033[0m\tEl archivo aportado no es válido, ingrese \"fin\" para utilizar un nombre predeterminado o ingrese un nombre válido: ");
        scanf("%s", nombre_archivo);
        if(strcmp(nombre_archivo, "fin") == 0){
            strcpy(nombre_archivo, FDEFAULTNAME);
        }
    }
    if(esAbbVacio(*arbol)){
        printf("\033[1;32m\033[1mADVICE:\033[0m Su archivo de salida no va a contener información porque no ha creado ningún personaje\n");
    }
    else{
        /*LLamamos a la función que nos imprime el árbol*/
        _escribirDatos(arbol, archivo);
    }
    /*Cerramos el archivo para guardar su contenido cuando se hayan escrito todos los personajes*/
    fclose(archivo);
}

/* Función para buscar un personaje en el árbol e imprimir todos sus datos por pantalla
*
*@param arbol árbol que actúa como Base de Datos
*
*/
void BuscarPersonaje(TABB arbol){
    /*Comprobamos que haya posibles elementos a eliminar en el arbol*/
    if(!esAbbVacio(arbol)){
        /*Variable donde se guarda el nombre del personaje a buscar*/
        TIPOCLAVE nombre;

        /*Variable que corresponde al personaje a buscar del Árbol binario*/
        TIPOELEMENTOABB personaje;

        /*Reservamos memoria para el nombre*/
        nombre = (TIPOCLAVE)malloc(sizeof(char)*SIZE_NAME);

        /*Pedimos datos al usuario*/
        printf("Nombre del personaje a buscar: ");
        scanf(" %[^\n\r]", nombre);

        /*Reservamos memoria para el puntero de la estrucura del personaje*/
        personaje.name = (char*)malloc(sizeof(char)*(strlen((char*)nombre)+1));

        /*Guardamos la clave en el personaje*/
        strcpy(personaje.name, (char*)nombre);

        /*Comprobamos que está en el arbol*/
        if(esMiembroAbb(arbol, personaje)){
            /*Guarda todos los demás datos en la estructura del personaje*/
            buscarNodoAbb(arbol, personaje.name, &personaje);

            /*Imprimimos toda la información del personaje*/
            _imprimirPersonaje(personaje);
        }
        else{
            printf("\033[1;32m\033[1mADVICE:\033[0m El personaje %s no se encuentra en la Base de Datos\n", nombre);
        }
        /*Liberamos la memoria reservada que no vamos a utilizar*/
        free(nombre);
    }
    else{
    printf("\033[1;32m\033[1mADVICE:\033[0m No existe ningún personaje en la Base de Datos\n");
    }
}

/*
* Función para listar todos los personajes de [arbol] que sea de un tipo determinado
* contenido en el tipo de dato CharType
* @param arbol árbol que actúa como Base de Datos
*
*/

void ListarTipoPersonaje(TABB arbol){
    /*Variable que cuenta el número de personajes del tipo indicado*/
    int totalPersonajes = 0;

    /*Comprobamos que haya posibles elementos a eliminar en el arbol*/
    if(!esAbbVacio(arbol)){
        /*Variable donde se guarda el tipo de personaje a buscar*/
        CharType tipoPersonaje = 0;

        /*Pedimos datos al usuario*/
        printf("Character_type (1: persona , 2: gigante , 3: lobo , 4: dragon , 5: criatura): ");
        scanf(" %d", (int*)&tipoPersonaje);

        /*Validamos que sea un tipo de personaje existente*/
        while(tipoPersonaje < PERSONA || tipoPersonaje > CRIATURA){
            printf("\033[1;31m\033[1m\t\tERROR:\033[0mWrong Char_type. Type a number between 1 and 5: ");
            scanf(" %d", (int*)&tipoPersonaje);
        }

        /*Llamamos a la función recursiva que imprime los personajes del tipo indicado*/
        _listarTipoPersonajeRec(arbol, (CharType)tipoPersonaje, &totalPersonajes);

        if(totalPersonajes == 0){
            printf("\033[1;32m\033[1mADVICE:\033[0m No hay personajes del tipo %s en la Base de Datos\n", _toString(tipoPersonaje));
        }
        else{
            printf("Se encontraron %d %s\n", totalPersonajes, _toString(tipoPersonaje));
        }
    }
    else{
    printf("\033[1;32m\033[1mADVICE:\033[0m No existe ningún personaje en la Base de Datos\n");
    }
}


/* Función para modificar cualquier dato de un personaje existente en [arbol] imprimiendo
* su contenido antes y después de la modificación
*
* @param arbol árbol que actúa como Base de Datos
*
*/
void ModificarPersonaje(TABB * arbol){
    /*Comprobamos que haya posibles elementos a eliminar en el arbol*/
    if(!esAbbVacio(*arbol)){
        /*Variable donde se guarda el nombre del personaje a buscar*/
        TIPOCLAVE nombre;

        /*Variable que corresponde al personaje a buscar del Árbol binario*/
        TIPOELEMENTOABB personaje;

        /*Variable para alojar las cadenas de caracteres antes de asociarles memoria dinámica*/
        char buffer[1024];

        /*Variable que guarda los nuevos datos del personaje*/
        TIPOELEMENTOABB nuevoPersonaje;

        /*Reservamos memoria para el nombre*/
        nombre = (TIPOCLAVE)malloc(sizeof(char)*SIZE_NAME);

        /*Pedimos datos al usuario*/
        printf("Nombre del personaje a modificar: ");
        scanf(" %[^\n\r]", nombre);

        /*Reservamos memoria para el puntero de la estrucura del personaje*/
        personaje.name = (char*)malloc(sizeof(char)*(strlen((char*)nombre)+1));

        /*Guardamos la clave en el personaje*/
        strcpy(personaje.name, (char*)nombre);

        /*Comprobamos que está en el arbol*/
        if(esMiembroAbb(*arbol, personaje)){
            /*Guarda todos los demás datos en la estructura del personaje*/
            buscarNodoAbb(*arbol, personaje.name, &personaje);

            /*Imprimimos toda la información del personaje*/
            _imprimirPersonaje(personaje);

            /*Pedimos que información se desea modificar*/
            printf("\n ----Que informacion desea modificar?----\n");
            printf("\t1. Name\n");
            printf("\t2. Character Type\n");
            printf("\t3. House\n");
            printf("\t4. Royal\n");
            printf("\t5. Parents\n");
            printf("\t6. Siblings\n");
            printf("\t7. Killed\n");
            printf("\t8. Married/Engaged\n");
            printf("\t9. Description\n");
            printf("\t10. Cancelar\n");
            printf("Seleccione una opción: ");
            int opcion;
            scanf(" %d", &opcion);
            while(opcion < 1 || opcion > 10){
                printf("\033[1;31m\033[1m\t\tERROR:\033[0m Opción inválida. Seleccione una opción entre 1 y 10: ");
                scanf(" %d", &opcion);
            }

            
            switch(opcion){
                /*Solo la opción 1 es independiente, el resto pueden modificar la misma estructura*/
                case 1:
                    /*Cambiamos el campo a modificar*/
                    printf("\tNew Name: ");
                    scanf(" %[^\n\r]", buffer);
                    /*Comprobamos que el nuevo nombre no exista en el árbol*/
                    nuevoPersonaje.name = (char*)malloc(sizeof(char)*(strlen(buffer)+1));
                    strcpy(nuevoPersonaje.name, buffer);
                    while(esMiembroAbb(*arbol, nuevoPersonaje)){
                        printf("\033[1;31m\033[1m\t\tERROR:\033[0m El personaje %s ya existe en la Base de Datos, ingrese otro nombre: ", buffer);
                        /*Si existe volvemos a liberar la memoria y reescribir con otro nombre*/
                        scanf(" %[^\n\r]", buffer);
                        free(nuevoPersonaje.name);
                        nuevoPersonaje.name = (char*)malloc(sizeof(char)*(strlen(buffer)+1));
                        strcpy(nuevoPersonaje.name, buffer);
                    }
                    /*Simplemente duplicamos el resto de campos, con los enteros podemos hacerlo directamente*/
                    nuevoPersonaje.character_type = personaje.character_type;
                    nuevoPersonaje.royal = personaje.royal;
                    /*Para que no se libere la memoria dinámica duplicamos los punteros*/
                    if(personaje.house != NULL){
                        nuevoPersonaje.house = (char*)malloc(sizeof(char)*(strlen(personaje.house)+1));
                        strcpy(nuevoPersonaje.house, personaje.house);
                    }
                    else{
                        nuevoPersonaje.house = NULL;
                    }
                    nuevoPersonaje.description = (char*)malloc(sizeof(char)*(strlen(personaje.description)+1));
                    strcpy(nuevoPersonaje.description, personaje.description);

                    /*Para las listas simplemente igualamos punteros y creamos una nueva lista para el viejo
                    personaje, de esta forma nos aseguramos que no se libere al hacer destruirLista(&listaPersonajes)
                    dentro de la función destruirNodoABB*/
                    crearLista(&nuevoPersonaje.parents);
                    _copiarLista(nuevoPersonaje.parents, personaje.parents);

                    crearLista(&nuevoPersonaje.siblings);
                    _copiarLista(nuevoPersonaje.siblings, personaje.siblings);

                    crearLista(&nuevoPersonaje.killed);
                    _copiarLista(nuevoPersonaje.killed, personaje.killed);
    

                    crearLista(&nuevoPersonaje.marriedEngaged);
                    _copiarLista(nuevoPersonaje.marriedEngaged, personaje.marriedEngaged);


                    /*Eliminamos el personaje antiguo*/
                    suprimirElementoAbb(arbol, personaje);
                    
                    /*Insertamos el nuevo personaje*/
                    insertarElementoAbb(arbol, nuevoPersonaje);
                    printf("Nombre modificado correctamente\n");

                    /*Mostramos los datos del nuevo personaje*/
                    printf("\033[1;32m\033[1m\tADVICE:\033[0m Datos del nuevo personaje:\n");
                    _imprimirPersonaje(nuevoPersonaje);
                    break;
                case 2:
                    nuevoPersonaje = personaje;
                    /*Modificar tipo de personaje*/
                    printf("\tNew Character Type (1: persona , 2: gigante , 3: lobo , 4: dragon , 5: criatura): ");
                    scanf(" %d", (int*)&nuevoPersonaje.character_type);
                    /*Validamos que sea un tipo de personaje existente*/
                    while(nuevoPersonaje.character_type < PERSONA || nuevoPersonaje.character_type > CRIATURA){
                        printf("\033[1;31m\033[1m\t\tERROR:\033[0mWrong Char_type. Type a number between 1 and 5: ");
                        scanf(" %d", (int*)&nuevoPersonaje.character_type);
                    }
                    /*Imprimimos un pequeño mensaje de resultado de la operación modificar*/
                    if(nuevoPersonaje.character_type == personaje.character_type){
                        printf("\033[1;33m\033[1m\t\tWARNING:\033[0m Llamaste a la función modificar, para no cambiar nada\n");
                    }
                    break;
                case 3:
                    /*Variable auxiliar en caso de que la casa antigua fuera NULL*/
                    char buffer[1024];

                    nuevoPersonaje = personaje;
                    
                    if(personaje.house == NULL){
                        /*Si la casa es NULL debemos reservar memoria para la nueva*/
                        printf("New House (la casa era desconocida, - para no hacer nada): ");
                        scanf(" %[^\n\r]", buffer);
                        if(strcmp(buffer, "-") != 0){ /*Si queremos otra casa nueva reservamos memoria*/
                            nuevoPersonaje.house = (char*) malloc((strlen(buffer) + 1) * sizeof (char));
                            strcpy(nuevoPersonaje.house, buffer);
                            
                        }
                        else{ /*Si no simplemente no hay que hacer nada*/
                            printf("\033[1;33m\033[1mWARNING:\033[0m Llamaste a la función modificar, para no cambiar nada\n");
                            break;
                        }
                    }
                    else{
                        /*Si no es NULL lo único que vamos a hacer es copiar el nuevo valor*/
                        printf("New House (- para eliminar la antigua): ");
                        scanf(" %[^\n\r]", buffer);
                        if(strcmp(buffer, "-") == 0){
                            /*Si ya existe tenemos que liberarla (en caso de que se quiera marcar como desconocido)*/
                            free(personaje.house);
                            nuevoPersonaje.house = NULL;
                        }
                        else{
                            /*Si no se desea eliminar simplemente copiamos el nuevo valor*/
                            strcpy(nuevoPersonaje.house, buffer);
                        }
                    }
                    break;
                case 4:
                    /*Simplemente invertimos el valor*/
                    printf((personaje.royal == 1)? "El personaje era real, se cambiará a no real\n":"El personaje era no real, se cambiará a real\n");
                    nuevoPersonaje = personaje;
                    /*Comprobamos que era antes para poner lo opuesto*/
                    nuevoPersonaje.royal = (personaje.royal == 1) ? 0 : 1;
                    break;
                case 5:
                    nuevoPersonaje = personaje;
                    /*Modificar lista de padres*/
                    printf("Solo se pueden añadir personajes a la lista de Parents, no eliminar\n");
                    printf("    Parents (fin para terminar): ");
                    scanf(" %[^\n\r]", buffer);
                    /*Agregamos padres del personaje mientras que no introduzca "fin"*/
                    while(strcmp(buffer, "fin") !=0){
                        _insertarPesonajeLista(&nuevoPersonaje.parents, buffer, 1);
                    }
                    break;
                case 6:
                    nuevoPersonaje = personaje;
                    /*Modificar lista de hijos*/
                    printf("Solo se pueden añadir personajes a la lista de siblings, no eliminar\n");
                    printf("    Siblings (fin para terminar): ");
                    scanf(" %[^\n\r]", buffer);
                    /*Agregamos hijos del personaje mientras que no introduzca "fin"*/
                    while(strcmp(buffer, "fin") !=0){
                        _insertarPesonajeLista(&nuevoPersonaje.siblings, buffer, 2);
                    }
                    break;
                case 7:
                    nuevoPersonaje = personaje;     
                    /*Modificar lista de asesinados*/
                    printf("Solo se pueden añadir personajes a la lista de Killed, no eliminar\n");
                    printf("    Killed (fin para terminar): ");
                    scanf(" %[^\n\r]", buffer);
                    /*Agregamos los asesinados por el personaje mientras que no introduzca "fin"*/
                    while(strcmp(buffer, "fin") !=0){
                        _insertarPesonajeLista(&nuevoPersonaje.killed, buffer, 3);
                    }
                    break;
                case 8:
                    nuevoPersonaje = personaje;
                    printf("Solo se pueden añadir personajes a la lista de Married/Engaged, no eliminar\n");

                    printf("    Married/Engaged (fin para terminar): ");
                    scanf(" %[^\n\r]", buffer);
                    /*Agregamos casados del personaje mientras que no introduzca "fin"*/
                    while(strcmp(buffer, "fin") !=0){
                        _insertarPesonajeLista(&nuevoPersonaje.marriedEngaged, buffer, 4);
                    }
                    break;
                case 9:
                    /*Copiamos todos los datos*/
                    nuevoPersonaje = personaje;
                    /*Liberamos la descripción antigua*/
                    free(personaje.description);

                    /*Modificar descripción*/
                    printf("New Description: ");
                    scanf(" %[^\n\r]", buffer);

                    /*Reservamos memoria y copiamos la nueva descripción*/
                    nuevoPersonaje.description = (char*) malloc((strlen(buffer) + 1) *sizeof (char));
                    strcpy(nuevoPersonaje.description, buffer);

                    break;
                case 10:
                    printf("Cancelando modificación...\n");
                    break;
                }
            if(opcion != 10 && opcion != 1){
                /*Si no se ha cancelado la operación o no se ha modificado el nombre
                simplemente actualizamos el nodo del árbol, ya que no hay que cambiar
                la posición del nodo en el arbol, solo su contenido*/
                modificarElementoAbb(*arbol, nuevoPersonaje);
                printf("Modificación realizada correctamente\n");
                printf("\033[1;32m\033[1mADVICE:\033[0m Datos del personaje modificado:\n");
                _imprimirPersonaje(nuevoPersonaje);
            }
        }
        else{
            printf("\033[1;31m\033[1mERROR:\033[0m El personaje %s no se encuentra en la Base de Datos\n", nombre);
        }
        /*Liberamos la memoria reservada que no vamos a utilizar*/
        free(nombre);
    }
    else{
    printf("\033[1;32m\033[1mADVICE:\033[0m No existe ningún personaje en la Base de Datos\n");
    }
}
