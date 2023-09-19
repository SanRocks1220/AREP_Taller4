package edu.escuelaing.arep;

import org.junit.Test;

import static org.junit.Assert.*;
import java.io.*;

public class HttpServerTest {

    @Test
    public void testGetFormat() {
        String fileName = "example.txt";
        String format = HttpServer.getFormat(fileName);
        assertEquals("txt", format);
    }

    @Test
    public void testInCache() {
        HttpServer.cache.put("file.txt", "Content");
        assertTrue(HttpServer.inCache("file.txt"));
        assertFalse(HttpServer.inCache("nonexistentText.txt"));
    }

    @Test
    public void testNotInCache() {
        assertFalse(HttpServer.inCache("file.txt"));
        HttpServer.cache.put("file.txt", "Content");
        assertTrue(HttpServer.inCache("file.txt"));
    }

    @Test
    public void testGetFileDataNonexistentFile() throws IOException {
        HttpServer.getFileData("/getFileData?name=nonexistent.txt");
    }

    @Test
    public void testGetFormatWithTxtExtension() {
        String fileName = "example.txt";
        String format = HttpServer.getFormat(fileName);
        assertEquals("txt", format);
    }

    @Test
    public void testGetFormatWithJpgExtension() {
        String fileName = "image.jpg";
        String format = HttpServer.getFormat(fileName);
        assertEquals("jpg", format);
    }

    @Test
    public void testGetFormatWithNoExtension() {
        String fileName = "fileWithoutExtension";
        String format = HttpServer.getFormat(fileName);
        assertEquals("NoFormat", format);
    }

    @Test
    public void testGetFormatWithMultipleDots() {
        String fileName = "document.v1.2.docx";
        String format = HttpServer.getFormat(fileName);
        assertEquals("v1", format);
    }

    @Test
    public void testHelloFormer() {
        // Prueba el método helloFormer con un valor de entrada y verifica si la respuesta contiene el saludo esperado.
        String input = "John";
        String expectedResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<!DOCTYPE html>\r\n" +
                "<html>\r\n" +
                "    <head>\r\n" +
                "        <meta charset=\"UTF-8\">\r\n" +
                "        <title>Salutation</title>\r\n" +
                "    </head>\r\n" +
                "    <body>\r\n" +
                "         <h1> Hello John</h1>\r\n" +
                "    </body>\r\n" +
                "</html>";
        
        String response = HttpServer.helloFormer(input);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetFileDataNotFound() {
        // Prueba el método getFileData con una URL de archivo que no existe y verifica si la respuesta indica que el archivo no existe.
        String input = "/nonexistent.txt";
        String expectedResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<!DOCTYPE html>\r\n" +
                "<html>\r\n" +
                "    <head>\r\n" +
                "        <meta charset=\"UTF-8\">\r\n" +
                "        <title>File Content</title>\r\n" +
                "    </head>\r\n" +
                "    <body>\r\n" +
                "<pre>El archivo no existe</pre>\r\n" +
                "    </body>\r\n" +
                "</html>";

        String response = HttpServer.getFileData(input);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testHelloFormerEmptyInput() {
        // Prueba el método helloFormer con una entrada vacía y verifica si la respuesta contiene el saludo esperado.
        String input = "";
        String expectedResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<!DOCTYPE html>\r\n" +
                "<html>\r\n" +
                "    <head>\r\n" +
                "        <meta charset=\"UTF-8\">\r\n" +
                "        <title>Salutation</title>\r\n" +
                "    </head>\r\n" +
                "    <body>\r\n" +
                "         <h1> Hello </h1>\r\n" +
                "    </body>\r\n" +
                "</html>";

        String response = HttpServer.helloFormer(input);

        assertEquals(expectedResponse, response);
    }

    
}
