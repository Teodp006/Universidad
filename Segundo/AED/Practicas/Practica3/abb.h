#ifndef ABB_H
#define ABB_H
#include "lista.h"

/**
 * Tipo de dato abstracto para arbol binario con clave de
 * ordenacion y elemento de celda.
 */

///////////////////////////////////////INICIO PARTE MODIFICABLE

#define SIZE_NAME 50

typedef char* TIPOCLAVE; /*Para ordenar por el nombre*/

typedef enum {
    PERSONA = 1,
    GIGANTE,
    LOBO,
    DRAGON,
    CRIATURA
} CharType;

typedef struct{
    char *name;
    CharType character_type; /*(1: persona , 2: gigante , 3: lobo , 4: dragon , 5: criatura )*/
    char *house;
    short int royal; /*0 no es real, 1 si lo es*/
    TLISTA parents; /*Lista de padres del personaje*/
    TLISTA siblings; /*lista de hijos del personaje*/
    TLISTA killed; /*lista de personajes asesinados por el personaje*/
    TLISTA marriedEngaged; /*lista de personajes con los que está/estuvo casado*/
    char* description; /*descripción del personaje*/

} TIPOELEMENTOABB;

//////////////////////////////////////////FIN PARTE MODIFICABLE
		
typedef struct celda * TABB;//tipo opaco

/////////////////////////////// FUNCIONES

//FUNCIONES DE CREACIÓN Y DESTRUCCIÓN DEL �?RBOL
/**
 * Crea el arbol vacio.
 * @param A Puntero al arbol. Debe estar inicializado.
 */
void crearAbb(TABB *A);

/**
 * Destruye el arbol recursivamente
 * @param A El arbol que queremos destruir
 */
void destruirAbb(TABB *A);

//FUNCIONES DE INFORMACIÓN
/**
 * Comprueba si el arbol esta vacio
 * @param A El arbol binario
 */
unsigned esAbbVacio(TABB A);

/**
 * Devuelve el subarbol izquierdo de A
 * @param A - Arbol original
 */
TABB izqAbb(TABB A);
/**
 * Devuelve el subarbol derecho de A
 * @param A - Arbol original
 */
TABB derAbb(TABB A);
/**
 * Recupera la informacion de la celda de la raiz del arbol
 * @param A
 * @param E Puntero al nodo que apuntara a la informacion
 */
void leerElementoAbb(TABB A, TIPOELEMENTOABB *E);

/**
 * Comprueba si el elemento <E> existe en el arbol <A>
 * @param A
 * @param E
 */
unsigned esMiembroAbb(TABB A, TIPOELEMENTOABB E);

/**
 * Busca un nodo con clave <cl> en el arbol y, si existe
 * modifica el puntero <nodo> para que lo apunte.
 * @param A Arbol binario en el que se busca el nodo.
 * @param cl Clave que se buscara.
 * @param nodo Puntero a un tipoelem.
 */
void buscarNodoAbb(TABB A, TIPOCLAVE cl, TIPOELEMENTOABB *nodo);

//FUNCIONES DE MODIFICACIÓN
/**
 * Inserta un nuevo nodo en el arbol para el elemento E
 * del que toma su clave. Esta clave no debe existir en
 * el arbol.
 * @param A Arbol binario
 * @param E Informacion del nuevo nodo.
 */
void insertarElementoAbb(TABB *A, TIPOELEMENTOABB E);
/**
 * Suprime el nodo del arbol <A>
 * @param A
 * @param E
 */
void suprimirElementoAbb(TABB *A,TIPOELEMENTOABB E);
/**
 * Busca el nodo con clave la del nodo y lo modifica.
 * @param A
 * @param nodo
 */
void modificarElementoAbb(TABB A, TIPOELEMENTOABB nodo);

#endif	// ABB_H

