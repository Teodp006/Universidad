#ifndef PilaOperandos_H
#define PilaOperandos_H
#include "abin.h"

//Interfaz TAD PilaOperandos
typedef void *PilaOperandos; /*tipo opaco*/

//CONTENIDO DE CADA ELEMENTO DE LA Pila
//MODIFICAR: PARA LA Pila DE OPERADORES: char
//MODIFICAR: PARA LA Pila DE Operandos: abin
typedef abin tipoelemOperandos;

//Funciones de creacion y destruccion
/**
 * Crea la PilaOperandos vacia. 
 * @param P Puntero a la PilaOperandos. Debe estar inicializada.
 */
void crearPilaOperandos(PilaOperandos *P);

/**
 * Destruye la PilaOperandos
 * @param P puntero a la PilaOperandos
 */
void destruirPilaOperandos(PilaOperandos *P);

//Funciones de informacion
/**
 * Comprueba si la PilaOperandos esta vacia
 * @param P PilaOperandos
 */
unsigned esVaciaPilaOperandos(PilaOperandos P);

/*
 * Recupera la informacion del tope de la PilaOperandos
 * @param P PilaOperandos
 * 
*/
tipoelemOperandos topeOperandos(PilaOperandos P);

//Funciones de insercion/eliminacion
/**
 * Inserta un nuevo nodo en la PilaOperandos para el elemento E
 * en el tope de la PilaOperandos
 * @param P puntero a la PilaOperandos
 * @param E Informacion del nuevo nodo. 
 */
void pushOperandos(PilaOperandos *P, tipoelemOperandos E);

/**
 * Suprime el elemento en el tope de la PilaOperandos
 * @param P puntero a la PilaOperandos
 */
void popOperandos(PilaOperandos *P);

#endif	// PilaOperandos_H

