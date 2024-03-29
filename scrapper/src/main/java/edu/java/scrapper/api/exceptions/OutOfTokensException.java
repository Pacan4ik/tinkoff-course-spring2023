package edu.java.scrapper.api.exceptions;

public class OutOfTokensException extends ScrapperException {

    public OutOfTokensException() {
        super();
    }

    public OutOfTokensException(String message) {
        super(message);
    }

    public OutOfTokensException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutOfTokensException(Throwable cause) {
        super(cause);
    }
}
