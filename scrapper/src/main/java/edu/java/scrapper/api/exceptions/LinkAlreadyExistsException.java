package edu.java.scrapper.api.exceptions;

public class LinkAlreadyExistsException extends ScrapperException {
    public LinkAlreadyExistsException() {
        super();
    }

    public LinkAlreadyExistsException(String message) {
        super(message);
    }

    public LinkAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public LinkAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
