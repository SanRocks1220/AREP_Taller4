# Web Server en Java para Obtener Archivos desde el Disco Duro

Este proyecto consiste en la implementación de un servidor web en Java que permite solicitar archivos desde el disco duro a través de una aplicación web. El servidor es capaz de recibir solicitudes HTTP GET y POST para obtener el contenido de los archivos especificados. Además, se utiliza el concepto de funciones Lambda para crear el backend que maneja las diferentes URL.

## Arquitectura y Patrones de Diseño

El proyecto se centra en una arquitectura simple de servidor web que utiliza funciones Lambda para manejar las solicitudes en diferentes URL. Cada URL se asigna a una función Lambda que se encarga de procesar la solicitud correspondiente. Esto proporciona una estructura modular y flexible para crear un servidor web que responde a las solicitudes de manera eficiente.

## Funcionamiento

El proyecto consta de una clase principal:

- **`HttpServer`**: Esta clase representa el servidor web. Utiliza funciones Lambda para asignar una acción específica a cada URL. Cuando se recibe una solicitud en una URL determinada, se ejecuta la función Lambda correspondiente, que maneja la solicitud y genera una respuesta adecuada.
- **`MySparkExample`**: Esta clase inicializa el servidor web, por lo que desde aquí es posible comenzar a hacer las solicitudes.

## Estrategias de Programación Empleadas

En este proyecto, se aplicaron las siguientes estrategias de programación:

1. **Funciones Lambda para el Backend**: En lugar de utilizar una arquitectura de servidor web concurrente, se aprovechan las funciones Lambda para asignar comportamientos específicos a cada URL. Esto simplifica el código y permite un enfoque más modular.

2. **Manejo de Sockets en Java**: Se utiliza la clase `ServerSocket` para aceptar conexiones entrantes de los clientes y para establecer la comunicación entre el servidor y los clientes.

3. **Interfaz de Usuario HTML**: Se genera una interfaz de usuario simple en HTML para interactuar con el servidor. Se crean formularios GET y POST para solicitar nombres de archivos y se aplica un estilo CSS básico para mejorar la presentación.

## Pruebas

Se han realizado pruebas exhaustivas para asegurarse de que el servidor web funcione correctamente y maneje de manera adecuada las solicitudes HTTP. Se han abordado varios aspectos en las pruebas para garantizar la calidad del código y la funcionalidad del servidor.

### Pruebas Unitarias

Se han implementado pruebas unitarias utilizando el framework de pruebas JUnit. Estas pruebas se centran en probar las diferentes funciones y métodos de la clase `HttpServer` de manera aislada para asegurarse de que produzcan los resultados esperados. Las pruebas abordan casos de éxito y casos de borde, como:

- Prueba de obtención de datos de archivos desde el disco duro.
- Prueba de generación de respuestas HTML para solicitudes GET y POST.
- Prueba de manejo adecuado de formatos de archivo y tipos MIME.

### Pruebas de Integración

Se han realizado pruebas de integración para verificar que los componentes del servidor trabajen correctamente en conjunto. Estas pruebas simulan el flujo completo de una solicitud HTTP, desde la conexión del cliente hasta la generación de la respuesta por parte del servidor. Se han abordado los siguientes aspectos:

- Prueba de conexión y comunicación adecuada entre clientes y servidor.
- Prueba de procesamiento de solicitudes GET y POST.

### Pruebas Manuales

Además de las pruebas automatizadas, se han realizado pruebas manuales exhaustivas para verificar la funcionalidad y el aspecto visual de la interfaz de usuario generada en HTML. Se han probado casos como:

- Ingresar nombres de archivos válidos e inválidos en los formularios.
- Hacer solicitudes GET y POST desde el navegador.
- Verificar la visualización correcta de archivos de texto en las respuestas.

## Cómo Usar Este Proyecto

Este proyecto se ha configurado para utilizarse con Maven como herramienta de administración de dependencias y construcción. Aquí están los pasos para ejecutar y probar el servidor web:

### Requisitos Previos

Asegurarse de tener instalados los siguientes elementos en su sistema:

1. **Java Development Kit (JDK)**: Debe estar instalada una versión del JDK.
2. **Maven**: Debe estar instalado Maven.

### Pasos para Ejecutar el Proyecto

1. **Clonar el Repositorio**: Abrir una terminal y navegar hasta el directorio donde se desee clonar el repositorio.
2. **Compilar el Proyecto**: Ejecutar el comando `mvn compile`.
3. **Ejecutar el Servidor**: Ejecutar el comando `mvn exec:java -Dexec.mainClass="edu.escuelaing.arep.MySparkExample"`.
4. **Acceder a la Interfaz de Usuario**: Abrir un navegador y entrar a `http://localhost:35000/` para acceder a la interfaz de usuario principal.
5. **Realizar solicitudes con las URL**: Posterior a `http://localhost:35000/` puede hacer uso de:
    * `/hello?name=<Su Nombre Aquí>`
    * `/helloPost?name=<Su Nombre Aquí>`
    * `/getFileData?name=<Archivo a Buscar Aquí>`
    * `/getFileDataPost?name=<Archivo a Buscar Aquí>`


## Buenas Prácticas y Consideraciones

- Se utiliza el concepto de funciones Lambda para crear un backend modular y flexible que responde a las solicitudes en diferentes URL.
- Se implementan buenas prácticas de programación, como nombres descriptivos de variables y métodos, división de responsabilidades en funciones Lambda y manejo adecuado de excepciones.
- Se proporciona una interfaz de usuario sencilla en HTML para interactuar con el servidor de manera intuitiva.

## Ejemplo de Desarrollo de Aplicaciones
En este ejemplo, crearemos una aplicación web simple que permite a los usuarios cargar un archivo de texto al servidor y luego ver su contenido en una página web.
1. Definiremos una clase que implementa la interfaz ServicioStr para manejar la carga de archivos y la visualización del contenido. 
    ```
    import edu.escuelaing.arep.ServicioStr;
    import java.io.BufferedReader;
    import java.io.FileReader;
    import java.io.IOException;
    import java.util.Map;

    public class FileService implements ServicioStr {

        @Override
        public String handle(String fileName) {
            // Construir el contenido HTML de la página de respuesta
            StringBuilder response = new StringBuilder();
            response.append("<!DOCTYPE html>\n<html>\n<head>\n");
            response.append("<meta charset=\"UTF-8\">\n");
            response.append("<title>Contenido del Archivo</title>\n");
            response.append("</head>\n<body>\n");

            try {
                // Leer el contenido del archivo
                FileReader fileReader = new FileReader(fileName);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append("<p>").append(line).append("</p>\n");
                }
                bufferedReader.close();
            } catch (IOException e) {
                response.append("<p>Error al leer el archivo.</p>\n");
            }

            response.append("</body>\n</html>");

            return response.toString();
        }
    }
    ```

2. Configuraremos el servidor para usar esta clase en una URL específica.
    ```
    import edu.escuelaing.arep.HttpServer;

    public class MyWebApp {

        public static void main(String[] args) {
            // Configurar el servidor
            HttpServer.get("/getFileContent", new FileService());

            // Iniciar el servidor
            HttpServer.getInstance().start(args);
        }
    }

    ```
3. Finalmente, crearemos una página HTML que permita a los usuarios cargar un archivo y ver su contenido.
    ```
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <title>Cargar Archivo</title>
    </head>
    <body>
        <h1>Cargar Archivo</h1>
        <form action="/getFileContent" method="GET">
            <label for="fileName">Nombre del Archivo:</label>
            <input type="text" id="fileName" name="fileName">
            <input type="submit" value="Ver Contenido">
        </form>
    </body>
    </html>
    ```

Cuando el usuario envía el formulario, el servidor obtiene el nombre del archivo de la URL, utiliza la clase FileService para leer el contenido del archivo y luego genera una página HTML que muestra el contenido.

Ejemplo realizado son ayuda de [ChatGPT](https://chat.openai.com)

## Ejecución de Proyecto en diferentes OS
* Windows:
   - ![image](https://github.com/SanRocks1220/AREP_Taller3/assets/99696682/85bd7cdf-4169-4b9d-872e-bb2ba296cac7)
   - ![image](https://github.com/SanRocks1220/AREP_Taller3/assets/99696682/6ec0867b-c5b4-4420-bf47-47e53cd58761)
   - ![image](https://github.com/SanRocks1220/AREP_Taller3/assets/99696682/76d3c6dc-a95b-47cb-93a1-09e67c86cc5c)

* Kali - Linux
   - ![image](https://github.com/SanRocks1220/AREP_Taller3/assets/99696682/009295e2-e0c9-4aea-aa6e-fbe2f4aff4bd)
   - ![image](https://github.com/SanRocks1220/AREP_Taller3/assets/99696682/96728593-19d5-4f26-9415-a86900b5a296)
   - Parece que no puede encontrar el path correcto...



## Autor

- [Santiago Andres Rocha C.](https://github.com/SanRocks1220)

## Colaboradores

- [David Valencia](https://github.com/DavidVal6)
- [ChatGPT](https://chat.openai.com)
