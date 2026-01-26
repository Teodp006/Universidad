# include "grafo.h"
# include <stdio.h>
# include <string.h>

/* Función que implementa el algoritmo de Floyd_Warshall para una Matriz de Valores D y una de Predecesores P ya inicializadas
* @param D: matriz de valores entre nodos (Distancias, velocidades, costes...)
* @param P: matriz de predecesores entre nodos
*/
void Floyd_Warshall(int N, float V[N][N], int P[N][N]){
    // Índices para recorrer las matrices
    int i,j,k;
    // Algoritmo de Floyd-Warshall
    for(k = 0; k<N; k++){
        for(i = 0; i<N; i++){
            for(j = 0; j<N; j++){
                if(V[i][k] + V[k][j] < V[i][j]){
                    V[i][j] = V[i][k] + V[k][j];
                    P[i][j] = P[k][j];
                }
            }
        }
    }
}

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
void inicializar_matrizPonderada(int N, float D[N][N], int P[N][N], grafo G, float velocidad_tierra, float velocidad_mar){
    int i,j;
    for(i = 0; i<N; i++){
        for(j = 0; j<N; j++){
            if(i == j){ // Un nodo consigo mismo por defecto tiene valor 0 para la distancia y el predecesor.
                D[i][j] = 0;
                P[i][j] = 0;
            }
            switch (tipo_conexion(G, i, j)) {
                case tierra:
                    D[i][j] = longitud(G, i, j) / velocidad_tierra; // Si velocidad = 1, se guarda la distancia, si no el tiempo a dicha velocidad
                    P[i][j] = i + 1; // Guardamos los vértices desde 1 hasta N para no confundir el 0 con "sin predecesor"
                    break;
                case mar:
                    D[i][j] = longitud(G, i, j) / velocidad_mar; // Si velocidad = 1, se guarda la distancia, si no el tiempo a dicha velocidad
                    P[i][j] = i + 1; // Guardamos los vértices desde 1 hasta N para no confundir el 0 con "sin predecesor"
                    break;
                default: // Si no hay un camino de longitud 1 entre i y j, inicializamos a distancia infinita y sin predecesor
                    D[i][j] = INFINITY;
                    P[i][j] = 0;
                    break;
            }
        }
    }
}
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
void imprimir_camino(int origen, int destino, int N, int P[N][N], float D[N][N], grafo G, const char *unidad){
    if(origen < 0 || origen > N - 1 || destino < 0 || destino > N - 1){
        printf("Error: índices de origen o destino fuera de rango\n");
        return;
    }
    int intermedio = P[origen][destino]-1; // Restamos 1 porque la matriz P está inicializada con posición + 1
    if(P[origen][destino] == 0){
        printf("No hay camino de %d a %d\n", origen, destino);
    }
    else if(intermedio == origen){ // Caso base, ya llegamos al comienzo de la ruta, imprimimos origen --> destino
        tipovertice *VECTOR = array_vertices(G);
        if(tipo_conexion(G, origen, destino) == tierra){
            printf("%s %s--[%.2f %s]-->%s %s", VECTOR[origen].nombre, marron ,D[origen][destino], unidad, reset, VECTOR[destino].nombre);
        }
        else{
            printf("%s %s~~[%.2f %s]~~>%s %s", VECTOR[origen].nombre, azul ,D[origen][destino], unidad, reset, VECTOR[destino].nombre);
        }
    }
    else{ // Caso recursivo, imprimimos --> destino desde el intermedio
        imprimir_camino(origen, intermedio, N, P, D, G, unidad); // Repetimos la función para el tramo origen --> intermedio
        tipovertice *VECTOR = array_vertices(G);
        if(tipo_conexion(G, P[origen][destino]-1, destino) == tierra){
            printf(" %s--[%.2f %s]-->%s %s", marron, D[intermedio][destino], unidad, reset,VECTOR[destino].nombre);
        }
        else{
            printf(" %s~~[%.2f %s]~~>%s %s", azul ,D[intermedio][destino], unidad, reset, VECTOR[destino].nombre);
        }
    }
    
}

/* Función para aplicar el algoritmo del cálculo del árbol de expresión de tiempo o distancia mínimo de un grafo
*  ponderado en distancias. Para distancias, usar velocidad_tierra = 1 y velocidad_mar = 1.
*
* @param G: grafo sobre el que se aplicará el algoritmo de Prim
* @param velocidad_tierra: velocidad de viaje en tierra (km/h)
* @param velocidad_mar: velocidad de viaje en mar (km/h)
*/
void Prim(grafo G, float velocidad_tierra, float velocidad_mar){
    int N = num_vertices(G);
    int sin_visitar[N]; // Vector de nodos sin visitar
    int visitado[N]; // Empezamos con un vector "sin elementos" (inicializado como -1)
    int num_visitados = 0, i, j;
    float tiempo_minimo = INFINITO; // Menor tiempo en un conjunto de iteraciones
    float tiempo_actual = INFINITO; // Tiempo conseguido en una iteración determinada
    float tiempo_total = 0;
    int arcos[2][N-1]; // Almacenará los arcos del árbol de expansión mínima, para N vértices N-1 arcos
    
    // Inicializamos el array de sin visitar, visitado y arcos
    for(i = 0; i < N; i++){
        sin_visitar[i] = i; // Cada nodo en esta lista está representado por su índice (Si no se ha visitado)
        visitado[i] = -1; // Inicializamos el array de visitados a -1 (Ningún nodo ha sido visitado)
        arcos[0][i] = -1; // Inicializamos los arcos a -1
        arcos[1][i] = -1; // Inicializamos los arcos a -1
    }
    
    visitado[0] = 0; // Marcamos el primer nodo como visitado
    sin_visitar[0] = -1; // Lo eliminamos de la lista de sin visitar

    for(num_visitados = 1; num_visitados < num_vertices(G); num_visitados++){
        tiempo_minimo = INFINITO;
        tiempo_actual = INFINITO;
        for(i = 0; i < num_visitados; i++){ // Para todos los vértices ya seleccionados 
            for(j = 0; j < num_vertices(G); j++){ // Miramos todos los que quedan sin seleccionar
                if(sin_visitar[j] != -1){  // Si el vertice ya fue visitado, solo crearemos un ciclo, eso no lo queremos
                    if(tipo_conexion(G, visitado[i], sin_visitar[j]) != -1){ // Solo haremos comprobaciones si existe un arco entre el iésimo visitado y el jésimo sin visitar
                        switch (tipo_conexion(G, visitado[i], sin_visitar[j])) { // Calculamos el tiempo según la velocidad, ya que eso puede ser diferente de la distancia mínima
                            case tierra: tiempo_actual = longitud(G, visitado[i], sin_visitar[j]) / velocidad_tierra;
                                break;
                            case mar: tiempo_actual = longitud(G, visitado[i], sin_visitar[j]) / velocidad_mar;
                                break;
                        }
                        if(tiempo_actual < tiempo_minimo){ // Guardamos el arco v1,v2 por ejemplo: 0,3
                            tiempo_minimo = tiempo_actual; // Almacenamos el tiempo mínimo, de tal forma que la última iteración de esta línea va a ser el tiempo más pequeño posible
                            arcos[0][num_visitados - 1] = visitado[i]; // -1 porque num_visitados empieza en 1 y perderíamos un hueco del array
                            arcos[1][num_visitados - 1] = sin_visitar[j];
                        }
                    } 
                }
            }
        }
        // Aquí ya habremos encontrado el arco de tiempo mínima para nuestro subárbol de tamaño num_visitados
        // Vamos a imprimirlo y a sumar el tiempo total.
        printf("%20s %s%s%s %-20s :    %s%-.2f horas%s\n", array_vertices(G)[arcos[0][num_visitados - 1]].nombre,(tipo_conexion(G,arcos[0][num_visitados - 1], arcos[1][num_visitados - 1]) == tierra)? marron: azul, (tipo_conexion(G,arcos[0][num_visitados - 1], arcos[1][num_visitados - 1]) == tierra)?"--":"~~",reset,array_vertices(G)[arcos[1][num_visitados - 1]].nombre, (tipo_conexion(G,arcos[0][num_visitados - 1], arcos[1][num_visitados - 1]) == tierra)? marron: azul, tiempo_minimo, reset);
        // Actualizamos los arrays de visitados y sin visitar
        visitado[num_visitados] = arcos[1][num_visitados - 1];
        sin_visitar[arcos[1][num_visitados - 1]] = -1; // Marcamos el segundo elemento del arco como visitado
        // Sumamos el tiempo total de la infraestructura
        tiempo_total += tiempo_minimo;
    }
    printf("\nTiempo total de la infraestructura viaria mínima: %.2f horas\n", tiempo_total);
}