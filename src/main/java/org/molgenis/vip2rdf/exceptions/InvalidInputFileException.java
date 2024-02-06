package org.molgenis.vip2rdf.exceptions;

import java.io.IOException;

public class InvalidInputFileException extends IOException {
    public InvalidInputFileException() {
    }

    public InvalidInputFileException(String message) {
        super(message);
    }

    public InvalidInputFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputFileException(Throwable cause) {
        super(cause);
    }
}
