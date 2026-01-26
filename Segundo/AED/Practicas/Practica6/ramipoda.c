#include "lista.h"
#include "ramipoda.h"
#include <stdio.h>

/* Función que permite calcular el maximo beneficio de la matriz para establecer cotas triviales al comienzo
* del algoritmo de ramificación y poda.
*
* @param B[][] Matriz de beneficios donde B[i][j] es el beneficio de asignar el galeón i a la ciudad j 
* @return Entero correspondiente al máximo beneficio encontrado en la matriz B
*/
int _max_beneficio_matriz(int B[N][N]){
    if(N > 0 && B != NULL){
        int max = B[0][0];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                if(B[i][j] > max){
                    max = B[i][j];
                }
            }
        }
        return max;
    }
    return -1; /* En caso de error devolvemos -1 */
}


/* Función que indica si un nodo es una solución completa del problema, esta función no comprueba
* que la asignación de galeones a ciudades sea sin repetición ya que eso se hace en la función de Backrtraking
* 
* @param x nodo a comprobar si es solución
* @return 1 si el nodo es solución, 0 en otro caso
*/
int _solucion(NODO x){
    return x.nivel == N;
}


/* Función que selecciona el nodo de la lista siguiendo la estategia MBLIFO y
* actualiza la lista de nodos vivos (LNV) eliminando el nodo seleccionado.
*
* @param LNV puntero a la lista de nodos vivos.
* @return estructura del último nodo más prometedor (mayor BE).
*/
NODO _seleccionar(TLISTA *LNV){
    NODO nSelect; /* Nodo seleccionado temporalmente*/
    NODO aux; /* Nodo auxiliar para recorrer la lista */
    TPOSICION auxPos = primeroLista(*LNV); /* Posición auxiliar para recorrer la lista */
    TPOSICION pSelect = auxPos; /* Posición del nodo seleccionado en la lista */
    if(!esListaVacia(*LNV)){
        recuperarElementoLista(*LNV, auxPos, &nSelect); 
        auxPos = siguienteLista(*LNV, auxPos); 
        while(finLista(*LNV) != auxPos){ /* Miramos todos los nodos vivos */
            recuperarElementoLista(*LNV, auxPos, &aux); 
            if(aux.BE > nSelect.BE){ /* Nos quedamos con el primero que tenga mayor BE*/
                nSelect = aux;
                pSelect = auxPos;
            }
            auxPos = siguienteLista(*LNV, auxPos); 
        }
    }
    /* Eliminamos el nodo seleccionado de la lista */
    suprimirElementoLista(LNV, pSelect);
    return nSelect;
}

/* Actualiza la cota superior trivial de un nodo x
*
* @param x puntero al nodo a actualizar
* @param max_beneficio máximo beneficio de la matriz B[][]
*/
void _CS_trivial(NODO *x, int max_beneficio){
    x->CS = x->bact + (N - x->nivel) * max_beneficio;
}


/* Actualiza la cota inferior trivial de un nodo x
*
* @param x puntero al nodo a actualizar
* @param max_beneficio máximo beneficio de la matriz B[][]
*/
void _CI_trivial(NODO *x){
    x->CI = x->bact;
}


/* Actualiza la cota superior precisa de un nodo como la solucion voraz con repetición a partir del nodo x
*
* @param x puntero al nodo a actualizar
* @param B[][] Matriz de beneficios donde B[i][j] es el beneficio de asign
*/
void _CS_precisa(NODO *x, int B[N][N]){
    int bacum = x->bact;
    for(int i = x->nivel; i< N; i++){
        int max = B[i][0]; /* Máximo de una fila de la matriz (Posibles asignaciones con ciudades del galeón i)*/
        for(int j = 0; j < N; j++){
            if(!x->usadas[j] && B[i][j] > max){
                max = B[i][j];
            }
        }
        bacum += max;
    }
    x->CS = bacum;
}

/* Actualiza la cota superior precisa de un nodo como la solucion voraz sin repetición a partir del nodo x
*
* @param x puntero al nodo a actualizar
* @param B[][] Matriz de beneficios donde B[i][j] es el beneficio de asign
*/
void _CI_precisa(NODO *x, int B[N][N]){
    int bacum = x->bact;
    /* Inicialización del vector usadas (hacemos una copia para no modificarlo)*/
    int usadas_local[N] = {0};
    for(int i = 0; i < x->nivel; i++){
        usadas_local[x->tupla[i]] = 1; /* tupla[i] contiene el número de ciudad asignada al galeón i, por tanto esa ciudad está utilizada*/
    }
    for(int i = x->nivel; i< N; i++){
        int max = 0; /* Máximo de una fila de la matriz (Posibles asignaciones con ciudades del galeón i)*/
        int idx_max = 0; /* índice auxiliar */
        for(int j = 0; j < N; j++){
            if(!usadas_local[j] && B[i][j] > max){ /* Solo consideramos las ciudades no usadas previamente*/
                max = B[i][j];
                idx_max = j;
            }
        }
        bacum += max;
        usadas_local[idx_max] = 1; /* Marcamos esta ciudad como usada */
    }
    x->CI = bacum;
}




/* Actualiza el beneficio estimado de un nodo x tanto para estimaciones
* triviales como precisas, requiere ser ejecutada con CS y CI inicializadas
*
* @param x puntero al nodo a actualizar
*/
void _BE(NODO *x){
    x->BE = ( x->CS + x->CI )/ 2;
}

/* Función equivalente a CI_precisa pero que devuelve el nodo fruto de haber realizado la asignación voraz con sus
* campos tupla, usadas y bact actualizados.
*
* @param x nodo desde el cual realizar la asignación voraz
* @param B[][] Matriz de beneficios donde B[i][j] es el beneficio de asign
* @return nodo resultado de la asignación voraz sin repetición
*/
NODO _SolAsignacionVoraz(NODO x, int B[N][N], int *numNodos){
    /* Asignación voraz sin repetición */
    for(int i = x.nivel; i< N; i++){
        int max = 0; /* Máximo de una fila de la matriz (Posibles asignaciones con ciudades del galeón i)*/
        int idx_max = 0; /* índice auxiliar */
        for(int j = 0; j < N; j++){
            if(x.usadas[j] == 0 && B[i][j] > max){ /* Solo consideramos las ciudades no usadas previamente*/
                max = B[i][j];
                idx_max = j;
            }
        }
        x.bact += max;
        x.tupla[i] = idx_max; /* Asignamos la ciudad al galeón */
        x.usadas[idx_max] = 1; /* Marcamos esta ciudad como usada */
    }
    /* Mensaje para indicar que se generaron nuevos nodos al ejecutar la solución voraz*/
    for(int i = x.nivel; i < N; i++){
        (*numNodos)++;
        for(int j = x.nivel ; j < i+1 ; j++){
            printf("\t"); /* Imprimimos \t para que parezca forma de árbol*/
        }
        printf("\t|____Nodo\t%d:  Nivel %d ---> Generado por solución voraz\n",*numNodos, i+1);
    }
    return x;
}





/* Optimización por ramificación y poda para el problema de asignación de galeones a ciudades sin repetición
* , maximizando el beneficio total. Imprime también los nodos generados y una información sobre cada nodo generado en
* estructura de "árbol".
* @param solucion[] Vector donde se almacenará la solución óptima encontrada
* @param B[][] Matriz de beneficios donde B[i][j] es el beneficio de asignar el galeón i a la ciudad j
* @param modo discriminante para ejecutar ramificación y poda con estimaciones triviales (0) o precisas (1)
* @param numNodos puntero al contador de nodos generados
*/
void ramificacion_poda(NODO *s, int B[N][N], int modo, int *numNodos){
    TLISTA LNV; /* Lista de Nodos Vivos, soluciones parciales que pueden ser soluciones óptimas todavía*/
    NODO nRaiz; /* Nodo raíz */
    NODO nSelect; /* Nodo seleccionado en la solución parcial*/
    NODO nHijo; /* Nodo hijo del nodo seleccionado*/
    NODO nSolucion; /* Nodo con mejor solución hasta la fecha*/

    float C = 0; /* Cota de poda */
    *numNodos = 0; /* Contador de nodos generados */


    /* Nos aseguramos de que el nodo Solución esté correctamente inicializado*/
    nSolucion.bact = 0;
    if(nSolucion.bact != 0){
        nSolucion.bact = 0;
    }


    /* Inicializamos el beneficio máximo de la matriz */
    int max_beneficio = _max_beneficio_matriz(B);

    /* Inicializamos los datos como en las traspas*/
    nRaiz.n = 1;
    nRaiz.nivel = 0;
    nRaiz.bact = 0;
    for(int i = 0; i < N; i++){
        nRaiz.tupla[i] = -1; /* Ningún galeón ha sido asignado a ninguna ciudad */
        nRaiz.usadas[i] = 0; /* Ninguna ciudad ha sido atacada */
    }
    /* Inicializamos las cotas del nodo raíz */
    if(modo == 0){
        _CI_trivial(&nRaiz);
        _CS_trivial(&nRaiz, max_beneficio);
    }
    else if(modo == 1){
        _CI_precisa(&nRaiz, B);
        _CS_precisa(&nRaiz, B);
    }
    /* Calculamos el beneficio actual y creamos la lista*/
    _BE(&nRaiz);
    crearLista(&LNV);
    (*numNodos)++;

    /* Si la solución de la raíz es voraz ya no hay que hacer nada */
    if(modo == 1){
        if(nRaiz.CS == nRaiz.CI){
            printf("\t\tNodo\t%d: CI=%.2f, BE=%.2f, CS=%.2f ---> Es una solución voraz\n", nRaiz.n, nRaiz.CI, nRaiz.BE, nRaiz.CS);
            nSolucion = _SolAsignacionVoraz(nRaiz, B, numNodos);
        }
    }

    /* Si la solución no es voraz o estamos en cotas triviales hay que desarrollar el algoritmo*/
    if(nRaiz.CS != nRaiz.CI){
        printf("\t\tNodo\t%d: CI=%.2f, BE=%.2f, CS=%.2f ---> Entra en LNV (C = %.2f)\n", nRaiz.n, nRaiz.CI, nRaiz.BE, nRaiz.CS, C);
        insertarElementoLista(&LNV, finLista(LNV), nRaiz);
    }

    while(!esListaVacia(LNV)){
        nSelect = _seleccionar(&LNV); /* Seleccionamos el mejor nodo de la lista de nodos vivos */
        /*¿Cumple la condicion de Poda?*/
        if(nSelect.CS > C){
            printf("✅ Ramifico nodo %d\n", nSelect.n);
            /* Generamos los hijos del nodo seleccionado */
            for(int ciudad = 0; ciudad < N; ciudad++){
                /* Solo vamos a desarrollar el nodo cuando sea válido, es decir
                la ciudad selecionada en dicha iteración no haya sido asignada a ningún otro galeón */
                if(nSelect.usadas[ciudad] == 0){
                    /* Creamos un nodo hijo válido, inicializando valores */
                    (*numNodos)++;
                    nHijo.n = (*numNodos);
                    nHijo.nivel = nSelect.nivel + 1;
                    nHijo.bact = nSelect.bact + B[nSelect.nivel][ciudad];
                    for(int i = 0; i < N; i++){
                        nHijo.tupla[i] = nSelect.tupla[i];
                        nHijo.usadas[i] = nSelect.usadas[i];
                    }
                    nHijo.tupla[nSelect.nivel] = ciudad; /* Asignamos la ciudad al galeón del nivel del nodo */
                    nHijo.usadas[ciudad] = 1; /* Marcamos la ciudad como asignada a un galeón */
                    /* Actualizamos las cotas del nodo hijo */
                    if(modo == 0){
                        _CI_trivial(&nHijo);
                        _CS_trivial(&nHijo, max_beneficio);
                    }
                    else if(modo == 1){
                        _CI_precisa(&nHijo, B);
                        _CS_precisa(&nHijo, B);
                    }
                    _BE(&nHijo);

                    if(modo == 1){
                        if(!_solucion(nHijo) && nHijo.CS >= C && nHijo.CS == nHijo.CI){ /* Cuando se igualaban CI y CS significaba que a partir de ese nodo la solución óptima era voraz*/
        
                            printf("\t|____Nodo\t%d: CI=%.2f, BE=%.2f, CS=%.2f ---> Es una solución voraz\n", nHijo.n, nHijo.CI, nHijo.BE, nHijo.CS);
                            nHijo = _SolAsignacionVoraz(nHijo, B, numNodos);

                            if(nHijo.bact > nSolucion.bact){ /* Guardamos la mejor solución hasta la fecha y actualizamos la cota de poda si es necesario*/
                                nSolucion = nHijo;
                                
                                if(nSolucion.bact > C){
                                    C = nSolucion.bact; 
                                }
                                continue;
                            }

                        }
                    }

                    if(_solucion(nHijo) && nHijo.bact > nSolucion.bact){ /* Guardamos la mejor solución hasta la fecha y actualizamos la cota de poda si es necesario*/
                        nSolucion = nHijo;
                        printf("\t|____Nodo\t%d: CI=%.2f, BE=%.2f, CS=%.2f ---> Es solucion: %d", nSolucion.n, nSolucion.CI, nSolucion.BE, nSolucion.CS, nSolucion.bact);
                        if(nSolucion.bact > C){
                            C = nSolucion.bact; 
                            printf(" ---> C = %.2f", C);
                        }
                        printf("\n");
                    }
                    else if(!_solucion(nHijo)){ /* Solo insertamos los nodos que superen la cota de poda y puedan ser ramificados (no son solución)*/
                        if(nHijo.CS > C){
                            insertarElementoLista(&LNV, primeroLista(LNV), nHijo); /* Lo insertamos al principio para hacer MB-LIFO*/
                            printf("\t|____Nodo\t%d: CI=%.2f, BE=%.2f, CS=%.2f ---> Entra en LNV (C = %.2f)\n", nHijo.n, nHijo.CI, nHijo.BE, nHijo.CS, C);
                            if(nHijo.CI > C){
                                C = nHijo.CI; /* Actualizamos la cota de poda a su nuevo valor*/
                            }
                        }
                        else{
                            printf("\t|____Nodo\t%d: CI=%.2f, BE=%.2f, CS=%.2f ---> No entra LNV (C = %.2f)\n", nHijo.n, nHijo.CI, nHijo.BE, nHijo.CS, C);
                        }
                    }
                }  
            }
        }
        else if(modo == 1 && nSelect.CS == C && nSelect.CS == nSelect.CI){
            printf("\t\tNodo\t%d: CI=%.2f, BE=%.2f, CS=%.2f ---> Es una solución voraz\n", *(numNodos), nSelect.CI, nSelect.BE, nSelect.CS);
            nSelect = _SolAsignacionVoraz(nSelect, B, numNodos);
            
            if(nSelect.bact > nSolucion.bact){ /* Guardamos la mejor solución hasta la fecha y actualizamos la cota de poda si es necesario*/
                nSolucion = nSelect;
                if(nSolucion.bact > C){
                    C = nSolucion.bact; 
                }
            }
        } 
        else{
            printf("❌ Podo nodo %d\n", nSelect.n);
        }
    }
    /* Al finalizar el algoritmo, copiamos la mejor solución encontrada al vector solución pasado como parámetro */
    for(int i = 0; i < N; i++){
        s->tupla[i] = nSolucion.tupla[i];
    }

    printf((modo==0)?"\n\n---- Estadísticas de Ramificación y Poda triviales ----\n":"\n\n---- Estadísticas de Ramificación y Poda Precisas ----\n");
    printf("Nodos generados: %d\n\n", (*numNodos));

    destruirLista(&LNV); /* Liberamos la memoria de la lista */
}





