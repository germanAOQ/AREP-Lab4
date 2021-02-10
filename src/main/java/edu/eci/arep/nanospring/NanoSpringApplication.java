package edu.eci.arep.nanospring;

import edu.eci.arep.httpserver.HttpServer;
import edu.eci.arep.httpserver.HttpServerImpl;
import edu.eci.arep.nanospring.components.NanoSpringException;
import edu.eci.arep.nanospring.components.PathVariable;
import edu.eci.arep.nanospring.components.RequestMapping;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * NanoSpring Application Server For The App.
 * Author: Daniel Felipe Walteros Trujillo
 */
public class NanoSpringApplication {
    private static final NanoSpringApplication nanoSpringApplication = new NanoSpringApplication();
    private static boolean componentsLoaded = false;
    private final Map<String, Method> componentsRoute = new HashMap<>();

    /**
     * Private Constructor for NanoSpringApplication
     */
    private NanoSpringApplication() {
        super();
    }

    /**
     * Loads all the components and start the Web Server.
     *
     * @param args List with the components to load in java class format (ex. java.util.WebService)
     * @throws ClassNotFoundException When some of the components does not exists.
     */
    public static void run(String[] args) throws ClassNotFoundException {
        if (!componentsLoaded) {
            nanoSpringApplication.loadComponents(args);
            componentsLoaded = true;
            nanoSpringApplication.startServer();
        }
    }

    /**
     * Starts the Http Server with the list of components.
     */
    private void startServer() {
        HttpServer httpServer = new HttpServerImpl(componentsRoute);
        try {
            httpServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the Components Of The Web App.
     *
     * @param components List with the components to load in java class format (ex. java.util.WebService)
     * @throws ClassNotFoundException When some of the components classpath does not exists.
     */
    private void loadComponents(String[] components) throws ClassNotFoundException {
        for (String component : components) {
            Class c = Class.forName(component);
            for (Method method : c.getMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    componentsRoute.put(method.getAnnotation(RequestMapping.class).value(), method);
                }
            }
        }
    }

    /**
     * Runs the function of a method component.
     *
     * @param staticMethod The method to run.
     * @param args         The posible extra parameters required for run the method.
     * @return A string value with the result of the method.
     * @throws NanoSpringException When a method require a not defined parameter.
     */
    public static String invoke(Method staticMethod, String... args) throws NanoSpringException {
        String result = null;
        String argument = null;
        try {
            for (Parameter parameter : staticMethod.getParameters()) {
                if (parameter.isAnnotationPresent(PathVariable.class)) {
                    if (args.length == 0) {
                        throw new NanoSpringException("No Hay Valor definido par el @PathVariable");
                    }
                    argument = args[0];
                }
            }
            if (argument != null) {
                result = staticMethod.invoke(null, argument).toString();
            } else {
                result = staticMethod.invoke(null).toString();
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
}
