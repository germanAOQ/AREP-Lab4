package edu.eci.arep.nanospark.components;

/**
 * Personalized Exception For The NanoSpark Framework.
 * Author: Daniel Felipe Walteros Trujillo
 */
public class NanoSparkException extends Exception {
    /**
     * Basic Constructor For NanoSparkException
     *
     * @param msg Error Message Of The Exception
     */
    public NanoSparkException(String msg) {
        super(msg);
    }
}
