# Web Server Concurrente en Java - App Web para Obtener Archivos

Este proyecto consiste en la implementación de un servidor web concurrente en Java que permite solicitar archivos desde el disco duro a través de una aplicación web. El servidor es capaz de recibir solicitudes HTTP GET y POST para obtener el contenido de los archivos especificados. El servidor también implementa un mecanismo de caché para almacenar los datos de los archivos consultados previamente y mejorar la eficiencia.

## Arquitectura y Patrones de Diseño

El proyecto sigue una arquitectura de servidor web concurrente, donde el servidor utiliza un `ServerSocket` para aceptar conexiones de clientes y maneja las solicitudes en hilos separados utilizando un `ThreadPool` para mejorar la eficiencia y permitir la concurrencia.

El servidor implementa el patrón Singleton para asegurarse de que solo exista una instancia de la clase `HttpServer`, garantizando así que el servidor sea único y controlado.

## Funcionamiento

El proyecto consta de la siguiente clase principal:

- **`HttpServer`**: Esta clase representa el servidor web concurrente. Utiliza un `ServerSocket` para aceptar conexiones de clientes y delega el procesamiento de cada solicitud a threads separados. Implementa métodos para manejar solicitudes HTTP GET y POST, obtener archivos del disco duro, guardar y buscar datos en caché, y generar respuestas HTML.

## Estrategias de Programación Empleadas

En este proyecto, se aplicaron diversas estrategias de programación:

1. **Programación Multihilo**: Se utiliza la programación multihilo para lograr la concurrencia en el servidor. Cada cliente que se conecta al servidor se maneja en un hilo separado, lo que permite que múltiples solicitudes sean atendidas simultáneamente.
2. **Uso de Threads y ExecutorService**: Los hilos se utilizan para crear un modelo concurrente en el servidor. Se emplea la clase `ExecutorService` para administrar un pool de hilos que manejan las solicitudes entrantes.
3. **Sockets en Java**: Se utiliza la clase `ServerSocket` para aceptar conexiones entrantes de los clientes y para establecer la comunicación entre el servidor y los clientes.
4. **Manejo de Caché**: Se implementa un mecanismo de caché para almacenar los datos de los archivos consultados previamente. Esto evita búsquedas repetitivas en el disco duro y mejora la eficiencia.
5. **Interfaz de Usuario HTML**: Se genera una interfaz de usuario simple en HTML para interactuar con el servidor. Se crean formularios GET y POST para solicitar nombres de archivos y se aplica un estilo CSS básico para mejorar la presentación.

## Pruebas

Se han realizado pruebas exhaustivas para asegurarse de que el servidor web concurrente funcione correctamente y maneje de manera adecuada las solicitudes HTTP. Se han abordado varios aspectos en las pruebas para garantizar la calidad del código y la funcionalidad del servidor.

### Pruebas Unitarias

Se han implementado pruebas unitarias utilizando el framework de pruebas JUnit. Estas pruebas se centran en probar las diferentes funciones y métodos de la clase `HttpServer` de manera aislada para asegurarse de que produzcan los resultados esperados. Las pruebas abordan casos de éxito y casos de borde, como:

- Prueba de obtención de datos de archivos desde el disco duro.
- Prueba de almacenamiento y recuperación de datos en caché.
- Prueba de generación de respuestas HTML para solicitudes GET y POST.
- Prueba de manejo adecuado de formatos de archivo y tipos MIME.

### Pruebas de Integración

Se han realizado pruebas de integración para verificar que los componentes del servidor trabajen correctamente en conjunto. Estas pruebas simulan el flujo completo de una solicitud HTTP, desde la conexión del cliente hasta la generación de la respuesta por parte del servidor. Se han abordado los siguientes aspectos:

- Prueba de manejo concurrente de múltiples solicitudes.
- Prueba de conexión y comunicación adecuada entre clientes y servidor.
- Prueba de procesamiento de solicitudes GET y POST.
- Prueba de funcionamiento adecuado del mecanismo de caché.

### Pruebas Manuales

Además de las pruebas automatizadas, se han realizado pruebas manuales exhaustivas para verificar la funcionalidad y el aspecto visual de la interfaz de usuario generada en HTML. Se han probado casos como:

- Ingresar nombres de archivos válidos e inválidos en los formularios.
- Hacer solicitudes GET y POST desde el navegador.
- Verificar la visualización correcta de archivos de texto e imágenes en las respuestas.


## Cómo Usar Este Proyecto

Este proyecto se ha configurado para utilizarse con Maven como herramienta de administración de dependencias y construcción. Aquí están los pasos para ejecutar y probar el servidor web concurrente:

### Requisitos Previos

Asegurarse de tener instalados los siguientes elementos en su sistema:

1. **Java Development Kit (JDK)**: Debe estar instalada una versión del JDK.
2. **Maven**: Debe estar instalado Maven.

### Pasos para Ejecutar el Proyecto

1. **Clonar el Repositorio**: Abrir una terminal y navegar hasta el directorio donde se desee clonar el repositorio.
2. **Compilar el Proyecto**: Ejecutar el comando `mvn compile`.
3. **Ejecutar el Servidor**: Ejecutar el comando `mvn exec:java -Dexec.mainClass="edu.escuelaing.arep.HttpServer"`.
4. **Acceder a la Interfaz de Usuario**: Abrir un navegador y entrar a `http://localhost:35000/` para acceder a la interfaz de usuario.

## Buenas Prácticas y Consideraciones

- Se implementó el patrón Singleton para garantizar que solo haya una instancia de la clase `HttpServer` y se pueda acceder a ella de manera controlada.
- Se utiliza un `ExecutorService` para administrar el pool de hilos y manejar las solicitudes de manera concurrente, lo que optimiza el uso de recursos y permite un manejo eficiente de las solicitudes.
- Se aplican buenas prácticas de programación, como nombres descriptivos de variables y métodos, división de responsabilidades en clases y métodos pequeños, y manejo adecuado de excepciones.
- Se implementa un mecanismo de caché para almacenar los datos de los archivos consultados previamente, lo que mejora la eficiencia y reduce las búsquedas repetitivas en el disco duro.

## Autor

- [Santiago Andres Rocha C.](https://github.com/SanRocks1220)
