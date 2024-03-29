package edu.java.scrapper.clients.botClient;

import edu.java.scrapper.clients.Client;
import java.net.URI;
import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;

public class BotClient extends Client<Void> {
    private static final String DEFAULT_BASE_URL = "http://localhost:8090/";
    public static final String URI_STRING = "/updates";
    private static final Class<Void> CLASS_REF = Void.class;

    public BotClient(WebClient.Builder webClientBuilder, String baseUrl, RetryTemplate retryTemplate) {
        super(webClientBuilder, CLASS_REF, baseUrl, retryTemplate);
    }

    public BotClient(WebClient.Builder webClientBuilder, String baseUrl) {
        super(webClientBuilder, CLASS_REF, baseUrl);
    }

    public BotClient(WebClient.Builder webClientBuilder, RetryTemplate retryTemplate) {
        this(webClientBuilder, DEFAULT_BASE_URL, retryTemplate);
    }

    public BotClient(WebClient.Builder webClientBuilder) {
        this(webClientBuilder, DEFAULT_BASE_URL);
    }

    @Override
    protected String getUriString() {
        return URI_STRING;
    }

    public ResponseEntity<Void> sendUpdates(Long id, URI url, String description, Collection<Long> tgChatIds) {
        return send(new BotUpdatesRequest(id, url, description, tgChatIds));
    }
}
