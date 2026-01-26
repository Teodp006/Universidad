#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "pilaoperandos.h"
#include "pilaoperadores.h"
#include "abin.h"

//Devuelve la prioridad del operador dentro de la pila.
//Prioridad('(')=0
int prioridadDentro(char op) {
    int prior;
    switch (op) {
        case '^': prior = 3;
            break;
        case '*': case '/': prior = 2;
            break;
        case '+': case '-': prior = 1;
            break;
        case '(': prior = 0; //nunca va a entrar en la pila, provoca vaciado
            break;
    }
    return prior;
}

//Devuelve la prioridad del operador fuera de la pila.
//Prioridad('(')=4
int prioridadFuera(char op) {
    int prior;
    switch (op) {
        case '^': prior = 3;
            break;
        case '*': case '/': prior = 2;
            break;
        case '+': case '-': prior = 1;
            break;
        case '(': prior = 4; //para que SIEMPRE entre en la pila
            break;
    }
    return prior;
}

//Devuelve 1 si c es un operador: +, -, /, *, ^, ()
unsigned esOperador(char c) {
    unsigned ope;
    switch (c) {
        case '^': case '*': case '/': case '+': case '-': case '(':
            ope = 1;
            break;
        default:
            ope = 0;
    }
    return ope;
}

//Devuelve 1 si c es un operando: mayúsculas y minúsculas
//completar para caracteres 0 a 9
unsigned esOperando(char c) {
    if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122) || (c >= 48 && c <= 57))
        return 1;
    else
        return 0;
}

void construirSubarbol(PilaOperandos *pilaArbol, PilaOperadores *pilaAux);
//Recibe una cadena de caracteres y devuelve el arbol de expresion
//DEBES ESCRIBIR ESTA FUNCIÓN
abin arbolExpresion(char *expr_infija) {
    abin Arbol;
    PilaOperandos pilaArbol;
    PilaOperadores pilaAux;
    int i = 0;
    char op;

    crearPilaOperandos(&pilaArbol);
    crearPilaOperadores(&pilaAux);

    // Aquí va la lógica para construir el árbol de expresión

    for(i = 0; i<strlen(expr_infija); i++){
        if (esOperando(expr_infija[i])){ /* Crear un nodo de árbol para el operando y apilarlo */
            abin nuevo;
            /* Al crear un árbol ya se le asocia una referencia nueva automáticamente*/
            crear(&nuevo);
            insizq(&nuevo, (tipoelem)expr_infija[i]);
            pushOperandos(&pilaArbol, nuevo);
        }
        else if (esOperador(expr_infija[i])){ /* Manejar el operador según su prioridad */
            /*Verificamos que no haya otros operadores con mayor prioridad primero*/
            if(prioridadFuera(expr_infija[i]) <= prioridadDentro(topeOperadores(pilaAux)) && expr_infija[i] != '('){
                while(prioridadFuera(expr_infija[i]) <= prioridadDentro(topeOperadores(pilaAux)) && !esVaciaPilaOperadores(pilaAux)){
                    /*Construímos un subárbol con los últimos */
                    construirSubarbol(&pilaArbol, &pilaAux);
                }
            }
            /*Insertamos el nuevo operador a la pila*/
            pushOperadores(&pilaAux, expr_infija[i]);
            
        }
        else if (expr_infija[i] == ')') {
            while(topeOperadores(pilaAux) != '(' && !esVaciaPilaOperadores(pilaAux)) {
                /*Construímos un subárbol con los últimos */
                construirSubarbol(&pilaArbol, &pilaAux);
            }
            popOperadores(&pilaAux); /*Sacar el '(' de la pila*/
        }
        else{
            printf("Error: No se reconoció el caracter '%c'\n", expr_infija[i]);
            return NULL; // Manejo de error
        }
    }
    /*Si queda algún operador en la pila de Operadores, se utilizan para construir el árbol final*/
    while(!esVaciaPilaOperadores(pilaAux)){
        construirSubarbol(&pilaArbol, &pilaAux);
    }
    Arbol = topeOperandos(pilaArbol); /* Obtenemos el árbol final, producto de todas las operaciones */
    destruirPilaOperadores(&pilaAux);
    return Arbol; // Cambiar por el árbol construido
}

/*
* Función que crea un nuevo árbol a partir del último operador
* de la pila de operadores y los últimos dos operandos de la pila de operandos,
* y lo inserta en el tope de la pila de operandos
* @param *pilaArbol puntero a la pila que contiene los operandos
* @param *pilaAux puntero a la pila que contiene los operadores
*/
void construirSubarbol(PilaOperandos *pilaArbol, PilaOperadores *pilaAux) {
    // Desapilar de pilaAux los operadores y de pilaArbol los operandos necesarios para construir un árbol
    abin nuevo, der, izq;
    char op;
    crear(&nuevo);
    /*Colocamos el último operador como raíz del nuevo árbol*/
    op = topeOperadores(*pilaAux);
    insizq(&nuevo, op);
    popOperadores(pilaAux);
    /*Insertamos como rama derecha del operador el último Árbol de PilaOOperandos*/
    der = topeOperandos(*pilaArbol);
    /*Insertamos como rama derecha del operador*/
    insArbolder(&nuevo, der);
    /*Lo Desapilamos de la Pila de Operandos*/
    popOperandos(pilaArbol);
    /*Mismo procedimiento con el segundo operando*/
    izq = topeOperandos(*pilaArbol);
    /*En este caso va a la izquierda*/
    insArbolizq(&nuevo, izq);
    popOperandos(pilaArbol);
    /*Guardamos el nuevo árbol en la pila de Operandos y lo destruimos*/
    pushOperandos(pilaArbol, nuevo);
}