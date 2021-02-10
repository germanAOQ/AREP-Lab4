package edu.eci.arep.httpserver;

import edu.eci.arep.nanospark.NanoSpark;
import edu.eci.arep.nanospark.NanoSparkImpl;
import edu.eci.arep.nanospark.components.NanoSparkException;
import edu.eci.arep.nanospark.components.Request;
import edu.eci.arep.nanospark.components.Response;
import edu.eci.arep.persistence.PersistenceService;
import edu.eci.arep.persistence.PersistenceServiceImpl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class That Implements The Basic HttpServer Methods For The App.
 * Author: Daniel Felipe Walteros Trujillo
 */
public class HttpServerImpl implements HttpServer {

    private boolean running;
    private static final String ROUTE_TO_STATIC_FILES = "/src/main/resources/static";
    private ServerSocket serverSocket;
    private OutputStream out;
    private BufferedReader in;
    private PersistenceService persistenceService;

    /**
     * Constructor For The HttpServerImpl Class.
     */
    public HttpServerImpl() {
        super();
        persistenceService = new PersistenceServiceImpl();
    }

    /**
     * Initialize the server to accept requests of the network.
     *
     * @throws IOException When the port of the application is already occupied.
     */
    @Override
    public void startServer() throws IOException {
        NanoSpark nanoSpark = null;
        int port = getPort();
        try {
            serverSocket = new ServerSocket(port);
            nanoSpark = new NanoSparkImpl(this);
            nanoSpark.get("/hello", this::getHelloHandler);
            nanoSpark.get("/", this::redirectToIndexPage);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port + ".");
            System.exit(1);
        }
        startAcceptingRequests(nanoSpark);
    }

    /**
     * Search for Static Files in the web server, for now only searches HTML,PNG and JS.
     *
     * @param endpoint The Url Path of The File To Search.
     */
    @Override
    public void getStaticFiles(String endpoint) {
        String fullPath = ROUTE_TO_STATIC_FILES + endpoint;
        if (endpoint.contains("png")) {
            getImage(fullPath);
        } else if (endpoint.contains("html") || endpoint.contains("js")) {
            getResource(fullPath);
        }
    }

    /**
     * Search for Static Resource Files in the web server, for now only searches HTML and JS.
     *
     * @param fullPath The Url Path of The File To Search.
     */
    @Override
    public void getResource(String fullPath) {
        String type = fullPath.split("\\.")[1];
        if (type.equals("js")) type = "json";
        try {
            in = new BufferedReader(new FileReader(System.getProperty("user.dir") + fullPath));
            String outLine = "";
            String line;
            while ((line = in.readLine()) != null) {
                outLine += line;
            }
            out.write(("HTTP/1.1 201 OK\r\n"
                    + "Content-Type: text/" + type + ";"
                    + "charset=\"UTF-8\" \r\n"
                    + "\r\n"
                    + outLine).getBytes());
        } catch (IOException e) {
            int statusCode = 404;
            printErrorMessage(statusCode,
                    "<!DOCTYPE html>\n"
                            + "<html>\n"
                            + "<head>\n"
                            + "<meta charset=\"UTF-8\">\n"
                            + "<title>" + statusCode + " Error</title>\n"
                            + "</head>\n"
                            + "<body>\n"
                            + "<h1>404 File Not Found</h1>\n"
                            + "</body>\n"
                            + "</html>\n", "Not Found");
        }
    }

    /**
     * Search for Static Image Files in the web server, for now only searches PNG.
     *
     * @param fullPath The Url Path of The File To Search.
     */
    @Override
    public void getImage(String fullPath) {
        String type = fullPath.split("\\.")[1];
        try {
            BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + fullPath));
            ByteArrayOutputStream arrBytes = new ByteArrayOutputStream();
            DataOutputStream outImage = new DataOutputStream(out);
            ImageIO.write(image, type, arrBytes);
            outImage.writeBytes("HTTP/1.1 200 OK \r\n"
                    + "Content-Type: image/" + type + " \r\n"
                    + "\r\n");
            out.write(arrBytes.toByteArray());
        } catch (IOException e) {
            int statusCode = 404;
            printErrorMessage(statusCode,
                    "<!DOCTYPE html>\n"
                            + "<html>\n"
                            + "<head>\n"
                            + "<meta charset=\"UTF-8\">\n"
                            + "<title>" + statusCode + " Error</title>\n"
                            + "</head>\n"
                            + "<body>\n"
                            + "<h1>404 File Not Found</h1>\n"
                            + "</body>\n"
                            + "</html>\n", "Not Found");
        }
    }

    /**
     * Outputs A Error Message In The Web Response.
     *
     * @param statusCode The Error HTTP Status Code.
     * @param message    The Error Message (can be HTML).
     * @param statusName The Name Of The Error.
     */
    @Override
    public void printErrorMessage(int statusCode, String message, String statusName) {
        try {
            out.write(("HTTP/1.1 " + statusCode + " " + statusName + "\r\n"
                    + "\r\n"
                    + message).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the Output Stream Of The HTTP Server.
     *
     * @return The Output Stream Of The HTTP Server.
     */
    @Override
    public OutputStream getOut() {
        return out;
    }

    /**
     * Sets the Output Stream Of The HTTP Server.
     *
     * @param out The New Output Stream Of The HTTP Server.
     */
    @Override
    public void setOut(OutputStream out) {
        this.out = out;
    }

    /**
     * Method that initialize the receiving of Http Requests.
     *
     * @param nanoSpark The NanoSpark Framework for The Endpoints.
     * @throws IOException When The Request Reading Fails.
     */
    private void startAcceptingRequests(NanoSpark nanoSpark) throws IOException {
        running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            receiveRequest(clientSocket, nanoSpark);
        }
        serverSocket.close();
    }

    /**
     * Method that determine what happen when the server receive a request.
     *
     * @param clientSocket The Socket Of The Client Request.
     * @param nanoSpark    The NanoSpark Framework for The Endpoints.
     * @throws IOException When The Request Reading Fails.
     */
    private void receiveRequest(Socket clientSocket, NanoSpark nanoSpark) throws IOException {
        out = clientSocket.getOutputStream();
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        nanoSpark.setOut(out);
        String inputLine;
        String endpoint;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("GET")) {
                endpoint = inputLine.split(" ")[1];
                if (!isSparkEndpoint(endpoint, nanoSpark)) getStaticFiles(endpoint);
            }
            if (!in.ready()) break;
        }
        in.close();
        clientSocket.close();
    }

    /**
     * Determines if a path is a NanoSpark Endpoint Of The App, if it is then calculate the predetermined function of the endpoint.
     *
     * @param endpoint  The path requested to determine if is a NanoSpark Endpoint Of The App.
     * @param nanoSpark The NanoSpark Framework for The Endpoints.
     * @return A boolean value that determines if a path is a NanoSpark Endpoint Of The App.
     */
    private boolean isSparkEndpoint(String endpoint, NanoSpark nanoSpark) {
        boolean isSparkEndpoint = false;
        if (endpoint.equals("/")) {
            nanoSpark.check(endpoint);
            isSparkEndpoint = true;
        }
        if (endpoint.contains("/Apps")) {
            endpoint = endpoint.replace("/Apps", "");
            nanoSpark.check(endpoint);
            isSparkEndpoint = true;
        }
        return isSparkEndpoint;
    }

    /**
     * Gets The Greeting of the /hello NanoSpark Endpoint of The App.
     *
     * @param req The Request of the client.
     * @param res The Response of the server.
     * @return The String Value of The Result (can be HTML).
     */
    private String getHelloHandler(Request req, Response res) {
        String result = null;
        try {
            result = persistenceService.getGreeting(req.queryParams("value"));
        } catch (NanoSparkException e) {
            res.setError("There is no value to hello on the url, (i.e. /hello?value=example)");
        }
        return result;
    }

    /**
     * Redirects tho the index.html page on the / NanoSpark Endpoint of The App.
     *
     * @param req The Request of the client.
     * @param res The Response of the server.
     * @return The String Value of The Result (can be HTML).
     */
    private String redirectToIndexPage(Request req, Response res) {
        res.redirect("/index.html");
        return null;
    }

    /**
     * Get the Port Of The Web Application.
     *
     * @return The value of the port configured in the system environment, returns 35000 by default.
     */
    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
