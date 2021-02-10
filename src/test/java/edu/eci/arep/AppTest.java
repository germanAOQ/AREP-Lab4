package edu.eci.arep;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Unit Tests For The App.
 */
public class AppTest {

    private final String basicUrl = "http://127.0.0.1:35000";

    @BeforeClass
    public static void setup() {
        new Thread(() -> {
            try {
                String[] args = {};
                App.main(args);
            } catch (ClassNotFoundException e) {
                fail("No debió fallar al iniciar la app");
            }
        }).start();
    }

    @Test
    public void shouldFindTheHTMLFile() {
        try {
            URL url = new URL(basicUrl + "/index.html");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine = bufferedReader.readLine();
            assertTrue(inputLine.contains("<!DOCTYPE html>"));
        } catch (IOException e) {
            fail("No debió fallar al realizar la conexión con esa URL");
        }
    }

    @Test
    public void shouldFindTheJSFile() {
        try {
            URL url = new URL(basicUrl + "/app.js");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine = bufferedReader.readLine();
            assertTrue(inputLine.contains("document.getElementById(\"button\")"));
        } catch (IOException e) {
            fail("No debió fallar al realizar la conexión con esa URL");
        }
    }

    @Test
    public void shouldFindThePNGFile() {
        try {
            URL url = new URL(basicUrl + "/Logo.png");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine = bufferedReader.readLine();
            assertTrue(inputLine.contains("PNG"));
        } catch (IOException e) {
            fail("No debió fallar al realizar la conexión con esa URL");
        }
    }

    @Test
    public void shouldNotFindTheFile() {
        URL url = null;
        try {
            url = new URL(basicUrl + "/hola.html");
        } catch (MalformedURLException e) {
            fail("No debió fallar al crear la URL");
        }
        try {
            new BufferedReader(new InputStreamReader(url.openStream()));
            fail("Debió fallar al realizar la conexión con esa URL");
        } catch (IOException e) {
            assertEquals(basicUrl + "/hola.html", e.getMessage());
        }
    }

    @Test
    public void shouldFindTheNanoSpringEndpoint() {
        String value = "Daniel";
        try {
            URL url = new URL(basicUrl + "/Nsapps/hello?value=" + value);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine = bufferedReader.readLine();
            assertEquals("Hola " + value + " desde NanoSpring", inputLine);
        } catch (IOException e) {
            fail("No debió fallar al realizar la conexión con esa URL");
        }
    }

    @Test
    public void shouldFailWithAnIncompleteNanoSpringEndpoint() {
        URL url = null;
        try {
            url = new URL(basicUrl + "/Nsapps/hello");
        } catch (MalformedURLException e) {
            fail("No debió fallar al crear la URL");
        }
        try {
            new BufferedReader(new InputStreamReader(url.openStream()));
            fail("Debió fallar al realizar la conexión con esa URL");
        } catch (IOException e) {
            assertEquals("Server returned HTTP response code: 409 for URL: " + basicUrl + "/Nsapps/hello", e.getMessage());
        }
    }
}
