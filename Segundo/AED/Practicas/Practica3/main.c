#include <stdio.h>
#include <stdlib.h>
#include "lista.h"
#include "abb.h"
#include "juegodetronos.h"

int main(int argc, char *argv[]){
    char opt; TABB ArbolPersonajes;

    crearAbb(&ArbolPersonajes);

    cargar_archivo(&ArbolPersonajes, argc, argv);

    do{
        printf("Menu:\n");
        printf("A. Agregar personaje\n");
        printf("L. Listar personajes\n");
        printf("E. Eliminar personaje\n");
        printf("B. Buscar personaje por nombre\n");
        printf("T. Listar personajes por tipo\n");
        printf("M. Modificar personaje\n");
        printf("S. Salir\n");
        printf("Seleccione una opcion: ");
        scanf(" %c", &opt);

        switch(opt){
            case 'A': case 'a':
                AnhadirPersonaje(&ArbolPersonajes);
                break;
            case 'L': case 'l':
                ListarPersonajes(ArbolPersonajes);
                break;
            case 'E': case 'e':
                EliminarPersonaje(&ArbolPersonajes);
                break;
            case 'B': case 'b':
                BuscarPersonaje(ArbolPersonajes);
                break;
            case 'T': case 't':
                ListarTipoPersonaje(ArbolPersonajes);
                break;
            case 'M': case 'm':
                ModificarPersonaje(&ArbolPersonajes);
                break;
            case 'S': case 's':
                printf("Saliendo...\n");
                break;
            default:
                printf("Opcion invalida. Intente de nuevo.\n");
        }
    } while(opt != 'S' && opt != 's');
    guardar_archivo(&ArbolPersonajes, argc, argv);
    destruirAbb(&ArbolPersonajes);
    return 0;
}

