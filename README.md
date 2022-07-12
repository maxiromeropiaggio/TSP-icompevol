# TSP-icompevol
Trabajo Final para la materia optativa Introducción a la Computación Evolutiva, cursada 2021.

La aplicación presentada permite configurar distintos parámetros dentro de lo que significa plantear un algoritmo evolutivo clásico de la literatura según las recomendaciones y explicaciones que aprendimos en la materia.
En particular, es una pequeña implementación que permite correr varios experimentos de forma concurrente.

Para compilar y crear el .jar que corre el algoritmo evolutivo ejecute a través de la consola el archivo 'createTSPjar.sh'

Recuerde que el comando para correr la aplicación es la siguiente:
```
java -jar TSP.jar <arg1> <arg2> ... <argn>
```

Aclaraciones:
*   La aplicación se creará en la carpeta raíz.
*   El listado de argumentos deben indicar archivos de experimento, y deben ubicarse en la carpeta 'experiments' para ser utilizados. Se dejarán algunos ejemplos para saber cuales son los parámetros configurables.
*   La salida de cada experimento se encontrará separado de la siguiente forma: 'results_<nombre_experimento>' (se le da un nombre como parámetro).
*   En la carpeta instancias-TSP deben ubicarse ejemplares del problema, con el formato específico.
