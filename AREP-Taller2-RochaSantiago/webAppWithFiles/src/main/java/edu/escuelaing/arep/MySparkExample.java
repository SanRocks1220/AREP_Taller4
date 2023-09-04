package edu.escuelaing.arep;

import java.io.IOException;

public class MySparkExample {

    public static void main(String[] args) throws IOException{
        // GET
        HttpServer.get("/hello", str -> HttpServer.helloFormer(str));
        HttpServer.get("/getFileData", str -> HttpServer.getFileData(str));

        //POST
        HttpServer.get("/hellopost", str -> "Hello " + str);

        //Start App
        HttpServer.getInstance().start(args);
    }
}
