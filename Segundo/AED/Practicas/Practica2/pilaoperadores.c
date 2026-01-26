#include <stdlib.h>

//Implementacion TAD PilaOperadores

//CONTENIDO DE CADA ELEMENTO DE LA Pila
//MODIFICAR: PARA LA Pila DE Operadores: char
//MODIFICAR: PARA LA Pila DE OPERANDOS: abin
typedef char tipoelemOperadores;
///////////////////////////////////////////////////////

//Definicion del tipo de datos PilaOperadores
struct tipo_celda {
    tipoelemOperadores elemento;
    struct tipo_celda * sig;
};

typedef struct tipo_celda * PilaOperadores;
/////////////////////////////////////////////////////////

void crearPilaOperadores(PilaOperadores *P) {
    *P = NULL;
}

void destruirPilaOperadores(PilaOperadores *P) {
    PilaOperadores aux;
    aux = *P;
    while (aux != NULL) {
        aux = aux->sig;
        free(*P);
        *P = aux;
    }
}

unsigned esVaciaPilaOperadores(PilaOperadores P) {
    return P == NULL;
}

tipoelemOperadores topeOperadores(PilaOperadores P) {
    if (!esVaciaPilaOperadores(P)) /*si PilaOperadores no vacia*/
        return P->elemento;
}

void pushOperadores(PilaOperadores *P, tipoelemOperadores E) {
    PilaOperadores aux;
    aux = (PilaOperadores) malloc(sizeof (struct tipo_celda));
    aux->elemento = E;
    aux->sig = *P;
    *P = aux;
}

void popOperadores(PilaOperadores *P) {
    PilaOperadores aux;
    if (!esVaciaPilaOperadores(*P)) /*si PilaOperadores no vacia*/ {
        aux = *P;
        *P = (*P)->sig;
        free(aux);
    }
}
