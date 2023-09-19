package edu.escuelaing.arep.controller;

import edu.escuelaing.arep.HttpServer;
import edu.escuelaing.arep.controller.annotations.Component;
import edu.escuelaing.arep.controller.annotations.RequestMapping;

@Component
public class srtController {
    @RequestMapping(value = "/html")
    public static String html() {
        return HttpServer.getFileData("htmlBasico.html");
    }

    @RequestMapping(value = "/txt")
    public static String txt() {
        return HttpServer.getFileData("textoEjemplo.txt");
    }
}
