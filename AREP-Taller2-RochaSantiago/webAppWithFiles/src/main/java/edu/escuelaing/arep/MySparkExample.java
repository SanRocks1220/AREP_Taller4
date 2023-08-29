package edu.escuelaing.arep;

import java.io.IOException;

import edu.escuelaing.arep.HttpServer.get;

public class MySparkExample {
    public static void main(String[] args) throws IOException{
        get("/hello", str -> "Hello " + str);
        get("/hellopost", str -> "Hello " + str);

        HttpServer.getInstance().start(args);
    }
}
