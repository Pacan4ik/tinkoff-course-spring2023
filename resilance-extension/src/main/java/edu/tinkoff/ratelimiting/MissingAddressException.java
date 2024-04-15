package edu.tinkoff.ratelimiting;

public class MissingAddressException extends RuntimeException {
    public MissingAddressException() {
        super();
    }

    public MissingAddressException(String message) {
        super(message);
    }

    public MissingAddressException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingAddressException(Throwable cause) {
        super(cause);
    }
}
