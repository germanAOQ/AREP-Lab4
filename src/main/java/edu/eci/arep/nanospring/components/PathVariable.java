package edu.eci.arep.nanospring.components;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)

/**
 * Annotation that represents a path variable inside a request on the http server.
 * Author: Daniel Felipe Walteros Trujillo
 */
public @interface PathVariable {
}
