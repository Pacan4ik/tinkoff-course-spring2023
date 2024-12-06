package ru.andryxx.gitworker.client;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@EnableRetry
@Slf4j
public abstract class Client<T> {
    protected Class<T> tClass;
    protected String baseUrl;
    protected WebClient webClient;
    protected RetryTemplate retryTemplate;

    public Client(WebClient.Builder webClientBuilder, Class<T> classRef, String baseUrl, RetryTemplate retryTemplate) {
        this.tClass = classRef;
        this.baseUrl = baseUrl;
        this.webClient = configureClient(webClientBuilder);
        this.retryTemplate = retryTemplate;
    }

    public Client(WebClient.Builder webClientBuilder, Class<T> classRef, String baseUrl) {
        this.tClass = classRef;
        this.baseUrl = baseUrl;
        this.webClient = configureClient(webClientBuilder);
        this.retryTemplate = new RetryTemplateBuilder().customPolicy(new NeverRetryPolicy()).build();
    }

    protected T fetchResponse(String... userInfo) {
        return retryTemplate.execute(
            (RetryCallback<T, WebClientResponseException>) context -> getRequest(userInfo).block()
        );

    }

    protected Mono<T> getRequest(Object[] userInfo) {
        return webClient.get()
            .uri(getUriString(), userInfo)
            .retrieve()
            .bodyToMono(tClass);
    }

    protected List<T> fetchListResponse(String... userInfo) {
        return retryTemplate.execute(
            (RetryCallback<List<T>, WebClientResponseException>) context -> getListRequest(userInfo).block()
        );
    }

    protected Mono<List<T>> getListRequest(Object[] userInfo) {
        return webClient.get()
            .uri(getUriString(), userInfo)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<T>>() {});
    }

    protected ResponseEntity<T> send(Object body, String... userInfo) {
        return retryTemplate.execute(
            (RetryCallback<ResponseEntity<T>, WebClientResponseException>) context -> postRequest(
                body,
                userInfo
            ).block()
        );
    }

    protected Mono<ResponseEntity<T>> postRequest(Object body, Object[] userInfo) {
        return webClient.post()
            .uri(getUriString(), userInfo)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(body))
            .retrieve()
            .toEntity(tClass);
    }

    private WebClient configureClient(WebClient.Builder builder) {
        return builder.baseUrl(baseUrl).build();
    }

    protected abstract String getUriString();
}
