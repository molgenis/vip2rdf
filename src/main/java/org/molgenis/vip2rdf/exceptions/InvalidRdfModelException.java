package org.molgenis.vip2rdf.exceptions;

public class InvalidRdfModelException extends Exception {
    public InvalidRdfModelException() {
    }

    public InvalidRdfModelException(String message) {
        super(message);
    }

    public InvalidRdfModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRdfModelException(Throwable cause) {
        super(cause);
    }
}
