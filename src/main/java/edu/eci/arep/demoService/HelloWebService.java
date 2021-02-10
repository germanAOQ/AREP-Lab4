package edu.eci.arep.demoService;

import edu.eci.arep.nanospring.components.NanoSpringException;
import edu.eci.arep.nanospring.components.PathVariable;
import edu.eci.arep.nanospring.components.RequestMapping;
import edu.eci.arep.persistence.PersistenceService;
import edu.eci.arep.persistence.PersistenceServiceImpl;

/**
 * Basic Service For Test The Nano Spring Framework.
 * Author: Daniel Felipe Walteros Trujillo
 */
public class HelloWebService {

    private static final PersistenceService persistenceService = new PersistenceServiceImpl();

    /**
     * Get a greeting from the App.
     *
     * @param value Your name for a personalized greeting.
     * @return A string representing the greeting from the App.
     * @throws NanoSpringException When the DB Connection Fails.
     */
    @RequestMapping(value = "/hello")
    public static String index(@PathVariable String value) throws NanoSpringException {
        return persistenceService.getGreeting(value) + " desde NanoSpring";
    }
}
