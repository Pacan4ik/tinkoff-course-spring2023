package ru.andryxx.habrworker.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import ru.andryxx.habrworker.client.RawHtmlClient;

@Configuration
public class ClientConfig {
    private final WebClient.Builder webClientBuilder;

    public ClientConfig(WebClient.Builder webClientBuilder) {
        HttpClient httpClient = HttpClient.create().wiretap(true).followRedirect(true);
        this.webClientBuilder = webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient));
    }

    @Bean
    public RawHtmlClient rawHtmlClient(@Qualifier("rawHtmlRetryTemplate") RetryTemplate retryTemplate) {
        return new RawHtmlClient(webClientBuilder, retryTemplate);
    }

}
