#include <stdlib.h>
#include "abin.h"
//Implementacion TAD PilaOperandos

//CONTENIDO DE CADA ELEMENTO DE LA Pila
//MODIFICAR: PARA LA Pila DE OPERADORES: char
//MODIFICAR: PARA LA Pila DE Operandos: abin
typedef abin tipoelemOperandos;
///////////////////////////////////////////////////////

//Definicion del tipo de datos PilaOperandos
struct tipo_celda {
    tipoelemOperandos elemento;
    struct tipo_celda * sig;
};

typedef struct tipo_celda * PilaOperandos;
/////////////////////////////////////////////////////////

void crearPilaOperandos(PilaOperandos *P) {
    *P = NULL;
}

void destruirPilaOperandos(PilaOperandos *P) {
    PilaOperandos aux;
    aux = *P;
    while (aux != NULL) {
        aux = aux->sig;
        free(*P);
        *P = aux;
    }
}

unsigned esVaciaPilaOperandos(PilaOperandos P) {
    return P == NULL;
}

tipoelemOperandos topeOperandos(PilaOperandos P) {
    if (!esVaciaPilaOperandos(P)) /*si PilaOperandos no vacia*/
        return P->elemento;
}

void pushOperandos(PilaOperandos *P, tipoelemOperandos E) {
    PilaOperandos aux;
    aux = (PilaOperandos) malloc(sizeof (struct tipo_celda));
    aux->elemento = E;
    aux->sig = *P;
    *P = aux;
}

void popOperandos(PilaOperandos *P) {
    PilaOperandos aux;
    if (!esVaciaPilaOperandos(*P)) /*si PilaOperandos no vacia*/ {
        aux = *P;
        *P = (*P)->sig;
        free(aux);
    }
}
