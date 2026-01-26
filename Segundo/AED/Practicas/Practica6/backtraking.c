#include "lista.h"
#include "backtraking.h"
#include <stdio.h>

/* Garantiza una asignación de ciudades a galeones sin repetición, comprobando que la ciudad Nº nivel no haya sido asignada previamente en ninguno de los
* otros N galeones
*
* @param s[] Vector con la solución parcial s[i] = j, donde i es el número de galeón y j la ciudad asignada
* @param nivel Nivel actual en el árbol de soluciones (último galeón asignado)
* @param modo discriminante para ejecutar Criterio() sin vector usadas (0) o con vector usadas (1)
* @param usadas[i] Cada posición i del vector indica el número de que la ciudad i ha sido asignada a cualquier galeón (Solo se utiliza con modo = 1)
* @param PasosCriterio puntero al total de pasos realizados por las funciones de criterio en una ejecución de Backtraking (Utilizado para obtener estadísticas de coste computacional)
* @return 1 si la ciudad Nº nivel no ha sido asignada previamente, 0 en otro caso
 */
int _Criterio(int nivel, int s[], int modo, int usadas[], int* PasosCriterio){
    if(modo == 0){
        for(int i = 0; i < nivel; i++){
            (*PasosCriterio)++; /* Criterio incrementa en un paso por cada comparación */
            if(s[i] == s[nivel]){
                return 0; // Ciudad ya usada
            }
        }
        return 1; // Ciudad no usada
    }
    else if(modo == 1){ /* Lo que es lo mismo, si se ha usado solo una vez, la asignación es correcta*/
        (*PasosCriterio)++;
        return usadas[s[nivel]] == 1;         /* Con vector usadas solo requiere 1 paso extra */
        
    }
    else{
        return 0; /* En caso de error, no se cumple el criterio*/
    }
}


/* Comprueba que una solución es completa, es decir que las N ciudades han sido asignadas
* a un galeón.
*
* @param nivel Nivel actual en el árbol de soluciones
* @param s[] Vector con la solución parcial s[i] = j, donde i es el número de galeón y j la ciudad asignada
* @param modo discriminante para ejecutar Solucion() sin vector usadas (0) o con vector usadas (1)
* @param usadas[i] Cada posición i del vector indica el número de que la ciudad i ha sido asignada a cualquier galeón (Solo se utiliza con modo = 1)
* @param pasosSolucion puntero al total de pasos realizados por las funciones de solución en una ejecución de Backtraking (Utilizado para obtener estadísticas de coste computacional)
* @param pasosCriterio puntero al total de pasos realizados por las funciones de criterio en una ejecución de Backtraking (Utilizado para obtener estadísticas de coste computacional)
* @return 1 si la solución es completa, 0 en otro caso
*/
int _Solucion(int nivel, int s[], int modo, int usadas[], int* pasosCriterio){
     /* Si hemos asignado todas las ciudades y se cumple el criterio entonces es solución*/
    return (nivel == N-1 && _Criterio(nivel, s, modo, usadas, pasosCriterio));
    
}

/* Permite retroceder un nivel en el árbol de soluciones, desasignando el galeón de la última ciudad
* asignada, la ciudad Nº nivel de las N totales.
*
* @param nivel Nivel actual en el árbol de soluciones (última ciudad asignada)
* @param s[] Vector con la solución parcial s[i] = j, donde i es el número de galeón y j la ciudad asignada
* @param bact puntero al beneficio actual de la solución parcial s[]
* @param B[][] Matriz de beneficios donde B[i][j] es el beneficio de asignar el galeón i a la ciudad j
* @param modo discriminante para ejecutar Retroceder() sin vector usadas (0) o con vector usadas (1)
* @param usadas[] Cada posición i del vector indica el número de veces que ese galeón ha sido asignado una ciudad (Solo se utiliza con modo = 1)
* @param PasosRetroceder puntero al total de pasos realizados por las funciones de retroceso en una ejecución de Backtraking (Utilizado para obtener estadísticas de coste computacional)
*/
void _Retroceder(int *nivel, int s[], int *bact, int B[N][N], int modo, int usadas[]){
    if(modo == 1){
        usadas[s[*nivel]]--; /* Decrementamos el contador de veces que se ha usado esta ciudad */
    }
    *bact -= B[*nivel][s[*nivel]]; /* Restamos el beneficio de la última asignación realizada */
    s[*nivel] = -1; /* Marcamos esta ciudad como inexplorada */
    *nivel -= 1; /* Retrocedemos un nivel en el árbol de soluciones */
}


/* Función que comprueba si para el galeon Nº nivel quedan más ciudades por asignar (es decir, más posibles nodos en ese nivel del
* árbol de soluciones)
*
* @param nivel Nivel actual en el árbol de soluciones
* @param s[] Vector con la solución parcial s[i] = j, donde i es el número de galeón y j la ciudad asignada
* @param PasosMasHermanos puntero al total de pasos realizados por las funciones de mas hermanos en una ejecución de Backtraking (Utilizado para obtener estadísticas de coste computacional)
* @return 1 si quedan más ciudades por asignar al galeón Nº nivel, 0 en otro caso
*/
int _MasHermanos(int nivel, int s[], int *PasosMasHermanos){
    (*PasosMasHermanos)++; /* Un paso garantizado para comparar s[nivel] con N-1*/
    return s[nivel]<N-1; /* Si en este nivel no hemos probado las N ciudades, entonces quedan posibilidades por generar*/
}

/* Actualiza el beneficio actual de la solución parcial s[] al asignar al galeón Nº nivel la siguiente ciudad
* sin comprobar si la asignación es válida o no
*
* @param nivel Nivel actual en el árbol de soluciones (último galeón asignado)
* @param s[] Vector con la solución parcial s[i] = j, donde i es el número de galeón y j la ciudad asignada
* @param bact puntero al beneficio actual de la solución parcial s[]
* @param B[][] Matriz de beneficios donde B[i][j] es el beneficio de asignar el galeón i a la ciudad j
* @param modo discriminante para ejecutar Generar() sin vector usadas (0) o con vector usadas (1)
* @param usadas[] Cada posición i del vector indica el número de veces que ese galeón se le ha asignado una ciudad (Solo se utiliza con modo = 1)
* @param PasosGenerar puntero al total de pasos realizados por las funciones de generación en una ejecución de Backtraking (Utilizado para obtener estadísticas de coste computacional)
* @param Numodos es el puntero al total de nodos generados en una ejecución de Backtraking, aumenta solo cuando se cumple el criterio de solución parcial.
*/
void _Generar(int nivel, int s[], int *bact, int B[N][N], int modo, int usadas[]){
    if(modo == 1){
        if(s[nivel] != -1){ /* Si no es la primera vez que se asigna una ciudad a este galeón, se decrementa el contador de veces que se ha usado la ciudad anterior*/
            usadas[s[nivel]]--;
        }
        s[nivel] = s[nivel]+1; /* Asignamos la siguiente ciudad al galeón Nº nivel */
        usadas[s[nivel]]++; /* Incrementamos el contador de veces que se ha usado esta ciudad */
    }
    else if (modo == 0){
        s[nivel]=s[nivel]+1; /* Si no utilizamos el vector usadas simplemente asignamos al galeón Nº nivel la siguiente ciudad*/
    }
    if (s[nivel] == 0){ /* Si es la primera ciudad que le asigno al galeón le sumo el beneficio de haber obtenido dicho ataque*/
        *bact += B[nivel][s[nivel]];
    }
    else{ /* Si no es la primera ciudad asignada a ese galeón, le resto el beneficio de no haber atacado la anterior y le sumo el beneficio de haber atacado esta nueva*/
        *bact += B[nivel][s[nivel]]-B[nivel][s[nivel]-1];
    }
}
    



// NOTAS
/* Renombŕe el vector usadas a atacadas para facilitar la compresión del contexto del problema 
*  Solo en la función de backtraking() porque es la función general, las demás podrían ser reutilizadas
*  para otro código por eso las dejé con el formato original*/

/* Algoritmo que explora todas las posibles soluciones para un problema de asignación
* de una matriz de beneficion B[N][N] (N galeones y N ciudades), maximizando el beneficio total, puede
* ejecutarse utilizando un vector para controlar las ciudades ya asignadas o no, además imprime información
* sobre el coste computacional de varias funciones auxiliares y el Nº de nodos generados.
* @param solucion[] Vector donde se almacenará la solución óptima encontrada
* @param B[][] Matriz de beneficios donde B[i][j] es el beneficio de asignar el galeón i a la ciudad j
* @param modo discriminante para ejecutar backtraking sin vector usadas (0) o con vector usadas (1)
*/
void backtraking(int solucion[N], int B[N][N], int modo){
    EstadisticasBacktraking stadsB = {0,0,0,0,0,0}; // Estructura para almacenar las estadísticas del algoritmo
    int nivel = 0; // Nivel actual en el árbol de soluciones
    int s[N]; // Vector para la solución parcial
    int soa[N];  // Vector para la solución con mayor beneficio (problema de maximización)
    for(int i = 0; i < N; i++){s[i] = -1; soa[i] = s[i];} /* Inicializamos el vector solución parcial y solución global a -1 (ningún galeón ha sido asignado a ninguna ciudad) */
    int voa = 0; // Valor óptimo actual, beneficio asignado al vector soa[]
    int bact = 0; // Beneficio para el vector actual s[]
    int atacadas[N] = {0}; // Vector para ciudades atacadas por galeones (Inicialmente ninguna ha sido atacada)
    int flag = -1; // Variable auxiliar para guardar el resultado de Criterio de la iteración actual
    stadsB.NumNodos++; /* Contamos la raíz como nodo generado */
    while(nivel != -1){ // El algoritmo acaba cuando se vuelve a la raíz y se retrocede otro nivel
        _Generar(nivel, s, &bact, B, modo, atacadas); // Generamos la ciudad Nº nivel y le asignamos un galeón (empezando por el primero)
        stadsB.PasosGenerar++; /* Como Generar() es O(1) se cuenta 1 paso por ejecución*/
        stadsB.NumNodos++; /* A priori debe ser un nodo generado */
        stadsB.PasosSolucion++;
        if(_Solucion(nivel, s, modo, atacadas, &stadsB.PasosCriterio) && bact > voa ){ // Si la asignación de galeones a ciudades es correcta y el beneficio obtenido es mayor, se actualizan los máximos
            voa = bact;
            for(int i = 0; i < N; i++){
                soa[i] = s[i];
            }
        }
        if((flag = _Criterio(nivel, s, modo, atacadas, &stadsB.PasosCriterio)) && nivel < N-1){ // Si la ciudad no ha sido asignada a otro galeón y todos los galeones no han sido asignados, entonces se avanza a la siguiente ciudad
            nivel++;
        }
        else if(!flag){ /* Si no se cumple el criterio, este nodo no es válido y no se cuenta como nodo generado*/
            stadsB.NumNodos--; 
        }
        while(!_MasHermanos(nivel, s, &stadsB.PasosMasHermanos) && nivel >= 0){ // Si no quedan más posibles galeones por asignar a dicha ciudad, luego tenermos que probar otras combinaciones de ciudades anteriores
            _Retroceder(&nivel, s, &bact, B, modo, atacadas); // Retrocedemos un nivel en el árbol de soluciones
            stadsB.PasosRetroceder++; 
        }
        // En el caso de que ya en el nivel 0 (raíz) hayamos probado todas las combinaciones, entonces el algoritmo acaba
    }
    // Al finalizar el algoritmo, copiamos la mejor solución encontrada al vector solución pasado como parámetro
    for(int i = 0; i < N; i++){
        solucion[i] = soa[i];
    }
    imprimir_estadisticas_backtraking(&stadsB); /* Imprimimos las estadísticas del algoritmo */
}


/* Función que imprime los resultado de ejecución del algoritmo de Backtraking
* como el número de nodos generados o el número de instrucciones totales que ejecutó cada
* función contando todas sus ejecuciones en una tabla.
*
* @param stats puntero a la estructura con las estadísticas del algoritmo.
*/
void imprimir_estadisticas_backtraking(EstadisticasBacktraking* stats){
    const char *UNDERLINE = "\x1b[4m"; /* Código ANSI para el subrayado */
    const char *RESET = "\x1b[0m"; /* Código ANSI para reiniciar el formato */

    /* Imprimimos cabecera de la tabla */
    printf("%s%-139s%s\n", UNDERLINE, " ", RESET);
    printf("%s| %-20s | %-20s | %-20s | %-20s | %-20s | %-20s |%s\n",
           UNDERLINE, 
           "Nodos Generados", "Pasos Generar", "Pasos Criterio", "Pasos Solución", "Pasos Más Hermanos", "Pasos Retroceder",
           RESET);

    /* Imprimimos la fila de los datos */
    printf("%s| %-20d | %-20d | %-20d | %-19d | %-19d | %-20d |%s\n",
           UNDERLINE,
           stats->NumNodos, stats->PasosGenerar, stats->PasosCriterio, stats->PasosSolucion, stats->PasosMasHermanos, stats->PasosRetroceder,
           RESET);
}