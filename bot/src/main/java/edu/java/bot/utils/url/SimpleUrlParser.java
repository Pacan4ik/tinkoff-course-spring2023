package edu.java.bot.utils.url;

import org.springframework.stereotype.Component;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
                Integer.toString(url.getPort()),
                url.getPath(),
                url.getQuery()
            );
        } catch (URISyntaxException | MalformedURLException e) {
            throw new URLSyntaxException("Invalid URL", e);
        }
        return parsedUrl;
    }
}
