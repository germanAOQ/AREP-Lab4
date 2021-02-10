package edu.eci.arep.nanospark.components;

import java.util.HashMap;
import java.util.Map;

/**
 * Class That Represents A User Request On The NanoSpark Framework.
 * Author: Daniel Felipe Walteros Trujillo
 */
public class Request {
    private final HashMap<String, String> valuesHashMap;

    /**
     * Basic Constructor For A Request On The NanoSpark Framework.
     *
     * @param path The path to search by the user.
     */
    public Request(String path) {
        valuesHashMap = new HashMap<>();
        int indexOfValue = path.indexOf("?");
        if (indexOfValue >= 0) {
            String values = path.substring(indexOfValue + 1);
            setValues(values);
        }
    }

    /**
     * Sets the values of the path.
     *
     * @param values A string that represents the values part of the path (ex. value=1&order=2).
     */
    private void setValues(String values) {
        String[] valuesList = values.split("%26");
        for (String value : valuesList) {
            String[] valuePair = value.split("=");
            valuesHashMap.put(valuePair[0], valuePair[1]);
        }
    }

    /**
     * Returns All The Values On The Request.
     *
     * @return A Map Structure That represents the name and the value of each parameter.
     */
    public Map<String, String> queryParams() {
        return valuesHashMap;
    }

    /**
     * Returns One Value On The Request.
     *
     * @param key The Name Of The Value To Search.
     * @return The Value Of The Given Key.
     * @throws NanoSparkException When the key does not exist on the path.
     */
    public String queryParams(String key) throws NanoSparkException {
        if (valuesHashMap.isEmpty()) {
            throw new NanoSparkException("No hay valores asignados");
        }
        if (!valuesHashMap.containsKey(key)) {
            throw new NanoSparkException("No existe valor con el nombre " + key);
        }
        return valuesHashMap.get(key);
    }
}
