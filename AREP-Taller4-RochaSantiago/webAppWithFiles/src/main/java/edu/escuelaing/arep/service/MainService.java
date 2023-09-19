package edu.escuelaing.arep.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import edu.escuelaing.arep.HttpServer;

/**
 * Clase que contiene el punto de entrada principal para iniciar el servidor HTTP y definir las rutas de las solicitudes GET y POST.
 * @author Santiago Andres Rocha C.
 */
public class MainService {

    /**
     * El método principal de la clase MySparkExample.
     * @param args Argumentos de línea de comandos (no se utilizan en este ejemplo).
     * @throws IOException Excepción lanzada en caso de errores de entrada/salida.
     */
    public static void main(String[] args) throws IOException{
        HttpServer server = HttpServer.getInstance();
        try {
            server.run(args);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
