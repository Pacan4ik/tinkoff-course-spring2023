package edu.java.scrapper.botClient;

import java.net.URI;
import java.util.Collection;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class BotClient {
    private static final String DEFAULT_BASE_URL = "http://localhost:8090/";

    private final String baseUrl;

    private final WebClient webClient;

    public BotClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.baseUrl = baseUrl;
        this.webClient = configureClient(webClientBuilder);
    }

    public BotClient(WebClient.Builder webClientBuilder) {
        this(webClientBuilder, DEFAULT_BASE_URL);
    }

    private WebClient configureClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(baseUrl).build();
    }

    public ResponseEntity<Void> sendUpdates(Long id, URI url, String description, Collection<Long> tgChatIds) {
        return webClient.post()
            .uri("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new BotUpdatesRequest(id, url, description, tgChatIds)))
            .retrieve()
            .toEntity(Void.class)
            .block();
    }
}
