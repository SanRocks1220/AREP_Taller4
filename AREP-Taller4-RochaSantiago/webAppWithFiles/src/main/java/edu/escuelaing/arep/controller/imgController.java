package edu.escuelaing.arep.controller;

import edu.escuelaing.arep.HttpServer;
import edu.escuelaing.arep.controller.annotations.Component;
import edu.escuelaing.arep.controller.annotations.RequestMapping;

/**
 * Clase que actúa como controlador para gestionar solicitudes web relacionadas con imágenes.
 * Está marcada con la anotación "@Component", lo que indica que es un componente gestionado por el sistema.
 * @Author Santiago Andrés Rocha
 */
@Component
public class imgController {
    /**
     * Método manejador de solicitud para la ruta "/jpg".
     * Devuelve los datos de la imagen "ia.jpg" almacenada en el servidor.
     * Está marcado con la anotación "@RequestMapping" para indicar la URL asociada.
     * @return Una cadena que contiene los datos de la imagen JPEG.
     */
    @RequestMapping(value = "/jpg")
    public static String jpg() {
        return HttpServer.getFileData("ia.jpg");
    }

    /**
     * Método manejador de solicitud para la ruta "/png".
     * Devuelve los datos de la imagen "logoECI.png" almacenada en el servidor.
     * Está marcado con la anotación "@RequestMapping" para indicar la URL asociada.
     * @return Una cadena que contiene los datos de la imagen PNG.
     */
    @RequestMapping(value = "/png")
    public static String png() {
        return HttpServer.getFileData("logoECI.png");
    }

    /**
     * Método manejador de solicitud para la ruta "/gif".
     * Devuelve los datos de la imagen "loading.gif" almacenada en el servidor.
     * Está marcado con la anotación "@RequestMapping" para indicar la URL asociada.
     * @return Una cadena que contiene los datos de la imagen GIF.
     */
    @RequestMapping(value = "/gif")
    public static String gif() {
        return HttpServer.getFileData("loading.gif");
    }
}

