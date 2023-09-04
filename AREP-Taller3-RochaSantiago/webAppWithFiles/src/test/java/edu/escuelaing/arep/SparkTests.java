package edu.escuelaing.arep;

import org.junit.Test;
import static org.junit.Assert.*;

public class SparkTests {

    @Test
    public void testRegisterGetService() {
        ServicioStr mockService = str -> "Mock GET Response";
        HttpServer.get("/testGet", mockService);

        ServicioStr registeredService = HttpServer.getServicios().get("/testGet");

        assertNotNull(registeredService);
        assertEquals(mockService, registeredService);
    }

    @Test
    public void testRegisterPostService() {
        ServicioStr mockService = str -> "Mock POST Response";
        HttpServer.post("/testPost", mockService);

        ServicioStr registeredService = HttpServer.getServicios().get("/testPost");

        assertNotNull(registeredService);
        assertEquals(mockService, registeredService);
    }

    @Test
    public void testBuscarExistingService() {
        ServicioStr mockService = str -> "Mock Response";
        HttpServer.getServicios().put("/testResource", mockService);

        ServicioStr foundService = HttpServer.buscar("/testResource");

        assertNotNull(foundService);
        assertEquals(mockService, foundService);
    }

    @Test
    public void testBuscarNonExistingService() {
        ServicioStr foundService = HttpServer.buscar("/nonExistingResource");

        assertNull(foundService);
    }
}