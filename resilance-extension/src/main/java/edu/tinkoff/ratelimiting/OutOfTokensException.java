package edu.tinkoff.ratelimiting;

public class OutOfTokensException extends RuntimeException {
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
