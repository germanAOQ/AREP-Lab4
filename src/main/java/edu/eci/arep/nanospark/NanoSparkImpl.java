package edu.eci.arep.nanospark;

import edu.eci.arep.httpserver.HttpServer;
import edu.eci.arep.nanospark.components.Request;
import edu.eci.arep.nanospark.components.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.function.BiFunction;

/**
 * Class That Implements The Minimum Spark Methods For The App.
 * Author: Daniel Felipe Walteros Trujillo
 */
public class NanoSparkImpl implements NanoSpark {

    private final HttpServer httpServer;
    private OutputStream out;
    private final HashMap<String, BiFunction<Request, Response, String>> endpoints;

    /**
     * Builds A NanoSpark Framework Based On A Http Server.
     *
     * @param httpServer The HTTP Server Running.
     */
    public NanoSparkImpl(HttpServer httpServer) {
        this.httpServer = httpServer;
        this.out = httpServer.getOut();
        this.endpoints = new HashMap<>();
    }

    /**
     * Execute the function of a given path.
     *
     * @param path     The Path Received On The HTTP Request.
     * @param function The function determined to run on a specific path.
     */
    @Override
    public void execute(String path, BiFunction<Request, Response, String> function) {
        Request request = new Request(path);
        Response response = new Response(httpServer, path);
        try {
            String result = function.apply(request, response);
            if (result != null) {
                out.write(("HTTP/1.1 200 OK\r\n"
                        + "\r\n"
                        + result).getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a New Endpoint And The Functionality Of The Same.
     *
     * @param endpoint New Path On The Server.
     * @param function New Function That Executes When A Client Access The Path.
     */
    @Override
    public void get(String endpoint, BiFunction<Request, Response, String> function) {
        endpoints.put(endpoint, function);
    }

    /**
     * Sets the Output Stream Of The NanoSpark Framework.
     *
     * @param out The New Output Stream Of The NanoSpark Framework.
     */
    @Override
    public void setOut(OutputStream out) {
        this.out = out;
    }

    /**
     * Returns the Output Stream Of The NanoSpark Framework.
     *
     * @return The Output Stream Of The NanoSpark Framework.
     */
    @Override
    public OutputStream getOut() {
        return out;
    }

    /**
     * Check if A Given Path Is On The Endpoints Defined.
     *
     * @param path The Path Received On The HTTP Request.
     */
    @Override
    public void check(String path) {
        int indexOfValue = path.indexOf("?");
        endpoints.forEach((k, v) -> {
            if (indexOfValue < 0 && path.equals(k)) {
                execute(path, v);
            } else if (indexOfValue >= 0) {
                String pathWithOutValues = path.substring(0, indexOfValue);
                if (pathWithOutValues.equals(k)) {
                    execute(path, v);
                }
            }
        });
    }
}
