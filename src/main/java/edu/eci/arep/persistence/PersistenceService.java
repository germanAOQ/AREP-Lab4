package edu.eci.arep.persistence;

import edu.eci.arep.nanospring.components.NanoSpringException;

/**
 * Interface with basic DBConnection Method For The App.
 * Author: Daniel Felipe Walteros Trujillo
 */
public interface PersistenceService {
    /**
     * Get a greeting for the person with the name.
     *
     * @param name The name of the person.
     * @return A greeting for the person.
     * @throws NanoSpringException When the DB Connection Fails.
     */
    String getGreeting(String name) throws NanoSpringException;
}
