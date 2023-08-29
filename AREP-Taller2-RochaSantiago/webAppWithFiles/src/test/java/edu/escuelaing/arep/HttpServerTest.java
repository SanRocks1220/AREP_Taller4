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
    
}
