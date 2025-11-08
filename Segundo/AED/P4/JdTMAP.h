#ifndef JdTMAP_H
#define JdTMAP_H

#include "grafo.h"
#define velocidad_tierra_caballo 5.5 // Velocidad en tierra en km/h (caballo)
#define velocidad_mar_caballo 11.25 // Velocidad en mar en km/h (caballo)
#define velocidad_dragon 80.0 // Velocidad en tierra y mar constante en km/h (dragón)

//FUNCIONES DEL PROGRAMA DE PRUEBA DE GRAFOS

/*Opción a del menú, introducir un vertice (ciudad) nuevo en el grafo (mapa)
* @param G: grafo donde se insertará el nuevo vértice
* @exception Si existe ya una ciudad con el mismo nombre, no se hará nada (No es posible modificar la región de la ciudad con esta función).
*/
void agregar_ciudad(grafo *G);

/*Opción b del menú, eliminar un vértice (ciudad) del grafo (mapa) pidiendo su nombre
* @param G: grafo del que se eliminará el vértice
*/
void eliminar_ciudad(grafo *G);


/* Opcion c del menú, crear una relación (ruta) entre dos vértices (ciudades) indicando el tipo de conexión y la distancia en kilómetros
* @param G: grafo donde se insertará la nueva relación
* @exception Si alguna de las ciudades no existe, se finalizará la ejecución de la función
*/
void nueva_ruta(grafo *G);

/*Opción d del menú, eliminar una relación (ruta) entre dos vértices (ciudades)
* @param G: grafo del que se eliminará la relación
* @exception Si alguna de las ciudades no existe, se finalizará la ejecución de la función
*/
void eliminar_ruta(grafo *G);

/* Opción i del memú, para imprimir todos los vértices y las conexiones de ese vértice del Grafo
* @param G: grafo a imprimir
*/
void imprimir_mapa(grafo G);

/* Función que lee un archivo de texto pasado por línea de comandos con el formato: c1,r1,c2,r2,d,t y lo guarda en el grafo [G]
* @param *G: puntero al grafo donde se almacenará la información del archivo
* @param **argv: array de argumentos de línea de comandos
* @param argc: número de argumentos de línea de comandos
* @exception Si no se puede abrir el archivo, se muestra un mensaje de error y la función retorna sin modificar el grafo
*/
void leer_archivo(grafo *G, char** argv, int argc);

/* Función que guarda las conexiones del grafo [G] (sin repetir) en un archivo de texto llamado [filename] con el formato: c1,r1,c2,r2,d,t
* @param G: puntero al grafo donde se almacenará la información del archivo.
* @param argv: array de argumentos de línea de comandos
* @param argc: número de argumentos de línea de comandos
* @exception Si no se puede abrir el archivo, se muestra un mensaje de error y probablemente se pierda la información pervia del archivo.
*/
void guardar_archivo(grafo G, char **argv, int argc);

/* Opcion e del menú, para calcular la distancia mínima en kilómetro entre dos ciudades usando Floyd-Warshall
* y mostrando las distancias entre las ciudades intermedias para llegar al destino.
* @param G: grafo donde se calculará la distancia mínima
* @exception Si alguna de las ciudades no existe, se informa al usuario y se finaliza la ejecución de la función
*/
void distancia_minima(grafo G);

/* Opcion f del menú, para calcular el tiempo mínimo entre dos ciudades usando Floyd-Warshall en función del medio de transporte
* que puede ser caballo o dragón ('c' o 'd'), también muestra los tiempos entre las ciudades intermedias para llegar al destino.
* @param G: grafo donde se calculará el tiempo mínimo
* @exception Si alguna de las ciudades no existe, se informa al usuario y se finaliza la ejecución de la función
*/
void tiempo_minimo(grafo G);
#endif	/* JdTMAP_H */

