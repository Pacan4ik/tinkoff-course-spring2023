package edu.java.bot.api.exceptions;

public class OutOfTokensException extends BotException {
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
