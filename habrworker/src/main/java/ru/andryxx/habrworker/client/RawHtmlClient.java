package ru.andryxx.habrworker.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@EnableRetry
@Slf4j
public class RawHtmlClient {
    protected WebClient webClient;
    protected RetryTemplate retryTemplate;

    public RawHtmlClient(WebClient.Builder webClientBuilder, RetryTemplate retryTemplate) {
        this.webClient = webClientBuilder.build();
        this.retryTemplate = retryTemplate;
    }

    public RawHtmlClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
        this.retryTemplate = new RetryTemplateBuilder().customPolicy(new NeverRetryPolicy()).build();
    }

    public String fetchRawHtmlResponse(String path) {
        return retryTemplate.execute(
            (RetryCallback<String, WebClientResponseException>) context -> getRequest(path).block()
        );

    }

    protected Mono<String> getRequest(String path) {
        return webClient.get()
            .uri(path)
            .retrieve()
            .bodyToMono(String.class);
    }

}
