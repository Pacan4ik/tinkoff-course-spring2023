package edu.java.bot.api.exceptions;

public class BotException extends RuntimeException {
    public BotException() {
        super();
    }

    public BotException(String message) {
        super(message);
    }

    public BotException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotException(Throwable cause) {
        super(cause);
    }
}
