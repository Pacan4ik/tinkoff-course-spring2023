package edu.java.bot.utils.url;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.stereotype.Component;

@Component
public class SimpleUrlParser implements UrlParser {
    @Override
    public ParsedUrl parse(String urlString) throws URLSyntaxException {
        ParsedUrl parsedUrl = null;
        try {
            URL url = new URI(urlString).toURL();
            parsedUrl = new ParsedUrl(
                url.getProtocol(),
                url.getHost(),
                url.getPort() == -1 ? null : Integer.toString(url.getPort()),
                url.getPath().isEmpty() || url.getPath().equals("/") ? null : url.getPath(),
                url.getQuery()
            );
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            throw new URLSyntaxException("Invalid URL", e);
        }
        return parsedUrl;
    }
}
