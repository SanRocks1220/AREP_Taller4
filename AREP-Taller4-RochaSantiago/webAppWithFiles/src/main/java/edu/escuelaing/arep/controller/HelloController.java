package edu.escuelaing.arep.controller;

import edu.escuelaing.arep.HttpServer;
import edu.escuelaing.arep.controller.annotations.Component;
import edu.escuelaing.arep.controller.annotations.RequestMapping;

/**
 * Clase que actúa como controlador para gestionar las solicitudes web relacionadas con "/hello".
 * Está marcada con la anotación "@Component", lo que indica que es un componente gestionado por el sistema.
 * @Author Santiago Andrés Rocha
 */
@Component
public class helloController {
    
    /**
     * Método manejador de solicitud para la ruta "/hello".
     * Devuelve una respuesta HTTP que contiene una página HTML básica con un saludo.
     * Está marcado con la anotación "@RequestMapping" para indicar la URL asociada.
     * @return Una cadena que representa la respuesta HTTP.
     */
    @RequestMapping(value = "/hello")
    public static String index() {
        return indexResponse();
    }

    /**
     * Método privado que genera la respuesta HTML para la página de saludo.
     * @return Una cadena que contiene la respuesta HTML.
     */
    public static String indexResponse() {
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "    <head>\r\n"
                + "        <title>Form Example</title>\r\n"
                + "        <meta charset=\"UTF-8\">\r\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
                + HttpServer.getCSS()
                + "    </head>\r\n"
                + "    <body>\r\n"
                + "        <div class=\"feedback-card\">\r\n"
                + "            <div class=\"feedback-header\">\r\n"
                + "                <h1>Pagina Principal</h1>\r\n"
                + "                <h3>Hola! Este es el servicio Hello basico ;)</h3>\r\n"
                + "            </div>\r\n"
                + "    </body>\r\n"
                + "</html>";
        return outputLine;
    }
}
