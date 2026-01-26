#ifndef RAMIPODA_H
#define RAMIPODA_H

/* Optimización por ramificación y poda para el problema de asignación de galeones a ciudades sin repetición
* , maximizando el beneficio total. Imprime también los nodos generados y una información sobre cada nodo generado en
* estructura de "árbol".
* @param solucion[] Vector donde se almacenará la solución óptima encontrada
* @param B[][] Matriz de beneficios donde B[i][j] es el beneficio de asignar el galeón i a la ciudad j
* @param modo discriminante para ejecutar ramificación y poda con estimaciones tribiales (0) o precisas (1)
* @param numNodos puntero al contador de nodos generados
*/
void ramificacion_poda(NODO *s, int B[N][N], int modo, int *numNodos);

#endif /* RAMIPODA_H */