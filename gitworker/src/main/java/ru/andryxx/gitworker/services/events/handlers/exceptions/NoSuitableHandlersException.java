package ru.andryxx.gitworker.services.events.handlers.exceptions;

public class NoSuitableHandlersException extends Exception {
    public NoSuitableHandlersException() {
        super();
    }

    public NoSuitableHandlersException(String message) {
        super(message);
    }

    public NoSuitableHandlersException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuitableHandlersException(Throwable cause) {
        super(cause);
    }

}
