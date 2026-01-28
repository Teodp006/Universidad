<html>
  <head>
    
  </head>
  <body>
    <h1>Técnicas algorítmicas</h1>
    <blockquote> Créditos a <a href="https://github.com/ismaaa06">Isma</a> por realizar este informe conmigo.</blockquote>
    <h2></h2> <!---Solo para poner la línea en medio--->
    <h3 align="center"> Como esta organizado este proyecto</h3>
    <ul>
      <li> 📝Documento con el informe realizado acerca de la comparativa de ambos programas</li>
      <li> Makefile para literalmente no tener que saber como compilar nada del proyecto</li>
      <li> Launcher.c para decidir en tiempo de ejecución que tamaño de matriz desea el usuario</li>
      <li> Código Backtraking.c y su librería </li>
      <li> Código Ramipoda.c y su librería <em>(Branch & Bound)</em> </li>
      <li> Código funciones.c y su librería <em>(Para imprimir las asignaciones con el siguiente formato:)</em> </li>
    </ul>
    <img src="https://github.com/Teodp006/Universidad/blob/main/Segundo/AED/Practicas/Practica6/ResultadoFuncionesImpresi%C3%B3n.png" align="center" width="1000">
    <h2></h2> <!---Solo para poner la línea en medio--->
    <h3 align="center"> Sobre el Makefile y el Launcher.c </h3>
    <p> Este había sido el proyecto de AED que más había currado en el sentido de motivarme por el, después de JdTMAP, ya que utilicé las flags del compilador
        para poder hacer que el tamaño de la matriz se definiese en tiempo de compilación, entonces gracias a launcher.c y dos compilaciones paralelas del Makefile
        pude realizar una especie de menú interactivo que te permite <code>x) cambiar tamaño matriz </code> durante la ejecución del programa así como elegir el tamaño
        que deseas al comienzo del mismo, todo esto sin tener que comentar y descomentar nada.</p>
        <h3 align="center">¿Como funcionan las guardias del compilador?</h3>
    <p>
      <strong>👇lista.h👇</strong>
    </p>
  </body>
</html> <!---Cerramos html Para poder meterle una equiqueta MarkDown, si no GitHub lo ingnora y no renderiza el código como MarkDown--->

```c
#ifndef N // Si N no está definido entra en el bloque if(!isDefined(N)){
#define N 3 /* Define N = 3*/
#endif // Salimos del bloque }
```

<html> <!---Abrimos otra vez para volver a tener todo bien estructurado en HTML--->
  <body>
    <p>
      Nos permite modificar el valor de N, ya que si no el archivo lista.h sobredefiniría siempre el valor de N que nosotros intentemos definir en tiempo de compilación,
      mostrándonos un error por terminal.
    </p>
    <p>
      <strong>Comando para modificar las flags del compilador</strong>
    </p>
    </body>
</html> <!---Cerramos html Para poder meterle una equiqueta MarkDown, si no GitHub lo ingnora y no renderiza el código como MarkDown--->

```bash
gcc -Wall main.c lista.c backtraking.c ramipoda.c funciones.c -DN=6 -o ejecutable #-DN=6 es #define N = 6
```

<html> <!---Abrimos otra vez para volver a tener todo bien estructurado en HTML--->
  <body>
    <p>
      Finalmente el Makefile compila con las dos opciones <code>-DN=3</code> y <code>-DN=6</code> y luego es desde el launcher.c donde se elije cual ejecutable iniciar al comienzo
      y desde la opción x) del menú del <code>main()</code> se puede permutar de uno a otro.
    </p>
    <h2></h2> <!---Solo para poner la línea en medio--->
    <h3 align="center"> Nota de esta práctica: 8,25/10</h3>
    <p> Apreciaciones de la profesora: </p>
    <blockquote> <em>  "En el zip no hay que entregar el Makefile. BT: La solución debe mostrarse con los nombres de las ciudades y los galeones. RyP: lo mismo. El análisis comparativo de número de nodos vs complejidad de las cotas es casi inexistente. Minimización: incompleto." </em> </blockquote>
  </body>
</html>

