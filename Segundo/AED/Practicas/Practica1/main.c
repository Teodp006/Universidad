/* Practica 1: Prueba de manejo de Arboles Binarios  */
#include <stdlib.h>
#include <stdio.h>

#include "abin.h"
#include "recorridos.h"
#include "funcionesArboles.h"

int main(int argc, char** argv) {

    abin Arbol, aux; tipoelem letra;

    //Creo el Arbol
    crear(&Arbol);
    printf("Árbol creado\n");

    //Nodo raíz
    insizq(&Arbol, 'P'); 
    // Hijos de P
    insder(&Arbol, 'R'); 
    insizq(&Arbol, 'M');
    aux = der(Arbol); // Accedemos al nodo R
    insder(&aux, 'S');
    insizq(&aux, 'Q');
    aux = der(aux); // Accedemos al nodo S
    insder(&aux, 'X');
    aux = izq(Arbol); // Accedemos al nodo M
    insizq(&aux, 'D');
    insder(&aux, 'N');
    aux = izq(aux); // Accedemos al nodo D
    insizq(&aux, 'B');
    insder(&aux, 'F');

    printf("Altura del árbol: %d\n", AlturaArbol(Arbol));

    printf("\nRecorrido en anchura (usa colas):\n"); anchura(Arbol);

    //Accedemos al nodo R y suprimimos su subárbol derecho

    /* CODIGO APARTADO 1
    *aux = der(Arbol);
    supder(&aux);*/

    /* CODIGO APARTADO 2*/
    printf("Introduzca el nodo que quieres buscar para eliminar su derecha: ");
    scanf(" %c", &letra);
    aux = NULL;
    buscar(Arbol, letra, &aux);
    if(aux==NULL){
        printf("No se ha encontrado el nodo %c\n", letra);
    }
    else{
        supder(&aux);
        printf("Arbol derecho de %c eliminado\n", letra);
    }

    printf("Recorrido postorden (IDR):\n"); postorden(Arbol);
    // Utilizamos las funciones para calcular la altura y el número de nodos del árbol
    printf("\nAltura de arbol: %d", AlturaArbol(Arbol));
    printf("\nNúmero de nodos: %d\n", NumeroNodos(Arbol));

    // Liberamos memoria asociada al arbol:
    destruir(&Arbol);

    return (EXIT_SUCCESS);
}
