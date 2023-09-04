package edu.escuelaing.arep;

import java.io.IOException;

/**
 * Clase que contiene el punto de entrada principal para iniciar el servidor HTTP y definir las rutas de las solicitudes GET y POST.
 * @author Santiago Andres Rocha C.
 */
public class MySparkExample {

    public static void main(String[] args) throws IOException{
        // GET
        HttpServer.get("/hello", str -> HttpServer.helloFormer(str));
        HttpServer.get("/getFileData", str -> HttpServer.getFileData(str));

        //POST
        HttpServer.post("/helloPost", str -> HttpServer.helloFormer(str));
        HttpServer.post("/getFileDataPost", str -> HttpServer.getFileData(str));

        //Start App
        HttpServer.getInstance().start(args);
    }
}
