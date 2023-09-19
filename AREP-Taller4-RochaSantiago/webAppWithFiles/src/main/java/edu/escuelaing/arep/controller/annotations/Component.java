package edu.escuelaing.arep.controller.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Clase de anotación que marca una clase como un componente gestionado por el sistema.
 * Las clases marcadas con esta anotación pueden ser detectadas y gestionadas automáticamente
 * por mecanismos como la inyección de dependencias.
 * @Author Santiago Andrés Rocha
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
}