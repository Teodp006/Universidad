#ifndef PilaOperadores_H
#define PilaOperadores_H

//Interfaz TAD PilaOperadores
typedef void *PilaOperadores; /*tipo opaco*/

//CONTENIDO DE CADA ELEMENTO DE LA Pila
//MODIFICAR: PARA LA Pila DE Operadores: char
//MODIFICAR: PARA LA Pila DE OPERANDOS: abin
typedef char tipoelemOperadores;

//Funciones de creacion y destruccion
/**
 * Crea la PilaOperadores vacia. 
 * @param P Puntero a la PilaOperadores. Debe estar inicializada.
 */
void crearPilaOperadores(PilaOperadores *P);

/**
 * Destruye la PilaOperadores
 * @param P puntero a la PilaOperadores
 */
void destruirPilaOperadores(PilaOperadores *P);

//Funciones de informacion
/**
 * Comprueba si la PilaOperadores esta vacia
 * @param P PilaOperadores
 */
unsigned esVaciaPilaOperadores(PilaOperadores P);

/*
 * Recupera la informacion del tope de la PilaOperadores
 * @param P PilaOperadores
 * 
*/
tipoelemOperadores topeOperadores(PilaOperadores P);

//Funciones de insercion/eliminacion
/**
 * Inserta un nuevo nodo en la PilaOperadores para el elemento E
 * en el tope de la PilaOperadores
 * @param P puntero a la PilaOperadores
 * @param E Informacion del nuevo nodo. 
 */
void pushOperadores(PilaOperadores *P, tipoelemOperadores E);

/**
 * Suprime el elemento en el tope de la PilaOperadores
 * @param P puntero a la PilaOperadores
 */
void popOperadores(PilaOperadores *P);

#endif	// PilaOperadores_H

