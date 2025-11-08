# JDT MAP
Lo primero que tenemos que entender en este proyecto es como funciona la estructura `tipografo`:

```c
struct tipografo {
int N; //número de vértices del grafo
tipovertice VERTICES[MAXVERTICES]; //vector de vértices
tipoarco A[MAXVERTICES][MAXVERTICES]; //matriz de adyacencia
};
```
#
Un Grafo se ha simplificado para ser una estructura estática de tamaño `MAXVERTICES`$= 100$, por lo tanto su matriz de Adyacencia será $_{100\times100}$ y su vector de vértices tendrá tamaño $100$, por eso debemos guardar $N$ en la estructura del grafo y realizar transformaciones dinámicamente para ajustar el tamaño del grafo.

Por otra parte yo definí la estructura `TIPOARCO`que compone cada $a_{ij}$ de la Matriz en un puntero. Por dos razones principales.
  
  - Inicialización más sencilla (valor `NULL`), no hay que inventarse valores para distancia o TipoCon.
  - Ahorro significativo de memoria, ya la Matriz es simétrica (porque es un grafo no dirigido), yo utilizo $N/2$ punteros a las mismas estructuras.
  
    - Cada `struct` inicializado ocupa $8 \ Bytes$ del float y $4 \ Bytes$ del enum TipoCon.
    - Cada **puntero** a `struct` ocupa solo $8 \ Bytes$.
  
    $\implies$ En total es un ahorro de $4 \ Bytes$ por estructura no incilizada y $N/2$ estructuras totales.
#
### Matriz de Adyacencia
Inicialización:

$\begin{pmatrix}
    Null && Null && Null && ... && Null \\
    Null && Null && Null && ... && Null \\
    Null && Null && Null && ... && Null \\
    \vdots && \vdots && \vdots && \ddots && Null \\
    Null && Null && Null && Null && Null
\end{pmatrix}$

Para entenderlo mejor $A[i][j]$ será exactamente la conexión entre los vértices (a $G\rightarrow vertices$ la llamaremos `array`, porque es un array de vértices):

$array[i] \ \bigcirc \xrightarrow{A[i][j]} \bigcirc \ array[j]$

#### Funciones

*`crear_arco()`* $\to$ Crea una conexión entre pos1 y pos2 (índices del $array$ de vértices)
```c
void crear_arco(grafo *G, int pos1, int pos2, float distancia, tipoCon tipo) {
    if((*G)->A[pos1][pos2] == NULL && (*G)->A[pos2][pos1] == NULL){ // Si no existía arco, hay que reservar memoria, si existía solo se remplaza la información
        (*G)->A[pos1][pos2] = (tipoarco)malloc(sizeof(struct TELEMENTOARCO));
    }
    (*G)->A[pos1][pos2]->distancia = distancia;
    (*G)->A[pos1][pos2]->tipo = tipo;
    (*G)->A[pos2][pos1] = (*G)->A[pos1][pos2]; // Como es un grafo no dirigido pos1-pos2 es igual a pos2-pos1, simplemente apuntan al mismo struct
}
```
Importante:
    
- Si ambos punteros son NULL, se hace una única reserva de memoria para los dos.
  
  $A[i][j] \xrightarrow{ \  \ }{} \downarrow \\
  \\ \qquad \qquad \quad \xrightarrow{}{} \begin{bmatrix} \text{Memory bloq} \end{bmatrix}
  \\
  A[j][i]  \xrightarrow{ \  \ }{} \uparrow$

De tal manera que si se ejecuta:
```c
free(A[i][j])
```
Esto es como si ejecutase al mismo tiempo:
```c
free(A[j][i])
```
Y de hecho si hacemos las dos nos dará un 
```diff
- Double free() error
```
Esto es importante para la siguiente función que vamos a ver:

 *`borrar_arco()`* $\to$ Elimina la conexión entre pos1 y pos2 (índices del $array$ de vértices).

