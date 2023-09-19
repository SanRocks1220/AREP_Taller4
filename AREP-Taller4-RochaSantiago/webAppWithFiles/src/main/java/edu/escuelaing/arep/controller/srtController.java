package edu.escuelaing.arep.controller;

import edu.escuelaing.arep.HttpServer;
import edu.escuelaing.arep.controller.annotations.Component;
import edu.escuelaing.arep.controller.annotations.RequestMapping;

/**
 * Clase que actúa como controlador para gestionar solicitudes web relacionadas con archivos estáticos.
 * Está marcada con la anotación "@Component", lo que indica que es un componente gestionado por el sistema.
 * @Author Santiago Andrés Rocha
 */
@Component
public class srtController {
    /**
     * Método manejador de solicitud para la ruta "/html".
     * Devuelve los datos del archivo HTML "htmlBasico.html" almacenado en el servidor.
     * Está marcado con la anotación "@RequestMapping" para indicar la URL asociada.
     * @return Una cadena que contiene los datos del archivo HTML.
     */
    @RequestMapping(value = "/html")
    public static String html() {
        return HttpServer.getFileData("htmlBasico.html");
    }

    /**
     * Método manejador de solicitud para la ruta "/txt".
     * Devuelve los datos del archivo de texto "textoEjemplo.txt" almacenado en el servidor.
     * Está marcado con la anotación "@RequestMapping" para indicar la URL asociada.
     * @return Una cadena que contiene los datos del archivo de texto.
     */
    @RequestMapping(value = "/txt")
    public static String txt() {
        return HttpServer.getFileData("textoEjemplo.txt");
    }

    /**
     * Método manejador de solicitud para la ruta "/css".
     * Devuelve los datos del archivo CSS "mainPageStyle.css" almacenado en el servidor.
     * Está marcado con la anotación "@RequestMapping" para indicar la URL asociada.
     * @return Una cadena que contiene los datos del archivo CSS.
     */
    @RequestMapping(value = "/css")
    public static String css() {
        return HttpServer.getFileData("mainPageStyle.css");
    }

    /**
     * Método manejador de solicitud para la ruta "/js".
     * Devuelve los datos del archivo JavaScript "jsEjemplo.js" almacenado en el servidor.
     * Está marcado con la anotación "@RequestMapping" para indicar la URL asociada.
     * @return Una cadena que contiene los datos del archivo JavaScript.
     */
    @RequestMapping(value = "/js")
    public static String js() {
        return HttpServer.getFileData("jsEjemplo.js");
    }
}
