#include <stdio.h>
#include "lista.h" /* Para la constante N */
#include "funciones.h"

const char* GALEONES[] = {"Iron Victory", "Grief", "Lord Quellon", "Lamentation", "Kite", "Dagger"};
const char* CIUDADES[] = {"King's Landing", "Lannisport", "Oldtown", "White Harbor", "Seagard", "Storm’s End"};

/* Función para imprimir una solución encontrada [soa] y su beneficio asociado para un problema de asignación
* de galeones (filas) a ciudades (columnas) representado por la matriz de beneficios [B].
*
* @param B[][] Matriz de beneficios donde B[i][j] es el beneficio de asignar el galeón i a la ciudad j
* @param soa[] Vector con la solución óptima encontrada s[i] = j, donde
*/
void imprimir_solucion(int B[N][N], int soa[N]){
    int voa = 0;
    printf("Ataque realizado:\n");
    for(int i = 0; i < N; i++){
        printf("🚢 Galeón %s ---> 🏰 Ciudad %s\n", GALEONES[i], CIUDADES[soa[i]]);
        voa += B[i][soa[i]];
    }
    printf("Botín total saqueado 💰: %d\n", voa);
}
