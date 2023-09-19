package edu.escuelaing.arep;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.escuelaing.arep.controller.annotations.Component;
import edu.escuelaing.arep.controller.annotations.RequestMapping;

/**
 * Clase que implementa un servidor HTTP básico para solicitar archivos desde el disco duro.
 * Esta clase permite manejar solicitudes GET y POST.
 * @author Santiago Andres Rocha C.
 */
public class HttpServer {

    public static ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
    static ExecutorService threadPool = Executors.newFixedThreadPool(10); // Se puede ajustar el número de hilos

    public static ServerSocket serverSocket;
    static HttpServer instance = new HttpServer();

    public static PrintWriter out;

    private static Map<String, Method> servicios = new HashMap<String, Method>();
    private final static String root = "edu/escuelaing/arep/controller";

    /**
     * Constructor privado de la clase HttpServer.
     * Utiliza el patrón Singleton para asegurar una sola instancia de HttpServer.
     */
    private HttpServer(){}

    /**
     * Obtiene la única instancia de HttpServer.
     * @return Instancia única de HttpServer.
     */
    public static HttpServer getInstance(){
        return instance;
    }

    /**
     * Obtiene el mapa de servicios registrados en el servidor.
     * @return Un mapa que contiene los servicios registrados con sus rutas como clave.
     */
    public static Map<String, Method> getServicios() {
        return servicios;
    }

    /**
     * Registra un servicio para responder a solicitudes GET.
     * @param url URL del servicio.
     * @param service Servicio que implementa la interfaz ServicioStr.
     */
    public static void get(String url, Method service){
        servicios.put(url, service);
    }

    /**
     * Registra un servicio para responder a solicitudes POST.
     * @param url URL del servicio.
     * @param service Servicio que implementa la interfaz ServicioStr.
     */
    public static void post(String url, Method service){
        servicios.put(url, service);
    }

    /**
     * Busca un servicio registrado para una URL dada.
     * @param resource URL del servicio a buscar.
     * @return Servicio que responde a la URL especificada.
     */
    public static Method buscar(String resource){
        return servicios.get(resource);
    }

    /**
     * Inicia el servidor HTTP.
     * Escucha y maneja solicitudes entrantes en un bucle infinito.
     * @param args Argumentos para la inicialización de la clase.
     * @throws IOException Excepción arrojada en caso de no poder establecer la conexión.
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
     public void run(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

        List<Class<?>> classes = getClasses();
        for (Class<?> clasS:classes){
            if(clasS.isAnnotationPresent(Component.class)){
                Class<?> c = Class.forName(clasS.getName());
                Method[] methods = c.getMethods();
                for (Method method: methods){
                    if(method.isAnnotationPresent(RequestMapping.class)){
                        String key = method.getAnnotation(RequestMapping.class).value();
                        servicios.put(key,method);
                    }
                }
            }
        }


        try {
            serverSocket = new ServerSocket(35000);
            System.out.println("Listo para recibir...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
    }   
    
    /**
     * Detiene el servidor HTTP y cierra el ThreadPool y el socket del servidor.
     * No se utiliza en la versión actual del codigo.
     */
    public static void shutdown() {
        threadPool.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Class<?>> getClasses(){
        List<Class<?>> classes = new ArrayList<>();
        try{
            for (String cp: classPaths()){
                File file = new File(cp + "/" + root);
                if(file.exists() && file.isDirectory()){
                    for (File cf: Objects.requireNonNull(file.listFiles())){
                        if(cf.isFile() && cf.getName().endsWith(".class")){
                            String rootTemp = root.replace("/",".");
                            String className = rootTemp+"."+cf.getName().replace(".class","");
                            Class<?> clasS =  Class.forName(className);
                            classes.add(clasS);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return classes;
    }

    private static ArrayList<String> classPaths(){
        String classPath = System.getProperty("java.class.path");
        String[] classPaths =  classPath.split(System.getProperty("path.separator"));
        return new ArrayList<>(Arrays.asList(classPaths));
    }

    /**
     * Procesa una solicitud HTTP entrante.
     * @param clientSocket Socket destinado a la conexión con el cliente.
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void handleRequest(Socket clientSocket) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String uriString = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                //if(!inputLine.equals("GET /favicon.ico HTTP/1.1"))
                System.out.println("Received: " + inputLine);
                if (inputLine.startsWith("GET")) {
                    uriString = inputLine.split(" ")[1];
                    break;
                } else if (inputLine.startsWith("POST")) {
                    uriString = inputLine.split(" ")[1];
                    break;
                }
            }

            String outputLine = "";
            if(!uriString.equals("/favicon.ico") && !uriString.equals("/")){
                String[] uriParts = uriString.split("\\?");
                String serviceToUse = uriParts[0];

                System.out.println(serviceToUse);

                //String strToGive = uriParts[1].split("=")[1];
                System.out.println(servicios.containsKey(serviceToUse));
                if(servicios.containsKey(serviceToUse)){
                    outputLine = servicios.get(serviceToUse).invoke(null).toString();
                }
            } else {
                outputLine = indexResponse();
            }
            out.println(outputLine);
            
            out.close();
            in.close();

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el contenido de un archivo desde el disco duro o el cache.
     * @param uriString La URI que indica el recurso a buscar.
     * @return Respuesta HTTP con el contenido del archivo o mensaje de error.
     * @throws IOException Excepción arrojada en caso de no poder establecer la conexión.
     */
    public static String getFileData(String uriString){
        String fileName = uriString;
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
                fileData = getFiles("webAppWithFiles\\src\\main\\resources\\" + fileName);
                saveInCache(fileName, fileData);
            } else {
                byte[] imageBytes = getImageBytes("webAppWithFiles\\src\\main\\resources\\" + fileName);
                fileData = Base64.getEncoder().encodeToString(imageBytes);
                saveInCache(fileName, fileData);
            }
            
        }
        if (img) {
            String response = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    + "<!DOCTYPE html>\r\n"
                    + "<html>\r\n"
                    + "    <head>\r\n"
                    + "        <title>File Content</title>\r\n"
                    + getCSS()
                    + "    </head>\r\n"
                    + "    <body>\r\n"
                    + "         <center><img src=\"data:image/jpeg;base64," + fileData + "\" alt=\"Mi Imagen\"></center>" + "\r\n"
                    + "    </body>\r\n"
                    + "</html>";
            return response;
        }
        String response = "HTTP/1.1 200 OK\r\n"
            + "Content-Type: text/html\r\n"
            + "\r\n"
            + "<!DOCTYPE html>\r\n"
            + "<html>\r\n"
            + "    <head>\r\n"
            + "        <meta charset=\"UTF-8\">\r\n"
            + "        <title>File Content</title>\r\n"
            + getCSS()
            + "    </head>\r\n"
            + "    <body>\r\n"
            + "<pre>" + fileData + "</pre>\r\n"
            + "    </body>\r\n"
            + "</html>";
        return response;
    }

    
    /**
     * Verifica si un archivo ya ha sido consultado previamente y está en caché.
     * @param fileName Nombre del archivo a buscar en caché.
     * @return true si el archivo está en caché, false en caso contrario.
     */
    public static boolean inCache(String fileName) {
        return cache.containsKey(fileName);
    }
    

    
    /**
     * Obtiene los datos de un archivo desde el caché.
     * @param fileName Nombre del archivo de donde tomar los datos en caché.
     * @return Datos completos del archivo solicitado.
     */
    public static String getFromCache(String fileName){
        System.out.println("Ya está en caché, no busca en el Disco Duro");
        return cache.get(fileName);
    }

    
    /**
     * Guarda los datos de un archivo en caché.
     * @param fileName Nombre del archivo a asociar los datos.
     * @param fileData Datos del archivo a guardar asociados a su nombre.
     */
    public static void saveInCache(String fileName, String fileData){
        System.out.println("No está en caché, busca en el Disco Duro");
        cache.put(fileName, fileData);
    }

    /**
     * Obtiene el formato o la extensión de un archivo a partir de su nombre.
     * @param fileName Nombre del archivo que incluye su formato o extensión.
     * @return Formato o extensión del archivo.
     */
    public static String getFormat(String fileName) {
        System.out.println(fileName);
        String format = "";
        try{
            format = fileName.split("\\.")[1];
        } catch(ArrayIndexOutOfBoundsException e) {
            format = "NoFormat";
        }
        
        return format;
    }

    /**
     * Obtiene el contenido de un archivo no imagen desde el disco duro.
     * @param path Ruta y nombre del archivo a buscar.
     * @return Contenido del archivo especificado.
     */
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
    };

    /**
     * Obtiene el contenido de un archivo de imagen desde el disco duro y lo codifica en Base64.
     * @param imagePath Ruta y nombre del archivo de imagen a buscar.
     * @return Contenido del archivo de imagen especificado en formato Base64.
     */
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
            fis.close();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Error al leer el archivo
        }
    }

    /**
     * Genera una respuesta HTTP con un saludo personalizado.
     * @param str Nombre o mensaje a incluir en el saludo.
     * @return Respuesta HTTP en formato HTML con un saludo personalizado.
     */
    public static String helloFormer(String str) {
        return "HTTP/1.1 200 OK\r\n"
        + "Content-Type: text/html\r\n"
        + "\r\n"
        + "<!DOCTYPE html>\r\n"
        + "<html>\r\n"
        + "    <head>\r\n"
        + "        <meta charset=\"UTF-8\">\r\n"
        + "        <title>Salutation</title>\r\n"
        + "    </head>\r\n"
        + "    <body>\r\n"
        + "         <h1> Hello " + str + "</h1>\r\n"
        + "    </body>\r\n"
        + "</html>";
    } 

    
    /**
     * Genera una respuesta HTML con un formulario básico.
     * @return Respuesta HTML que muestra un formulario.
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
                + "                <h1>Pagina Principal</h1>\r\n"
                + "                <h3>Si deseas acceder a funciones o recursos, debes hacerlo desde la URL ;)</h3>\r\n"
                + "            </div>\r\n"
                + "    </body>\r\n"
                + "</html>";
    
        return outputLine;
    }
    
    /**
     * Define y aplica el estilo CSS de la página.
     * @return Estilo CSS a aplicar en la página HTML.
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
}
