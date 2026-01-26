#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "abin.h"
#include "FuncionesExpresion.h"
#include "recorridos.h"
#include "pilaoperandos.h"
#include "pilaoperadores.h"


int main(int argc, char *argv[]) {
	/*Guardamos en esta variable la expresión literal*/
	char *expresion;
	/*Árbol donde se almacenará la expresión*/
	abin MiArbol = 0;
	if(argc == 2) { /*Si se introduce la expresión por línea de comando*/
		/* Convertir la expresión infija a un árbol de expresión */
		MiArbol = arbolExpresion(argv[1]);
		inorden(MiArbol);
		printf("\n");
	}
	else{ /*Si no se introduce, se le pregunta al usuario*/
		printf("Introduzca una operación aritmética:\n");
		printf("Ejemplo: 7*(X+2)-3/4\n");
		/* Leer la expresión del usuario*/
		scanf("%s", expresion);
		MiArbol = arbolExpresion(expresion);
		inorden(MiArbol);
		printf("\n");	
	}
	destruir(&MiArbol);
	return 0;
}