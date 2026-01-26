#include "lista.h"
#include <stdio.h>
#include <stdlib.h>
/* Este archivo tiene como objetivo alternar entre la ejecución del "main" (Ejecutable correspondiente
a la compilación de main.c y todos los archivos .c auxiliares para realizar todas las funciones presentes
en el menú), al ejecutarse con Make su ejecución será correcta permitiendo ejecutar "main -DN=3" llamado ./matriz3x3
o "main -DN=6" llamado matriz6x6. Este código podría fallar en los siguientes casos:

1. Si no se compila con Make el programa enteró fallará ya que este es el primer paso
2. Si se compila desde la bash sin asignar flags del compilador N siempre valdrá 3
3. No sé que pasaría si hubiese algún error al compilar un archivo con el Make si generaría también este 
ejecutable (ya que en el main se prestablece que los dos ejecutable matriz3x3 y matriz6x6 son precondiciones
para poder ejecutar "./launcher" (proceso correspondiente a este archivo de código))

>>EN RESUMEN: Aquí se elije el tamaño de la matriz y se ejecuta el programa deseado según ese tamaño,
también sirve para cambiar el tamaño de la matriz en medio de una ejecución (se cambia a ejecutar otro
proceso distinto)*/



int main(){
    int n;
    printf("Introduzca las dimensiones de la Matriz (nxn): ");
    scanf("%d", &n);
    
    while(n != 3 && n != 6){
        printf("❌ Solo se admite tamaño 3x3 y 6x6, pruebe de nuevo: ");
        scanf("%d", &n);
    }
    /* Se ejecuta main compilado con -DN=3*/
    if(n == 3){
        system("./matriz3x3");
    }
    /* Se ejecuta main compilado con -DN=6*/
    if(n == 6){
        system("./matrix6x6");
    }
    return 0;
}