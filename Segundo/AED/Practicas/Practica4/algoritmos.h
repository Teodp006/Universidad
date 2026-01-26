#ifndef ALGORITMOS_H
#define ALGORITMOS_H


/* Función que implementa el algoritmo de Floyd_Warshall para una Matriz de Valores D y una de Predecesores P ya inicializadas
* @param D: matriz de valores entre nodos (Distancias, velocidades, costes...)
* @param P: matriz de predecesores entre nodos
*/
void Floyd_Warshall(int N, float V[N][N], int P[N][N]);

/* Función que inicializa las matrices D y P para el caso de tiempo o distancia mínima con distintas velocidades según el tipo de conexión.
* Para matriz de distancias (km), usar velocidad_tierra = 1 y velocidad_mar = 1. Si no 
*
* @param N: número de nodos del grafo
* @param D: matriz de valores entre nodos (Distancias, velocidades, costes...)
* @param P: matriz de predecesores entre nodos
* @param G: grafo del que se extraerá la información para inicializar las matrices
* @param velocidad_tierra: velocidad de viaje en tierra (km/h)
* @param velocidad_mar: velocidad de viaje en mar (km/h)
*/
void inicializar_matrizPonderada(int N, float D[N][N], int P[N][N], grafo G, float velocidad_tierra, float velocidad_mar);

/* Función para imprimir el camino más corto/rápido entre dos nodos utilizando una matriz de predecesores P transformada por Floyd-Warshall
* con el formato Ciudad1 --[valor unidad]--> Ciudad2 ~~[valor unidad]~~> Ciudad3 ... CiudadN
*
* @param origen: índice del vértice de origen (0 a N-1)
* @param destino: índice del vértice de destino (0 a N-1)
* @param N: número de nodos del grafo
* @param P: matriz de predecesores entre nodos (tamaño N x N)
* @param G: grafo del que se extraerá la información para imprimir los nombres de los vértices y las distancias entre pares
* @param unidad: cadena de caracteres que indica la unidad de medida ("km" para distancias o "h" para tiempos)
*
* @example Distancias: Pyke ~~[500 km]~~> Seagard | En caballo: Pyke ~~[44.44 h]~~> Seagard
*/
void imprimir_camino(int origen, int destino, int N, int P[N][N], float D[N][N], grafo G, const char *unidad);


/* Función para aplicar el algoritmo del cálculo del árbol de expresión de tiempo o distancia mínimo de un grafo
*  ponderado en distancias. Para distancias, usar velocidad_tierra = 1 y velocidad_mar = 1.
*
* @param G: grafo sobre el que se aplicará el algoritmo de Prim
* @param velocidad_tierra: velocidad de viaje en tierra (km/h)
* @param velocidad_mar: velocidad de viaje en mar (km/h)
*/
void Prim(grafo G, float velocidad_tierra, float velocidad_mar);
#endif /* ALGORITMOS_H */