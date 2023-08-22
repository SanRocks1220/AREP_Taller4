package edu.escuelaing.arep;

import java.net.*;
import java.io.*;
import java.io.FileInputStream;

public class HttpServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;

        
        try {
            System.out.println("Listo para recibir ...");
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        
        
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        String inputLine, outputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            if (!in.ready()) {
                break;
            }
        }
        outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + getFiles("AREP-Taller2-RochaSantiago\\webAppWithFiles\\src\\main\\resources\\mainPage.html") + inputLine;

        out.println(outputLine);
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static String getFiles(String path) {
        // Crear un objeto File con la ruta del archivo
        File archivo = new File(path);
        //System.out.println(archivo);

        // Verificar si el archivo existe
        if (!archivo.exists()) {
            System.out.println("El archivo no existe.");
        }

        // Declarar un arreglo de bytes para almacenar los datos del archivo
        byte[] buffer = new byte[(int) archivo.length()];

        try (FileInputStream fis = new FileInputStream(archivo)) {
            // Leer los datos del archivo en el arreglo de bytes
            fis.read(buffer);

            // Convertir los bytes en una cadena y mostrar el contenido del archivo
            String contenido = new String(buffer);
            System.out.println("Contenido del archivo:");
            System.out.println(contenido);
            return contenido;
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }
}