#include "grafo.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Colores para pintar las conexiones por su tipo

/////////////////////////////////////////////////////////// TIPOS DE DATOS

// Estructura privada
struct tipografo {
    int N; //número de vértices del grafo
    tipovertice VERTICES[MAXVERTICES]; //vector de vértices
    tipoarco A[MAXVERTICES][MAXVERTICES]; //matriz de adyacencia
};

//////////////////////////////////////////////////////////////// FUNCIONES

//HAY QUE MODIFICAR ESTA FUNCIÓN SI SE CAMBIA EL TIPO DE DATO tipovertice
/* 
 * Esta función devuelve 0 si los dos nodos son iguales
 * y 1 en otro caso.
 */
int _comparar_vertices(tipovertice V1, tipovertice V2){
	return strcmp(V1.nombre, V2.nombre) == 0 ? 0 : 1;
}

//Creación del grafo con 0 nodos
void crear_grafo(grafo *G) {
    *G = (struct tipografo*) malloc(sizeof (struct tipografo));
    (*G)->N = 0;
}

//Devuelve la posición del vértice Vert en el vector VERTICES del grafo G
//Si devuelve -1 es porque no encontró el vértice
int posicion(grafo G, tipovertice V) {
    int contador = 0;
    //comparo V con todos los vertices almacenados en VERTICES 
    while (contador < G->N) {
        //if (G->VERTICES[contador]==V)  //encontré la posicion de V
		if (_comparar_vertices(G->VERTICES[contador], V) == 0){
            return contador; 
        }
        contador++;
    }
    return -1;
}

//Devuelve 1 si el grafo G existe y 0 en caso contrario
int existe(grafo G) {
    return (G != NULL);
}

//Devuelve 1 si el vértice Vert existe en el grafo G
int existe_vertice(grafo G, tipovertice V) {
    return (posicion(G, V) >= 0);
}

//Inserta un vértice en el grafo, devuelve -1 si no ha podido insertarlo por estar el grafo lleno
int insertar_vertice(grafo *G, tipovertice Vert) {
    int i;
    if ((*G)->N == MAXVERTICES) {
    	// Se ha llegado al maximo numero de vertices
    	return -1;
    }
   
    (*G)->N++;
    (*G)->VERTICES[((*G)->N) - 1] = Vert;
    for (i = 0; i < (*G)->N; i++) {
        (*G)->A[(*G)->N - 1][i] = NULL;
        (*G)->A[i][(*G)->N - 1] = NULL;
    }
	return (*G)->N-1;
}

//Borra un vertice del grafo
void borrar_vertice(grafo *G, tipovertice Vert) {
    int F, C, P, N = (*G)->N;
    P = posicion(*G, Vert);
    if(P == -1){
    	return;
    }
    for (F = P; F < N - 1; F++){
        (*G)->VERTICES[F] = (*G)->VERTICES[F + 1];
	}
    // Liberamos memoria de los arcos de la columna del vertice (como el puntero
    // de la fila y columna apuntan a la misma direccion, con liberar una es suficiente)
    // Eso si, debemos colocar los dos a NULL para marcar que ya no existen eses arcos
    for(F = 0; F < N; F++){
        free((*G)->A[F][P]);
        (*G)->A[F][P] = NULL; // Cónexion vértice iésimo con vértice eliminado en posición P
        (*G)->A[P][F] = NULL; // Cónexion vértice eliminado en posición P con vértice iésimo
    }
    for (C = P; C < N - 1; C++){
        for (F = 0; F < N; F++){
            (*G)->A[F][C] = (*G)->A[F][C + 1];
        }
	}
    for (F = P; F < N - 1; F++){
        for (C = 0; C < N; C++){
            (*G)->A[F][C] = (*G)->A[F + 1][C];
        }
	}
    (*G)->N--;    
}

//Crea el arco de relación entre VERTICES(pos1) y VERTICES(pos2)
void crear_arco(grafo *G, int pos1, int pos2, float distancia, tipoCon tipo) {
    if((*G)->A[pos1][pos2] == NULL && (*G)->A[pos2][pos1] == NULL){ // Si no existía arco, hay que reservar memoria, si existía solo se remplaza la información
        (*G)->A[pos1][pos2] = (tipoarco)malloc(sizeof(struct TELEMENTOARCO));
    }
    (*G)->A[pos1][pos2]->distancia = distancia;
    (*G)->A[pos1][pos2]->tipo = tipo;
    (*G)->A[pos2][pos1] = (*G)->A[pos1][pos2]; // Como es un grafo no dirigido pos1-pos2 es igual a pos2-pos1, simplemente apuntan al mismo struct
}

//Borra el arco de relación entre VERTICES(pos1) y VERTICES(pos2)
void borrar_arco(grafo *G, int pos1, int pos2) {
    if ((*G)->A[pos1][pos2] != NULL) { // Si existía el arco, se libera la memoria dinámica de pos1-pos2, ya que al ser no dirigido la matriz es simétrica y si no haremos un doble free
        free((*G)->A[pos1][pos2]);
        (*G)->A[pos1][pos2] = NULL; // Cónexion vértice pos1 con vértice pos2 eliminada
        (*G)->A[pos2][pos1] = NULL; // Cónexion vértice pos2 con vértice pos1 eliminada
    }
}

//Devuelve el puntero a la estructura con los datos del arco si VERTICES(pos1) y VERTICES(pos2) son vértices adyacentes, si no NULL
tipoarco son_adyacentes(grafo G, int pos1, int pos2) {
    return (G->A[pos1][pos2] != NULL) ? G->A[pos1][pos2] : NULL;
}

// Devuelve la longitud del arco entre VERTICES(pos1) y VERTICES(pos2), si no existe arco devuelve INFINITY
float longitud(grafo G, int pos1, int pos2) {
    tipoarco arco = son_adyacentes(G, pos1, pos2);
    return (arco != NULL) ? arco->distancia : INFINITY;
}

// Devuelve el tipo de conexión del arco entre VERTICES(pos1) y VERTICES(pos2), si no existe arco devuelve -1
tipoCon tipo_conexion(grafo G, int pos1, int pos2) {
    tipoarco arco = son_adyacentes(G, pos1, pos2);
    return (arco != NULL) ? arco->tipo : -1;
}


//Destruye el grafo (Liberamos antes la memoria de la matriz)
void borrar_grafo(grafo *G) {
    for(int i = 0; i < (*G)->N; i++){
        for(int j = i; j < (*G)->N; j++){
            borrar_arco(G, i, j);
        }
    }
    free(*G);
    *G = NULL;
}

//Devuelve el número de vértices del grafo G
int num_vertices(grafo G) {
    return G->N;
}

//Devuelve el vector de vértices VERTICES del grafo G
tipovertice* array_vertices(grafo G) {
    return G->VERTICES;
}

