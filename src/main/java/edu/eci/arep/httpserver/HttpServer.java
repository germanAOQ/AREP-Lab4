package edu.eci.arep.httpserver;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface with basic HttpServer Methods For The App.
 * Author: Daniel Felipe Walteros Trujillo
 */
public interface HttpServer {
    /**
     * Initialize the server to accept requests of the network.
     *
     * @throws IOException When the port of the application is already occupied.
     */
    void startServer() throws IOException;

    /**
     * Search for Static Files in the web server, for now only searches HTML,PNG and JS.
     *
     * @param endpoint The Url Path of The File To Search.
     */
    void getStaticFiles(String endpoint);

    /**
     * Search for Static Resource Files in the web server, for now only searches HTML and JS.
     *
     * @param fullPath The Url Path of The File To Search.
     */
    void getResource(String fullPath);

    /**
     * Search for Static Image Files in the web server, for now only searches PNG.
     *
     * @param fullPath The Url Path of The File To Search.
     */
    void getImage(String fullPath);

    /**
     * Outputs A Error Message In The Web Response.
     *
     * @param statusCode The Error HTTP Status Code.
     * @param message    The Error Message (can be HTML).
     * @param statusName The Name Of The Error.
     */
    void printErrorMessage(int statusCode, String message, String statusName);

    /**
     * Returns the Output Stream Of The HTTP Server.
     *
     * @return The Output Stream Of The HTTP Server.
     */
    OutputStream getOut();

    /**
     * Sets the Output Stream Of The HTTP Server.
     *
     * @param out The New Output Stream Of The HTTP Server.
     */
    void setOut(OutputStream out);
}
