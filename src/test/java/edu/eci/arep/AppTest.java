package edu.eci.arep;

/**
 * Unit Tests For The App.
 */
public class AppTest {

    private final String basicUrl = "http://127.0.0.1:35000";

    /*@BeforeClass
    public static void setup() {
        new Thread(() -> App.main(null)).start();
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
    public void shouldFindTheNanoSparkEndpoint() {
        String value = "Daniel";
        try {
            URL url = new URL(basicUrl + "/Apps/hello?value=" + value);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine = bufferedReader.readLine();
            assertEquals("Hola " + value, inputLine);
        } catch (IOException e) {
            fail("No debió fallar al realizar la conexión con esa URL");
        }
    }

    @Test
    public void shouldFailWithAnIncompleteNanoSparkEndpoint() {
        URL url = null;
        try {
            url = new URL(basicUrl + "/Apps/hello");
        } catch (MalformedURLException e) {
            fail("No debió fallar al crear la URL");
        }
        try {
            new BufferedReader(new InputStreamReader(url.openStream()));
            fail("Debió fallar al realizar la conexión con esa URL");
        } catch (IOException e) {
            assertEquals("Server returned HTTP response code: 409 for URL: " + basicUrl + "/Apps/hello", e.getMessage());
        }
    }*/
}
