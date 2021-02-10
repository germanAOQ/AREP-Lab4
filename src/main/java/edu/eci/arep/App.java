package edu.eci.arep;

import edu.eci.arep.httpserver.HttpServer;
import edu.eci.arep.httpserver.HttpServerImpl;

import java.io.IOException;

/**
 * Basic HTTP Server With NanoSpark Framework App.
 * Author: Daniel Felipe Walteros Trujillo
 */
public class App {
    /**
     * Main function Of The App.
     *
     * @param args List of special arguments, do not have functionality yet.
     */
    public static void main(String[] args) {
        HttpServer httpServer = new HttpServerImpl();
        try {
            httpServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
