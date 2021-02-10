package edu.eci.arep.nanospark;

import edu.eci.arep.nanospark.components.Request;
import edu.eci.arep.nanospark.components.Response;

import java.io.OutputStream;
import java.util.function.BiFunction;

/**
 * Interface with the minimum Spark Methods For The App.
 * Author: Daniel Felipe Walteros Trujillo
 */
public interface NanoSpark {
    /**
     * Execute the function of a given path.
     *
     * @param path     The Path Received On The HTTP Request.
     * @param function The function determined to run on a specific path.
     */
    void execute(String path, BiFunction<Request, Response, String> function);

    /**
     * Sets a New Endpoint And The Functionality Of The Same.
     *
     * @param endpoint New Path On The Server.
     * @param function New Function That Executes When A Client Access The Path.
     */
    void get(String endpoint, BiFunction<Request, Response, String> function);

    /**
     * Sets the Output Stream Of The NanoSpark Framework.
     *
     * @param out The New Output Stream Of The NanoSpark Framework.
     */
    void setOut(OutputStream out);

    /**
     * Returns the Output Stream Of The NanoSpark Framework.
     *
     * @return The Output Stream Of The NanoSpark Framework.
     */
    OutputStream getOut();

    /**
     * Check if A Given Path Is On The Endpoints Defined.
     *
     * @param path The Path Received On The HTTP Request.
     */
    void check(String path);
}
