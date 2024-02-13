package edu.java.bot.utils.url;

public interface UrlParser {
    ParsedUrl parse(String url) throws URLSyntaxException;
}
