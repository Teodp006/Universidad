#include "lista.h"
#include <stdio.h>
#include <stdlib.h>
#include "backtraking.h"
#include "funciones.h"
#include "ramipoda.h"

int main(){

int B[N][N] = { // En tiempo de compilación (gracias a las flags del compilador o la constante de "lista.h" se inicializa B)
    #if N == 3
            
    /*G*/
    /*A*/   /* Ciudades*/
    /*L*/    {11, 17,  8},
    /*E*/    {9,   7,  6},
    /*O*/    {13, 15, 16}
    /*N*/               //3x3
    /*E*/
    /*S*/
    #elif N == 6
                  /* Ciudades*/ 
    /*G*/    
    /*A*/    {11, 17,  8, 16, 20, 14},
    /*L*/    {9,   7,  6, 12, 15, 18},
    /*E*/    {13, 15, 16, 12, 16, 18},
    /*O*/    {21, 24, 28, 17, 26, 20},
    /*N*/    {10, 14, 12, 11, 15, 13},
    /*E*/    {12, 20, 19, 13, 22, 17}
    /*S*/                           //6x6
    
    #else
    #error "N debe ser 3 o 6" /* Error de compilación si N no es ni 3 o 6 */
    #endif
    };

    /* Imprimimos el contenido de B para ver que es correcto
    printf("Matriz B (%dx%d):\n", N, N);
    for(int i = 0; i < N; i++){
        for(int j = 0; j < N; j++){
            printf("%4d ", B[i][j]);
        }
        printf("\n");
    }*/

    char opt = 's';
        do{
        printf("\n\n ------------------ MENU PRINCIPAL ----------------\n");
        printf("|    Estás utilizando una matriz de tamaño %dx%d    |\n", N, N);
        printf(" --------------------------------------------------\n");
        printf("a) Backtraking sin vector usadas\n");
        printf("b) Backtraking con vector usadas\n");
        printf("c) Ramificación y poda con estimaciones tribiales\n");
        printf("d) Ramifición y poda con estimaciones precisas\n");
        printf("x) Cambiar el tamaño de la matriz\n");
        printf("s) Salir\n");
        printf("Seleccione una opción: ");
        scanf(" %c", &opt);
        switch(opt){
            case 'a':
                printf("\n-----\tBacktraking sin vector\t-----\n\n");
                int solucion[N] = {0}; /*Inicialización vector solución*/
                backtraking(solucion, B, 0); /* Llamada a backtraking*/
                imprimir_solucion(B, solucion); /* Imprimimos la solución óptima encontrada */
                break;
            case 'b':
                printf("\n-----\tBacktraking con vector\t------\n\n");
                int solucion2[N] = {0}; /*Inicialización vector solución*/
                backtraking(solucion2, B, 1); /* Llamada a backtraking*/
                imprimir_solucion(B, solucion2); /* Imprimimos la solución óptima encontrada */
                break;
            case 'c':
                printf("Has seleccionado Ramificación y poda con estimaciones tribiales\n");
                NODO solucion3 = {0}; /*Inicialización vector solución*/
                int numNodos = 0;
                ramificacion_poda(&solucion3, B, 0, &numNodos); /* Llamada a ramificación y poda*/
                imprimir_solucion(B, solucion3.tupla); /* Imprimimos la solución óptima encontrada */
                break;
            case 'd':
                NODO solucion4 = {0}; /*Inicialización vector solución*/
                int numNodos2 = 0;
                ramificacion_poda(&solucion4, B, 1, &numNodos2); /* Llamada a ramificación y poda*/
                imprimir_solucion(B, solucion4.tupla); /* Imprimimos la solución óptima encontrada */
                break;
            case 'x':
                printf("\n\n----- CAMBIANDO TAMAÑO -----\n\n");
                system("./launcher"); /* Cambiamos al launcher (programa donde se elije el tamaño de la matriz) */
                break;
            case 's':
                printf("Saliendo del programa...\n");
                break;
            default:
                printf("Opción no válida\n");
        }
    }while (opt != 's' && opt != 'x'); /* Si es 'x' también se termina este proceso ya que se lanza otro. */
    return 0;
}