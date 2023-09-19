package edu.escuelaing.arep.controller;

import edu.escuelaing.arep.HttpServer;
import edu.escuelaing.arep.controller.annotations.Component;
import edu.escuelaing.arep.controller.annotations.RequestMapping;

@Component
public class imgController {
    @RequestMapping(value = "/image")
    public static String index() {
        return HttpServer.getFileData("ia.jpg");
    }
}
