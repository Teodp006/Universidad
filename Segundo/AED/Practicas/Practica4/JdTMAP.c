#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "grafo.h"
#include "JdTMAP.h"
#include "algoritmos.h"
#define DEFAULT_OUTPUT_FILENAME "Output.csv" //Nombre del archivo donde se guarda el grafo final si no se aporta ninguno por línea de comandos


/*Opción a del menú, introducir un vertice (ciudad) nuevo en el grafo (mapa)
* @param G: grafo donde se insertará el nuevo vértice
* @exception Si existe ya una ciudad con el mismo nombre, no se hará nada (No es posible modificar la región de la ciudad con esta función).
*/
void agregar_ciudad(grafo *G) {
    tipovertice v1;
    printf("Introduce el nombre de la ciudad: ");
    scanf(" %[^\n]", v1.nombre);
    printf("Introduce la region de la ciudad: ");
    scanf(" %[^\n]", v1.region);
    if (existe_vertice(*G, v1))
        printf("Esa ciudad ya existe en el mapa\n");
    else{
        if((insertar_vertice(G, v1)) == -1)
            printf("No se ha podido agregar la ciudad, el mapa ha llegado al máximo de su capacidad\n");
    }
}
/*Opción b del menú, eliminar un vértice (ciudad) del grafo (mapa) pidiendo su nombre
* @param G: grafo del que se eliminará el vértice
*/
void eliminar_ciudad(grafo *G) {
    tipovertice v1;
    printf("Introduce el nombre de la ciudad: ");
    scanf(" %[^\n]", v1.nombre);
    if (existe_vertice(*G, v1))
        borrar_vertice(G, v1);
    else
        printf("Esa ciudad no existe en el mapa\n");
}

/* Opcion c del menú, crear una relación (ruta) entre dos vértices (ciudades) indicando el tipo de conexión y la distancia en kilómetros
* @param G: grafo donde se insertará la nueva relación
* @exception Si alguna de las ciudades no existe, se finalizará la ejecución de la función
*/
void nueva_ruta(grafo *G) {
    tipovertice v1, v2;
    float distancia;
    tipoCon tipo;
    char tipoRuta;
    //Insertamos una nueva relación pidiendo los datos al usuario controlando que existan los vértices
    printf("Nueva ruta ciudad1-->ciudad2\n");
    //Vértice origen del arco
    printf("Introduce el nombre de la ciudad origen: ");
    scanf(" %[^\n]", v1.nombre);
    if (!existe_vertice(*G, v1)) {
        printf("La ciudad %s no existe en el mapa\n", v1.nombre);
        return;
    }
    //Vértice destino del arco
    printf("Introduce el nombre de la ciudad destino: ");
    scanf(" %[^\n]", v2.nombre);
    if (!existe_vertice(*G, v2)) {
        printf("La ciudad %s no existe en el mapa\n", v2.nombre);
        return;
    }   
    //Creación del arco (si existe se avisa al usuario y puede modificarlo)
    if (son_adyacentes(*G, posicion(*G, v1), posicion(*G, v2)) != NULL){
        printf("La ruta entre la ciudad %s y la ciudad %s por vía %s tiene una distancia de %.3fkm, ahora puedes modificarla\n", v1.nombre, v2.nombre, (tipo_conexion(*G, posicion(*G, v1), posicion(*G, v2)) == tierra ? "terrestre" : "marítima"), longitud(*G, posicion(*G, v1), posicion(*G, v2)));
    }

    printf("Introduce la distancia entre las dos ciudades: ");
    scanf("%f", &distancia);
    printf("Introduce el tipo de conexion ('t'=tierra, 'm'=mar): ");
    scanf(" %c", &tipoRuta);
    while(tipoRuta != 't' && tipoRuta != 'm'){
        printf("\tTipo de conexion no valido. Introduce 't' para tierra o 'm' para mar: ");
        scanf(" %c", &tipoRuta);
    }
    tipo = (tipoRuta == 't' ? tierra : mar);
    crear_arco(G, posicion(*G, v1), posicion(*G, v2), distancia, tipo);
      
}

/*Opción d del menú, eliminar una relación (ruta) entre dos vértices (ciudades)
* @param G: grafo del que se eliminará la relación
* @exception Si alguna de las ciudades no existe, se finalizará la ejecución de la función
*/
void eliminar_ruta(grafo *G) {
    tipovertice v1, v2;
    //Eliminamos una relación pidiendo los datos al usuario controlando que existan los vértices
    printf("Eliminar ruta ciudad1-->ciudad2\n");
    //Vértice origen del arco
    printf("Introduce el nombre de la ciudad origen: ");
    scanf(" %[^\n]", v1.nombre);
    if (!existe_vertice(*G, v1)) {
        printf("La ciudad %s no existe en el mapa\n", v1.nombre);
        return;
    }
    //Vértice destino del arco
    printf("Introduce el nombre de la ciudad destino: ");
    scanf(" %[^\n]", v2.nombre);
    if (!existe_vertice(*G, v2)) {
        printf("La ciudad %s no existe en el mapa\n", v2.nombre);
        return;
    }
    //Eliminación de la ruta
    if (son_adyacentes(*G, posicion(*G, v1), posicion(*G, v2)) != NULL)
        borrar_arco(G, posicion(*G, v1), posicion(*G, v2));
    else
        printf("No existe una ruta entre la ciudad %s y la ciudad %s\n", v1.nombre, v2.nombre);
}

/* Opción i del memú, para imprimir todos los vértices y las conexiones de ese vértice del Grafo
* @param G: grafo a imprimir
*/
void imprimir_mapa(grafo G) {
    tipovertice *VECTOR; //Para almacenar el vector de vértices del grafo
    int N; //número de vértices del grafo

    //Para recorrerla, simplemente vamos a recorrer la matriz de adyacencia
    N = num_vertices(G);
    VECTOR = array_vertices(G);
    if(N > 0){
        int i, j;
        printf("El mapa actual es:\n");
        for (i = 0; i < N; i++) {
            //Imprimo la ciudad con su nombre
            printf("Ciudad(%d): %s, %s\n", i, VECTOR[i].nombre, VECTOR[i].region);
            //Chequeo sus rutas con otras ciudades
            for (j = 0; j < N; j++){
                    if (son_adyacentes(G, i, j) != NULL && tipo_conexion(G, i, j) == tierra)
                        printf("\t%s, %s %s---(%.3fkm)---%s %s, %s\n", VECTOR[i].nombre, VECTOR[i].region, marron ,longitud(G, i, j), reset, VECTOR[j].nombre, VECTOR[j].region); // guiones marrón/naranja oscuro
                    else if (son_adyacentes(G, i, j) != NULL && tipo_conexion(G, i, j) == mar)
                        printf("\t%s, %s %s~~~(%.3fkm)~~~%s %s, %s\n", VECTOR[i].nombre, VECTOR[i].region, azul, longitud(G, i, j), reset,VECTOR[j].nombre, VECTOR[j].region); // vírgulas azules
            }
        }
    }
    else{
        printf("No hay ciudades en el Mapa\n");
    }
}
/* Función que elimina el salto de línea al final de una línea sustituyéndolo por un caracter de final de cadena
* @param linea: puntero a la cadena de caracteres a procesar
*/
void _strip_line(char *linea){
    linea[strcspn(linea, "\n")] = 0; // Elimina el salto de línea al final
}


/* Función que lee un archivo de texto pasado por línea de comandos con el formato: c1,r1,c2,r2,d,t y lo guarda en el grafo [G]
* @param *G: puntero al grafo donde se almacenará la información del archivo
* @param **argv: array de argumentos de línea de comandos
* @param argc: número de argumentos de línea de comandos
* @exception Si no se puede abrir el archivo, se muestra un mensaje de error y la función retorna sin modificar el grafo
*/
void leer_archivo(grafo *G, char** argv, int argc){
    char linea[1024], *token;
    tipovertice v1, v2;
    float distancia;
    tipoCon tipoConexion;
    if(argc == 2){
        FILE *archivo = fopen(argv[1], "r");
        if(archivo == NULL){
            printf("No se ha podido abrir el archivo %s\n", argv[1]);
            return;
        }
        while(fgets(linea, sizeof(linea), archivo)){
            /*Editamos el formato de la línea con _strip_line*/
            _strip_line(linea);
            /*Guardamos el nombre de la primera Ciudad y vemos si ya existe*/
            token = strtok(linea, ",");
            strcpy(v1.nombre, token);
            /*Si no existe hay que crearlo y guardar la región*/
            if(!existe_vertice(*G, v1)){
                token = strtok(NULL, ",");
                strcpy(v1.region, token);
                insertar_vertice(G, v1);
            }
            else{
                /*Si ya existe hay que saltarse la región*/
                token = strtok(NULL, ",");
            }
            /*Guardamos el nombre de la segunda Ciudad y vemos si ya existe*/   
            token = strtok(NULL, ",");
            strcpy(v2.nombre, token);
            /*Si no existe hay que crearlo y guardar la región*/
            if(!existe_vertice(*G, v2)){
                token = strtok(NULL, ",");
                strcpy(v2.region, token);
                insertar_vertice(G, v2);
            }
            else{
                /*Si ya existe hay que saltarse la región*/
                token = strtok(NULL, ",");
            }
            /*Guardamos la distancia y el tipo de conexión*/
            token = strtok(NULL, ",");
            distancia = atof(token);
            token = strtok(NULL, ",");
            tipoConexion = (strcmp(token, "land") == 0) ? tierra : mar;
            /*Creamos la conexión entre ambos vértices*/
            crear_arco(G, posicion(*G, v1), posicion(*G, v2), distancia, tipoConexion);
            /*Se sobreentiende que si se introduce dos veces la misma conexión:
            ciudad1,region1,ciudad2,region2,distancia1,tipo1 y ciudad1,region1,ciudad2,region2,distancia2,tipo2
            el objetivo es sobreescribir los valores de distancia y tipo de conexión*/
        }
    }
    else{
        printf("No se ha aportado ningún fichero para inicializar el Mapa, por defecto estará vacío\nUso con fichero %s <nombre_fichero>\n", argv[0]);
    }
}

/* Función que guarda las conexiones del grafo [G] (sin repetir) en un archivo de texto llamado [filename] con el formato: c1,r1,c2,r2,d,t
* @param G: puntero al grafo donde se almacenará la información del archivo.
* @param argv: array de argumentos de línea de comandos
* @param argc: número de argumentos de línea de comandos
* @exception Si no se puede abrir el archivo, se muestra un mensaje de error y probablemente se pierda la información pervia del archivo.
* @exception Si no se aporta ningún nombre de archivo, se usará "Output.csv" por defecto
*/
void guardar_archivo(grafo G, char **argv, int argc){
    char *filename;
    if(argc <2){ /*Si no se pasa el fichero, por defecto vale Output.csv*/
        printf("Guardando el mapa en el archivo por defecto %s\n", DEFAULT_OUTPUT_FILENAME);
        filename = (char*)malloc((strlen(DEFAULT_OUTPUT_FILENAME)+1));
        strcpy(filename, DEFAULT_OUTPUT_FILENAME);
    }
    else{ /*Si no, vale argv[1]*/
        filename = argv[1];
    }
    FILE *archivo = fopen(filename, "w");
    /*Probamos a abrir el archivo entregado*/
    if(archivo == NULL){
        printf("No se ha podido abrir el archivo %s para guardar el mapa\n", filename);
        return;
    }
    int N = num_vertices(G);
    tipovertice *VECTOR = array_vertices(G);
    /*Vamos a recorrer la matriz solo los elementos que están por encima de la diagonal e imprimir
    sus conexiones*/
    for(int i = 0; i < N; i++){
        for(int j = i+1; j < N; j++){
            /*Solo se imprimen conexiones entre ciudades, no ciudades aisladas*/
            tipoarco arco = son_adyacentes(G, i, j);
            /*Si están conectadas se imprimen los datos del arco con el formato preciso*/
            if(arco != NULL){
                fprintf(archivo, "%s,%s,%s,%s,%.3f,%s\n", VECTOR[i].nombre, VECTOR[i].region, VECTOR[j].nombre, VECTOR[j].region, arco->distancia, (arco->tipo == tierra) ? "land" : "sea");
            }
        }
    }
    fclose(archivo);
}


/* Opcion e del menú, para calcular la distancia mínima en kilómetro entre dos ciudades usando Floyd-Warshall
* y mostrando las distancias entre las ciudades intermedias para llegar al destino.
* @param G: grafo donde se calculará la distancia mínima
* @exception Si alguna de las ciudades no existe, se informa al usuario y se finaliza la ejecución de la función
*/
void distancia_minima(grafo G){
    // Inicializar D y P
    int N = num_vertices(G); // Numero de vértices del grafo
    float D[N][N]; // Matriz de distancias entre nodos
    int P[N][N]; // Matriz de predecesores para formar caminos
    
    // Inicializamos la matriz D a través de la matriz de Adyacencia de G con velocidades = 1 para obtener distancias
    inicializar_matrizPonderada(N, D, P, G, 1, 1);
    
    // Aplicar Floyd-Warshall a las matrices P y D
    Floyd_Warshall(N, D, P);

    // Pedir al usuario origen y destino
    tipovertice v1, v2;
    printf("Introduce el nombre de la ciudad de origen: ");
    scanf(" %[^\n]", v1.nombre);
    if(!existe_vertice(G, v1)){
        printf("La ciudad %s no existe en el mapa\n", v1.nombre);
        return;
    }
    printf("Introduce el nombre de la ciudad  de destino: ");
    scanf(" %[^\n]", v2.nombre);
    if(!existe_vertice(G, v2)){
        printf("La ciudad %s no existe en el mapa\n", v2.nombre);
        return;
    }
    printf("El camino más corto de %s a %s es:\n", v1.nombre, v2.nombre);
    // Imprimir camino recursivamente
    imprimir_camino(posicion(G,v1), posicion(G,v2), N, P, D, G, "km");
    printf("\nCon una distancia total de: %.0f\n", D[posicion(G,v1)][posicion(G,v2)]);
}


/* Opcion f del menú, para calcular el tiempo mínimo entre dos ciudades usando Floyd-Warshall en función del medio de transporte
* que puede ser caballo o dragón ('c' o 'd'), también muestra los tiempos entre las ciudades intermedias para llegar al destino.
* @param G: grafo donde se calculará el tiempo mínimo
* @exception Si alguna de las ciudades no existe, se informa al usuario y se finaliza la ejecución de la función
*/
void tiempo_minimo(grafo G){
    int N = num_vertices(G);
    float D[N][N]; // Matriz de tiempos entre nodos
    int P[N][N]; // Matriz de predecesores para formar caminos 
    // Pedir al usuario origen y destino
    tipovertice v1, v2;
    char opcion;
    printf("Introduce el nombre de la ciudad de origen: ");
    scanf(" %[^\n]", v1.nombre);
    if(!existe_vertice(G, v1)){
        printf("La ciudad %s no existe en el mapa\n", v1.nombre);
        return;
    }
    printf("Introduce el nombre de la ciudad  de destino: ");
    scanf(" %[^\n]", v2.nombre);
    if(!existe_vertice(G, v2)){
        printf("La ciudad %s no existe en el mapa\n", v2.nombre);
        return;
    }
    // Pedir tipo de desplazamiento (caballo o dragón)
    printf("Introduzca método de transporte ('c' = caballo, 'd' = dragón): ");
    scanf(" %c", &opcion);
    while(opcion != 'c' && opcion != 'd'){
        printf("\tMétodo de transporte no válido. Introduzca 'c' para caballo o 'd' para dragón: ");
        scanf(" %c", &opcion);
    }
    if(opcion == 'c'){
        // Inicializar D y P para tiempo a caballo
        inicializar_matrizPonderada(N, D, P, G, velocidad_tierra_caballo, velocidad_mar_caballo); // Velocidades distintas para el caballo según el tipo de ruta
        // Aplicar Floyd-Warshall
        Floyd_Warshall(N, D, P);
        printf("El camino más rápido a caballo de %s a %s es:\n", v1.nombre, v2.nombre);
        // Imprimir camino recursivamente
        imprimir_camino(posicion(G,v1), posicion(G,v2), N, P, D, G, "h");
        // Imprimir tiempo total, simplemente se usa la matriz D
        printf("\nCon un tiempo total de: %.2f horas\n", D[posicion(G,v1)][posicion(G,v2)]);
    }
    else if (opcion == 'd'){
        // Inicializar D y P para tiempo en dragón
        inicializar_matrizPonderada(N, D, P, G, velocidad_dragon, velocidad_dragon); // Velocidad de dragón 80 km/h tanto en tierra como en mar
        // Aplicar Floyd-Warshall
        Floyd_Warshall(N, D, P);
        printf("El camino más rápido en dragón de %s a %s es:\n", v1.nombre, v2.nombre);
        // Imprimir camino recursivamente
        imprimir_camino(posicion(G,v1), posicion(G,v2), N, P, D, G, "h");
        // Imprimir tiempo total, simplemente se usa la matriz D
        printf("\nCon un tiempo total de: %.2f horas\n", D[posicion(G,v1)][posicion(G,v2)]);
    }
    else{
        printf("Error: método de transporte no válido\n");
    }
}