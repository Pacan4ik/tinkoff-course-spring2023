package edu.java.bot.utils.url;

public record ParsedUrl(
    String protocol,
    String host,
    String port,
    String path,
    String query
) {
}
