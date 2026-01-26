# ifndef BACKTRAKING_H
# define BACKTRAKING_H
# include "lista.h"

// FUNCIONES PÚBLICAS

/*
* Estructura que permite almacenar información sobre el coste de ejecución de algunas
* instrucciones del algoritmo de Backtraking y el total de ejecuciones del algoritmo (equivalente a NumNodos).
* 
*
*/
typedef struct {
    int NumNodos; /* Cada vez que se ejecuta generar y el nodo cumple el criterio se cuenta como nodo (no se cuentan los nodos inválidos) */
    int PasosGenerar; /* Total de pasos realizados por la función Generar() */
    int PasosCriterio; /* Total de pasos realizados por la función Criterio()*/
    int PasosSolucion; /* Total de pasos realizados por la función Solución()*/
    int PasosMasHermanos; /* Total de pasos realizados por la función MasHermanos()*/
    int PasosRetroceder; /* Total de pasos realizados por la función Retroceder()*/
} EstadisticasBacktraking;



/* Algoritmo que explora todas las posibles soluciones para un problema de asignación
* de una matriz de beneficion B[N][N] (N galeones y N ciudades), maximizando el beneficio total
* @param solucion[] Vector donde se almacenará la solución óptima encontrada
* @param B[][] Matriz de beneficios donde B[i][j] es el beneficio de asignar el galeón i a la ciudad j
* @param modo discriminante para ejecutar backtraking sin vector usadas (0) o con vector usadas (1)
*/
void backtraking(int solucion[N], int B[N][N], int modo);

/* Función que imprime los resultado de ejecución del algoritmo de Backtraking
* como el número de nodos generados o el número de instrucciones totales que ejecutó cada
* función contando todas sus ejecuciones en una tabla.
*
* @param stats puntero a la estructura con las estadísticas del algoritmo.
*/
void imprimir_estadisticas_backtraking(EstadisticasBacktraking* stats);


#endif /* BACKTRAKING_H */