package edu.java.clients;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class Client<T> {
    protected Class<T> tClass;
    protected String baseUrl;

    public Client(Class<T> classRef, String baseUrl) {
        this.tClass = classRef;
        this.baseUrl = baseUrl;
    }

    public T fetchResponse(String... userInfo) {
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();

        Mono<T> responseMono =
            webClient.get().uri(getUriString(), (Object[]) userInfo).retrieve().bodyToMono(tClass);
        return responseMono.block();
    }

    protected abstract String getUriString();
}
