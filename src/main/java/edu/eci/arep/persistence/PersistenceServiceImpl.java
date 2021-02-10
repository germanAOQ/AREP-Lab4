package edu.eci.arep.persistence;

import edu.eci.arep.nanospark.components.NanoSparkException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Class that Implements the basic DBConnection Method For The App.
 * Author: Daniel Felipe Walteros Trujillo
 */
public class PersistenceServiceImpl implements PersistenceService {

    private final String firebaseURL = "https://arep-lab3-default-rtdb.firebaseio.com/";

    /**
     * Get a greeting for the person with the name.
     *
     * @param name The name of the person.
     * @return A greeting for the person.
     * @throws NanoSparkException When the DB Connection Fails.
     */
    @Override
    public String getGreeting(String name) throws NanoSparkException {
        String greeting;
        try {
            URL url = new URL(firebaseURL + "greetings/hello.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            greeting = bufferedReader.readLine();
        } catch (IOException e) {
            throw new NanoSparkException("Error de Conexion A Firebase");
        }
        greeting = greeting.replace("\"", "");
        return greeting + " " + name;
    }
}
