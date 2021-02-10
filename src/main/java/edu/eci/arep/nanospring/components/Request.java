package edu.eci.arep.nanospring.components;

/**
 * Request Class From Nano Spring Framework.
 * Author: Daniel Felipe Walteros Trujillo
 */
public class Request {

    private final String endpoint;
    private final String value;

    /**
     * Basic Constructor For Request
     *
     * @param path Path of the Http Request.
     */
    public Request(String path) {
        int indexOfValues = path.indexOf("?");
        if (indexOfValues < 0) {
            this.endpoint = path;
            this.value = null;
        } else {
            String valuePath = path.substring(indexOfValues + 1);
            int indexOfValue = valuePath.indexOf("=");
            this.endpoint = path.substring(0, indexOfValues);
            this.value = valuePath.substring(indexOfValue + 1);
        }
    }

    /**
     * Returns the Endpoint Value Of The Request.
     *
     * @return A String that represents the Endpoint Value Of The Request.
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Returns the Values on the Endpoint Of The Request.
     *
     * @return A String that represents the Values on the Endpoint Of The Request.
     */
    public String getValue() {
        return value;
    }
}
