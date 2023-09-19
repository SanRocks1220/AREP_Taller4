# Servidor Web en Java

## Resumen
Este proyecto es un servidor web en Java que permite entregar páginas HTML e imágenes PNG. También proporciona un framework IoC (Inversión de Control) para construir aplicaciones web a partir de POJOS (Plain Old Java Objects). El servidor es capaz de atender múltiples solicitudes no concurrentes.

## Buenas Prácticas y Arquitectura
El servidor web en Java sigue las siguientes buenas prácticas y arquitectura:
- Uso de patrón de diseño Singleton para garantizar una única instancia del servidor.
- Utilización de anotaciones como `@RequestMapping` y `@Component` para definir servicios y componentes.
- División del código en clases y métodos específicos para facilitar la lectura y el mantenimiento.
- Gestión de excepciones para manejar posibles errores durante la ejecución.

## Funcionamiento
El servidor web en Java funciona de la siguiente manera:
1. El servidor se inicia mediante la clase `MainService`, que crea una instancia de `HttpServer`.
2. Los componentes y servicios se registran mediante anotaciones como `@Component` y `@RequestMapping`.
3. El servidor escucha en un puerto específico (por ejemplo, el puerto 35000) y espera solicitudes entrantes.
4. Cuando llega una solicitud HTTP GET o POST, el servidor la procesa y busca el servicio correspondiente.
5. Si encuentra un servicio registrado para la URL de la solicitud, lo ejecuta y devuelve una respuesta HTTP.
6. Si no encuentra un servicio registrado, devuelve un mensaje de error o una página predeterminada.

## Estrategias de Programación Empleadas
En este proyecto, se emplearon las siguientes estrategias de programación:
- Uso de reflexión en Java para cargar dinámicamente clases y métodos.
- Implementación del patrón de diseño Singleton para garantizar una única instancia del servidor.
- Separación de responsabilidades en clases específicas, como `HttpServer` para la lógica del servidor y `MainService` para la inicialización.
- Utilización de estructuras de datos como mapas para almacenar servicios registrados y en caché.

## Pruebas Unitarias
El proyecto incluye pruebas unitarias para verificar el funcionamiento correcto de las clases y métodos clave. Las pruebas se encuentran en el directorio de pruebas y se pueden ejecutar utilizando un marco de pruebas como JUnit.

## Cómo Usar Este Proyecto
Para usar este proyecto, siga estos pasos:

1. Clone el repositorio en su máquina local.
2. Compile el proyecto utilizando Maven o una herramienta similar.
3. Para ejecutar el servidor, utilice el siguiente comando en la línea de comandos: `java -cp target/classes edu.escuelaing.arep.service.MainService`
4. El servidor se iniciará y estará listo para recibir solicitudes en el puerto 35000 (puede cambiar el puerto si es necesario).
5. Puede definir servicios web agregando clases con anotaciones `@Component` y `@RequestMapping` en el proyecto.
6. Acceda a los servicios utilizando su navegador o herramientas de cliente HTTP.

## Autor

- [Santiago Andres Rocha C.](https://github.com/SanRocks1220)

## Colaboradores

- [Julia Mejia](https://github.com/juliamejia)
- [David Valencia](https://github.com/DavidVal6)
- [ChatGPT](https://chat.openai.com)
