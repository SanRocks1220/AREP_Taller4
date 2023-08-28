package edu.escuelaing.arep;

import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Crea una conexion para App Web en la que solicitar informacion sobre peliculas directo de la API.
 * @author Santiago Andres Rocha C.
 */
public class HttpServer {

    static ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
    static ExecutorService threadPool = Executors.newFixedThreadPool(10); // Se puede ajustar el número de hilos

    private static ServerSocket serverSocket;

    /**
     * Constructor de la clase HttpServer.
     * Encargado de inicializar un objeto de la clase para ser alcanzado de forma estatica.
     * @param serverSocket Socket a abrir para permitir la conexion.
     */
    public HttpServer(ServerSocket serverSocket) {
        HttpServer.serverSocket = serverSocket;
    }

    /** 
     * Metodo principal de la clase HttpServer.
     * Encargada de crear el socket de conexion y arrancar el servidor para recibir solicitudes.
     * @param args Argumentos para la inicializacion de la clase.
     * @throws IOException Excepcion arrojada en caso de no poder establecer la conexion.
     */

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
    
        HttpServer httpServer = new HttpServer(serverSocket);
    
        boolean running = true;
        while (running) {
            System.out.println("Listo para recibir ...");
            httpServer.startServer(); // Pass the clientSocket to startServer method
        }
    
        serverSocket.close();
    }

    /**
     * Metodo encargado de encender y arrancar el servidor.
     */
    public void startServer() {
        boolean running = true;
        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                final Socket clientSocket = serverSocket.accept();
    
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        handleRequest(clientSocket);
                    }
                });
            } catch (IOException e) {
                System.err.println("Accept failed: " + e.getMessage());
            }
        }
    }
    
    

    // Se agrega un método shutdown para detener el ThreadPool cuando sea necesario
    /**
     * Encargado de terminar y cerrar las conecciones cuando se trabaja con concurrencia.
     */
    public static void shutdown() {
        threadPool.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    /** 
     * Encargado de procesar las solicitudes realizadas desde el cliente.
     * @param clientSocket Socket destinado a la conexion.
     */
    public static void handleRequest(Socket clientSocket) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            boolean firtsLine = true;
            String uriString = "";

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (firtsLine) {
                    firtsLine = false;
                    uriString = inputLine.split(" ")[1];
                    System.out.println("Uri: " + uriString);
                }
                if (!in.ready()) {
                    break;
                }
            }
            System.out.println("Uri: " + uriString);

            String outputLine = "";
            if (uriString.startsWith("/getFileData")) {
                outputLine = getFileData(uriString);
            } else {
                outputLine = indexResponse();
            }

            out.println(outputLine);
            out.close();
            in.close();

            // Cerrar el socket después de completar la solicitud
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    /** 
     * Conecta con la clase HttpConnection y solicita informacion sobre las peliculas indicadas.
     * @param uriString uri para tomar el nombre del archivo.
     * @return String Formato para poder imprimir el resultado JSON retornado.
     * @throws IOException Excepcion arrojada en caso de no poder establecer la conexion.
     */
    public static String getFileData(String uriString) throws IOException {
        String fileName = uriString.split("=")[1];
        String fileData = "";
        String fileFormat = getFormat(fileName);
        String[] imgFormats = {"png", "jpg", "gif"};
        Boolean img = false;
        for (String format : imgFormats) {
            if (fileFormat.equals(format)) {
                img = true;
            }
        }

        if (inCache(fileName)) {
            fileData = getFromCache(fileName);
        } else {
            if(!img){
                fileData = getFiles("AREP-Taller2-RochaSantiago\\webAppWithFiles\\src\\main\\resources\\" + fileName);
                saveInCache(fileName, fileData);
            } else {
                byte[] imageBytes = getImageBytes("AREP-Taller2-RochaSantiago\\webAppWithFiles\\src\\main\\resources\\" + fileName);
                fileData = Base64.getEncoder().encodeToString(imageBytes);
                //System.out.println("<img src=\"data:image/jpeg;base64," + fileData + "\" alt=\"Mi Imagen\">");
                saveInCache(fileName, fileData);
            }
            
        }
        if (img) {
            //String imageSrc = "data:image/" + format + ";base64," + fileData;
            //String imgTag = "<img src='" + imageSrc + "'>";

            String response = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    + "<!DOCTYPE html>\r\n"
                    + "<html>\r\n"
                    + "    <head>\r\n"
                    + "        <title>File Content</title>\r\n"
                    + "    </head>\r\n"
                    + "    <body>\r\n"
                    + "         <center><img src=\"data:image/jpeg;base64," + fileData + "\" alt=\"Mi Imagen\"></center>" + "\r\n"
                    + "    </body>\r\n"
                    + "</html>";
            return response;
        }
        String response = "HTTP/1.1 200 OK\r\n"
            + "Content-Type: " + fileFormat + "/html\r\n"
            + "\r\n"
            + "<!DOCTYPE html>\r\n"
            + "<html>\r\n"
            + "    <head>\r\n"
            + "        <title>File Content</title>\r\n"
            + "    </head>\r\n"
            + "    <body>\r\n"
            + "<pre>" + fileData + "</pre>\r\n"
            + "    </body>\r\n"
            + "</html>";
            //+ "Archivo " + fileFormat + " al desnudo:\r\n"
            //+ fileData.toString();
                return response;
    }

    
    /** 
     * Verifica si la pelicula ya ha sido consultada o no anteriormente.
     * @param fileName Nombre del archivo a buscar en cache.
     * @return boolean True si ya se ha buscado previamente, False si es la primera vez.
     */
    public static boolean inCache(String fileName) {
        return cache.containsKey(fileName);
    }
    

    
    /** 
     * Retorna los datos almacenados en cache del archivo que se solicite.
     * @param fileName Nombre del archivo de la que tomar los datos en cache.
     * @return String Datos completos en JSON sobre la pelicula solicitada
     */
    public static String getFromCache(String fileName){
        System.out.println("Ya está en caché, no busca en el Disco Duro");
        return cache.get(fileName);
    }

    
    /** 
     * Almacena en cache los datos del archivo una vez esta sea buscada.
     * @param fileName Nombre del archivo a la que asociar los datos.
     * @param fileData Datos del archivo a guardar asociados a su nombre.
     */
    public static void saveInCache(String fileName, String fileData){
        System.out.println("No está en caché, busca en el Disco Duro");
        cache.put(fileName, fileData);
    }
    
    /**
     * Genera los formularos para retornar informacion solicitada.
     * Hace uso del metodo getCSS() para agregar estilo al momento de enseñarse en pantalla.
     * @return String Formularios con GET y POST en su formato HTML, con un CSS basico aplicado
     */
    public static String indexResponse() {
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "    <head>\r\n"
                + "        <title>Form Example</title>\r\n"
                + "        <meta charset=\"UTF-8\">\r\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
                + getCSS()
                + "    </head>\r\n"
                + "    <body>\r\n"
                + "        <div class=\"feedback-card\">\r\n"
                + "            <div class=\"feedback-header\">\r\n"
                + "                <h1>CONSULT FILES BY NAME (GET)</h1>\r\n"
                + "            </div>\r\n"
                + "            <div class=\"feedback-body\">\r\n"
                + "                <form action=\"/getFileData\">\r\n"
                + "                    <label for=\"name\">File Name:</label><br>\r\n"
                + "                    <input type=\"text\" id=\"name\" name=\"name\" value=\"\"><br><br>\r\n"
                + "                    <button type=\"button\" onclick=\"loadGetMsg()\">Submit</button>\r\n"
                + "                </form>\r\n"
                + "                <div class=\"feedback-body__message\" id=\"getrespmsg\"></div>\r\n"
                + "            </div>\r\n"
                + "        </div>\r\n"
                + "\r\n"
                + "        <div class=\"feedback-card\">\r\n"
                + "            <div class=\"feedback-header\">\r\n"
                + "                <h1>CONSULT FILES BY NAME (POST)</h1>\r\n"
                + "            </div>\r\n"
                + "            <div class=\"feedback-body\">\r\n"
                + "                <form action=\"/getFileData\">\r\n"
                + "                    <label for=\"postname\">File Name:</label><br>\r\n"
                + "                    <input type=\"text\" id=\"postname\" name=\"name\" value=\"\"><br><br>\r\n"
                + "                    <button type=\"button\" onclick=\"loadPostMsg(postname)\">Submit</button>\r\n"
                + "                </form>\r\n"
                + "                <div class=\"feedback-body__message\" id=\"postrespmsg\"></div>\r\n"
                + "            </div>\r\n"
                + "        </div>\r\n"
                + "\r\n"
                + "        <script>\r\n"
                + "            function loadGetMsg() {\r\n"
                + "                let fileName = document.getElementById(\"name\").value;\r\n"
                + "                const xhttp = new XMLHttpRequest();\r\n"
                + "                xhttp.onload = function() {\r\n"
                + "                    document.getElementById(\"getrespmsg\").innerHTML =\r\n"
                + "                    this.responseText;\r\n"
                + "                }\r\n"
                + "                xhttp.open(\"GET\", \"/getFileData?name=\" + fileName);\r\n"
                + "                xhttp.send();\r\n"
                + "            }\r\n"
                + "\r\n"
                + "            function loadPostMsg(name) {\r\n"
                + "                let url = \"/getFileDatapost?name=\" + name.value;\r\n"
                + "                fetch(url, { method: 'POST' })\r\n"
                + "                    .then(x => x.text())\r\n"
                + "                    .then(y => document.getElementById(\"postrespmsg\").innerHTML = y);\r\n"
                + "            }\r\n"
                + "        </script>\r\n"
                + "    </body>\r\n"
                + "</html>";
    
        return outputLine;
    }
    
    /**
     * Define y aplica el estilo de la pagina a mostrar en pantalla.
     * @return Estilo a añadir al Html para enseñarse en pantalla.
     */
    public static String getCSS() {
        return "<style>\r\n"
                + "    /* Tu estilo CSS personalizado aquí */\r\n"
                + "    font-family: 'Roboto', sans-serif;\r\n"
                + "    body {\r\n"
                + "        margin: 0;\r\n"
                + "        padding: 0;\r\n"
                + "        background-color: #a2a2a2;\r\n"
                + "        display: flex;\r\n"
                + "        justify-content: center;\r\n"
                + "        align-items: center;\r\n"
                + "        flex-direction: column;\r\n"
                + "        height: 100vh;\r\n"
                + "    }\r\n"
                + "    input, button, textarea, img {\r\n"
                + "        border: 2px solid rgba(0, 0, 0, 0.6);\r\n"
                + "        background-image: none;\r\n"
                + "        background-color: #dadad3;\r\n"
                + "        box-shadow: none;\r\n"
                + "        padding: 5px;\r\n"
                + "    }\r\n"
                + "    input:focus, button:focus, textarea:focus {\r\n"
                + "        outline: none;\r\n"
                + "    }\r\n"
                + "    textarea {\r\n"
                + "        min-height: 50px;\r\n"
                + "        resize: vertical;\r\n"
                + "    }\r\n"
                + "    button {\r\n"
                + "        cursor: pointer;\r\n"
                + "        font-weight: 500;\r\n"
                + "    }\r\n"
                + "    .feedback-card {\r\n"
                + "        border: 1px solid black;\r\n"
                + "        max-width: 980px;\r\n"
                + "        background-color: #fff;\r\n"
                + "        margin: 0 auto;\r\n"
                + "        box-shadow: -0.6rem 0.6rem 0 rgba(29, 30, 28, 0.26);\r\n"
                + "    }\r\n"
                + "    .feedback-header {\r\n"
                + "        text-align: center;\r\n"
                + "        padding: 8px;\r\n"
                + "        font-size: 14px;\r\n"
                + "        font-weight: 700;\r\n"
                + "        border-bottom: 1px solid black;\r\n"
                + "    }\r\n"
                + "    .feedback-body {\r\n"
                + "        padding: 20px;\r\n"
                + "        display: flex;\r\n"
                + "        flex-direction: column;\r\n"
                + "    }\r\n"
                + "    .feedback-body__message {\r\n"
                + "        margin-top: 10px;\r\n"
                + "    }\r\n"
                + "    .feedback-body button {\r\n"
                + "        margin-top: 10px;\r\n"
                + "        align-self: flex-end;\r\n"
                + "    }\r\n"
                + "</style>\r\n";

                //
                // Nota por Santiago Rocha.
                // Plantilla tomada de:
                // https://plantillashtmlgratis.com/efectos-css/formularios-de-contacto-css/formulario-de-retroalimentacion-de-interfaz-de-usuario-retro/
                //
    }

    public static String getFormat(String fileName) {
        System.out.println(fileName);
        String format = fileName.split("\\.")[1];
        return format;
    }

    public static String getFiles(String path) {
        // Crear un objeto File con la ruta del archivo
        File archivo = new File(path);
        if (!archivo.exists()) {
            String noContent = "El archivo no existe";
            return noContent;
        } 

        byte[] buffer = new byte[(int) archivo.length()];

        try (FileInputStream fis = new FileInputStream(archivo)) {
            // Leer los datos del archivo en el arreglo de bytes
            fis.read(buffer);

            // Convertir los bytes en una cadena y mostrar el contenido del archivo
            String content = new String(buffer);

            return content;

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }

    public static byte[] getImageBytes(String imagePath) {
        File imageFile = new File(imagePath);
        
        if (!imageFile.exists() || !imageFile.isFile()) {
            return null; // El archivo no existe o no es válido
        }
        
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Error al leer el archivo
        }
    }

    
}