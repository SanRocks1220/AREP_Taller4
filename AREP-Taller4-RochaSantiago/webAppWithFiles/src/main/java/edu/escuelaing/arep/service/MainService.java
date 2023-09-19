package edu.escuelaing.arep.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import edu.escuelaing.arep.HttpServer;

/**
 * Clase principal que inicia el servidor HTTP y gestiona la ejecución del programa.
 * Crea una instancia de HttpServer y la utiliza para iniciar el servidor y manejar excepciones.
 * @author Santiago Andres Rocha
 */
public class MainService {

    /**
     * Método principal que inicia el servidor HTTP y maneja excepciones relacionadas con su ejecución.
     * @param args Argumentos de línea de comandos (no se utilizan en esta versión).
     * @throws IOException Excepción arrojada en caso de no poder establecer la conexión.
     */
    public static void main(String[] args) throws IOException {
        // Crea una instancia de HttpServer
        HttpServer server = HttpServer.getInstance();

        try {
            // Inicia el servidor HTTP y maneja excepciones de clase
            server.run(args);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
