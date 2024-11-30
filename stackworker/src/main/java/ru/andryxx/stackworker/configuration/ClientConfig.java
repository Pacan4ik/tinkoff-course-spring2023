package ru.andryxx.stackworker.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import ru.andryxx.stackworker.client.StackOverflowClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    private final ApplicationConfig applicationConfig;
    private final WebClient.Builder webClientBuilder;

    @Bean
    public StackOverflowClient stackOverflowClient(@Qualifier("stackRetryTemplate") RetryTemplate retryTemplate) {
        return new StackOverflowClient(webClientBuilder, applicationConfig.stackoverflow().baseUrl(), retryTemplate);
    }
}
