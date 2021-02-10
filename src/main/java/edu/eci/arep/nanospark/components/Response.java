package edu.eci.arep.nanospark.components;

import edu.eci.arep.httpserver.HttpServer;

/**
 * Class That Represents A Server Response On The NanoSpark Framework.
 * Author: Daniel Felipe Walteros Trujillo
 */
public class Response {
    private HttpServer httpServer;
    private String path;
    private int responseStatus;

    /**
     * Basic Constructor For A Response On The NanoSpark Framework.
     *
     * @param httpServer The HTTP Server Object.
     * @param path       The path to search by the user.
     */
    public Response(HttpServer httpServer, String path) {
        this.httpServer = httpServer;
        this.path = path;
    }

    /**
     * Returns The HTTP Server Of The Response.
     *
     * @return The HTTP Server Of The Response.
     */
    public HttpServer getHttpServer() {
        return httpServer;
    }

    /**
     * Sets The HTTP Server Of The Response.
     *
     * @param httpServer The New HTTP Server Of The Response.
     */
    public void setHttpServer(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    /**
     * Returns The Path Of The Response.
     *
     * @return The Path Of The Response.
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets The Path Of The Response.
     *
     * @param path The New Path Of The Response.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Returns The HTTP Response Status Code Of The Response.
     *
     * @return The HTTP Response Status Code Of The Response.
     */
    public int getResponseStatus() {
        return responseStatus;
    }

    /**
     * Sets The HTTP Response Status Code Of The Response.
     *
     * @param responseStatus The New HTTP Response Status Code Of The Response.
     */
    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    /**
     * Redirect To A Static File Based On The Path.
     *
     * @param path The Path Of A Static File.
     */
    public void redirect(String path) {
        httpServer.getStaticFiles(path);
    }

    /**
     * Sets A Error Response With HTTP Status Code 409.
     *
     * @param message A Error Message (can ve HTML).
     */
    public void setError(String message) {
        setResponseStatus(409);
        httpServer.printErrorMessage(409, message, "Conflict");
    }
}
