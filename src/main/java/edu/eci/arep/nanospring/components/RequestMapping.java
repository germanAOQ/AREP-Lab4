package edu.eci.arep.nanospring.components;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

/**
 * Annotation that represents a method for an endpoint inside a request on the http server.
 * Author: Daniel Felipe Walteros Trujillo
 */
public @interface RequestMapping {
    /**
     * The path value of the endpoint.
     *
     * @return The value of the endpoint.
     */
    String value();
}
