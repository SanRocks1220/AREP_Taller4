/**
 * Clase de anotación que se utiliza para marcar métodos o clases en Java como manejadores de solicitudes (request handlers).
 * Esta anotación permite asociar una URL o patrón de solicitud específico a un método o clase que manejará esa solicitud.
 * @Author Santiago Andrés Rocha
 */
package edu.escuelaing.arep.controller.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value();
}