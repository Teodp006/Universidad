#include <stdio.h>
#include <stdlib.h>
#include "JdTMAP.h"
#include "grafo.h"
#include "algoritmos.h"
/*
 * Programa que muestra el uso del TAD grafo de números enteros
 */

int main(int argc, char** argv) {
    //Grafo de números enteros
    grafo G; //grafo
    char opcion;

    //Creo el grafo
    crear_grafo(&G);
    leer_archivo(&G, argv, argc);
    do {
        printf("\n\na. Insertar nueva ciudad\n");
        printf("b. Eliminar ciudad\n");
        printf("c. Crear ruta entre dos ciudades\n");
        printf("d. Eliminar ruta\n");
        printf("e. Calcular ruta más corta entre dos ciudades\n");
        printf("f. Calcular ruta más rápida entre dos ciudades\n");
        printf("h. Imprimir la infraestructura viaria de tiempo minimo\n");
        printf("i. Imprimir mapa\n");
        printf("s. Salir\n");

        printf("Opcion: ");
        scanf(" %c", &opcion); 

        switch (opcion) {
            case 'a':case'A':
                agregar_ciudad(&G);
                break;
            case 'b':case 'B':
                eliminar_ciudad(&G);
                break;
            case 'c': case 'C':
                nueva_ruta(&G);
                break;
            case 'd': case 'D':
                eliminar_ruta(&G);
                break;
            case 'i': case 'I':
                imprimir_mapa(G);
                break;
            case 'e': case 'E':
                distancia_minima(G);
                break;
            case 'f': case 'F':
                tiempo_minimo(G);
                break;
            case 'h': case 'H':
                Prim(G,  velocidad_tierra_caballo, velocidad_mar_caballo); // Velocidad en tierra 5.5 km/h y en mar 11.25 km/h (por defecto caballo)
                break;
            case 's': case 'S':
                break;
            default:
                printf("Opción equivocada\n");
        }
    } while (opcion != 's');
    guardar_archivo(G, argv, argc);
    borrar_grafo(&G);
    return (EXIT_SUCCESS);
}

