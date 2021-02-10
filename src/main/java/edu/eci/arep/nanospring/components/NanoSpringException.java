package edu.eci.arep.nanospring.components;

/**
 * Personalized Exception For Nano Spring Framework.
 * Author: Daniel Felipe Walteros Trujillo
 */
public class NanoSpringException extends Exception {
    /**
     * Basic Constructor For NanoSpringException
     *
     * @param msg Error Message Of The Exception
     */
    public NanoSpringException(String msg) {
        super(msg);
    }
}
