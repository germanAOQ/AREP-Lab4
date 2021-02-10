package edu.eci.arep.httpserver;

import edu.eci.arep.nanospring.components.NanoSpringException;
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
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static edu.eci.arep.nanospring.NanoSpringApplication.invoke;

/**
 * Class That Implements The Basic HttpServer Methods For The App.
 * Author: Daniel Felipe Walteros Trujillo
 */
public class HttpServerImpl implements HttpServer {


    private static final String ROUTE_TO_STATIC_FILES = "/src/main/resources/static";
    private final Map<String, Method> componentsRoute;
    private ServerSocket serverSocket;
    private OutputStream out;
    private BufferedReader in;
    private boolean running;

    /**
     * Constructor For The HttpServerImpl Class.
     *
     * @param componentsRoute Map that indicates the paths and method of the NanoSpring Components.
     */
    public HttpServerImpl(Map<String,Method> componentsRoute) {
        super();
        this.componentsRoute = componentsRoute;
    }

    /**
     * Initialize the server to accept requests of the network.
     *
     * @throws IOException When the port of the application is already occupied.
     */
    @Override
    public void startServer() throws IOException {
        int port = getPort();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port + ".");
            System.exit(1);
        }
        startAcceptingRequests();
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
     * @throws IOException When The Request Reading Fails.
     */
    private void startAcceptingRequests() throws IOException {
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
            receiveRequest(clientSocket);
        }
        serverSocket.close();
    }

    /**
     * Method that determine what happen when the server receive a request.
     *
     * @param clientSocket The Socket Of The Client Request.
     * @throws IOException When The Request Reading Fails.
     */
    private void receiveRequest(Socket clientSocket) throws IOException {
        out = clientSocket.getOutputStream();
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        String endpoint;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("GET")) {
                endpoint = inputLine.split(" ")[1];
				if(endpoint.equals("/")){
					getStaticFiles("/index.html");
				} else if (endpoint.contains("/Nsapps")) {
                    endpoint = endpoint.replace("/Nsapps", "");
                    executeSpringEndpoint(endpoint, out);
                } else {
                    getStaticFiles(endpoint);
                }
            }
            if (!in.ready()) break;
        }
        in.close();
        clientSocket.close();
    }

    private void executeSpringEndpoint(String path, OutputStream out) throws IOException {
        Map<String, String> information = getInfoFromEndpoint(path);
        String endpoint = information.get("endpoint");
        String value = information.get("value");
        if (componentsRoute.containsKey(endpoint)) {
            String result;
            try {
                if (value == null) {
                    result = invoke(componentsRoute.get(endpoint));
                } else {
                    result = invoke(componentsRoute.get(endpoint), value);
                }
                out.write(("HTTP/1.1 200 OK\r\n"
                        + "\r\n"
                        + result).getBytes());
            } catch (NanoSpringException e) {
                printErrorMessage(409,
                        "<!DOCTYPE html>\n"
                                + "<html>\n"
                                + "<head>\n"
                                + "<meta charset=\"UTF-8\">\n"
                                + "<title>" + 409 + " Error</title>\n"
                                + "</head>\n"
                                + "<body>\n"
                                + "<h1>" + e.getMessage() + "</h1>\n"
                                + "</body>\n"
                                + "</html>\n", "409 Conflict");
            }
        }
    }

    private Map<String, String> getInfoFromEndpoint(String path) {
        Map<String, String> information = new HashMap<>();
        int indexOfValues = path.indexOf("?");
        if (indexOfValues < 0) {
            information.put("endpoint", path);
            information.put("value", null);
        } else {
            String valuePath = path.substring(indexOfValues + 1);
            int indexOfValue = valuePath.indexOf("=");
            information.put("endpoint", path.substring(0, indexOfValues));
            information.put("value", valuePath.substring(indexOfValue + 1));
        }
        return information;
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
