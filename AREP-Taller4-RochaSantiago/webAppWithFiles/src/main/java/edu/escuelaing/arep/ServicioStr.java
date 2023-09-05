package edu.escuelaing.arep;

/**
 * La interfaz ServicioStr define un contrato para los servicios que pueden manejar cadenas de texto y devolver una respuesta.
 * Las clases que implementan esta interfaz deben proporcionar una implementación para el método "handle".
 * @author Santiago Andres Rocha C.
 */
public interface ServicioStr {
    
    /**
     * Maneja una cadena de texto y devuelve una respuesta.
     * @param str Cadena de texto a ser procesada por el servicio.
     * @return Respuesta generada por el servicio como una cadena de texto.
     */
    public String handle(String str);
}
