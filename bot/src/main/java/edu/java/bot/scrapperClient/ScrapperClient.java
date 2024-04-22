package edu.java.bot.scrapperClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.bot.scrapperClient.model.LinkResponse;
import edu.java.bot.scrapperClient.model.ListLinksResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class ScrapperClient {
    private static final String DEFAULT_BASE_URL = "http://localhost:8090/";
    private static final String TG_CHAT_URI_STRING = "/tg-chat/{id}";
    private static final String LINKS_URI_STRING = "/links";
    private static final String LINKS_CHAT_ID_HEADER = "Tg-Chat-Id";

    private final String baseUrl;

    private final WebClient webClient;

    public ScrapperClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.baseUrl = baseUrl;
        this.webClient = configureClient(webClientBuilder);
    }

    public ScrapperClient(WebClient.Builder webClientBuilder) {
        this(webClientBuilder, DEFAULT_BASE_URL);
    }

    private WebClient configureClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(baseUrl).build();
    }

    public ResponseEntity<Void> registerChat(Long chatId) {
        return webClient.post()
            .uri(TG_CHAT_URI_STRING, chatId)
            .retrieve()
            .toEntity(Void.class)
            .block();
    }

    public ResponseEntity<Void> deleteChat(Long chatId) {
        return webClient.delete()
            .uri(TG_CHAT_URI_STRING, chatId)
            .retrieve()
            .toEntity(Void.class)
            .block();
    }

    public ResponseEntity<LinkResponse> addTrackingLink(Long chatId, String link) {
        return webClient.post()
            .uri(LINKS_URI_STRING)
            .header(LINKS_CHAT_ID_HEADER, Long.toString(chatId))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new LinkRequest(link)))
            .retrieve()
            .toEntity(LinkResponse.class)
            .block();
    }

    public ResponseEntity<ListLinksResponse> getTrackingLinks(Long chatId) {
        return webClient.get()
            .uri(LINKS_URI_STRING)
            .header(LINKS_CHAT_ID_HEADER, Long.toString(chatId))
            .retrieve()
            .toEntity(ListLinksResponse.class)
            .block();
    }

    public ResponseEntity<LinkResponse> deleteLink(Long chatId, String link) {
        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS_URI_STRING)
            .header(LINKS_CHAT_ID_HEADER, Long.toString(chatId))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new LinkRequest(link)))
            .retrieve()
            .toEntity(LinkResponse.class)
            .block();
    }

    private record LinkRequest(@JsonProperty("link") String link) {

    }
}
