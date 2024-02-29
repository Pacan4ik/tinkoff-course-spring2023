package edu.java.bot.utils.url;

public class URLSyntaxException extends Exception {
    public URLSyntaxException(String message) {
        super(message);
    }

    public URLSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public URLSyntaxException(Throwable cause) {
        super(cause);
    }
}
