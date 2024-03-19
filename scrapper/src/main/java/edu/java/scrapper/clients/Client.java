package edu.java.scrapper.clients;

import org.springframework.web.reactive.function.client.WebClient;

public abstract class Client<T> {
    protected Class<T> tClass;
    protected String baseUrl;
    protected WebClient webClient;

    public Client(WebClient.Builder webClientBuilder, Class<T> classRef, String baseUrl) {
        this.tClass = classRef;
        this.baseUrl = baseUrl;
        this.webClient = configureClient(webClientBuilder);
    }

    public T fetchResponse(String... userInfo) {
        return webClient.get()
            .uri(getUriString(), (Object[]) userInfo)
            .retrieve()
            .bodyToMono(tClass)
            .block();
    }

    private WebClient configureClient(WebClient.Builder builder) {
        return builder.baseUrl(baseUrl).build();
    }

    protected abstract String getUriString();
}
