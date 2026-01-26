#ifndef FUNCIONES_H
#define FUNCIONES_H



/* Función para imprimir una solución encontrada [soa] y su beneficio asociado para un problema de asignación
* de galeones (filas) a ciudades (columnas) representado por la matriz de beneficios [B].
*
* @param B[][] Matriz de beneficios donde B[i][j] es el beneficio de asignar el galeón i a la ciudad j
* @param soa[] Vector con la solución óptima encontrada s[i] = j, donde
*/
void imprimir_solucion(int B[N][N], int soa[N]);



#endif /* FUNCIONES_H */