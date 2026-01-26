#ifndef JUEGODETRONOS_H
#define JUEGODETRONOS_H
#include "abb.h"

/* Función para añadir un personaje al árbol
* 
* @param arbol Puntero al árbol que actúa como base de datos
*
*/
void AnhadirPersonaje(TABB * arbol);

/* Función para eliminar un personaje del árbol por su clave 
* 
* @param arbol Puntero al árbol que actúa como base de datos
*
*/
void EliminarPersonaje(TABB * arbol);

/* Función para imprimir todos los personajes del árbol por orden alfabético (según su clave, en este caso el nombre) 
* 
* @param arbol Puntero al árbol que actúa como base de datos
*
*/
void ListarPersonajes(TABB arbol);

/* Función para leer de un archivo formateado: nombre|tipo|casa|real(0/1)|parents1,parents2...|siblings1,siblings2...|killed1,killed2...|married1,married2...|description
* y guardar cada línea como un personaje en el arbol
*
* @param arbol Puntero al árbol que actúa como base de datos
* @param numParams número de parámetros introducidos en la línea de comandos
* @param **Params array de cadenas de caracteres con los parámetros introducidos en la línea de comandos
*
*/
void cargar_archivo(TABB *arbol, int numParams, char **Params);

/* Función para guardar los datos del árbol en el siguiente formato:
* nombre|tipo|casa|real(0/1)|parents1,parents2...|siblings1,siblings2...|killed1,killed2...|married1,married2...|description
* y liberar todos los nodos del mismo (para no tener que volver a recorrerlo con otro método de destrucción)
*
* @param arbol Puntero al árbol que actúa como base de datos
* @param numParams número de parámetros introducidos en la línea de comandos
* @param **Params array de cadenas de caracteres con los parámetros introducidos en la línea de comandos
*/
void guardar_archivo(TABB *arbol, int numParams, char **Params);

/* Función para buscar un personaje en el árbol e imprimir todos sus datos por pantalla
*
*@param arbol árbol que actúa como Base de Datos
*
*/
void BuscarPersonaje(TABB arbol);

/*
* Función para listar todos los personajes de [arbol] que sea de un tipo determinado
* contenido en el tipo de dato CharType
* @param arbol árbol que actúa como Base de Datos
*
*/
void ListarTipoPersonaje(TABB arbol);

/* Función para modificar cualquier dato de un personaje existente en [arbol] imprimiendo
* su contenido antes y después de la modificación
*
* @param arbol árbol que actúa como Base de Datos
*
*/
void ModificarPersonaje(TABB * arbol);
#endif    // FUEGODETRONOS_H
