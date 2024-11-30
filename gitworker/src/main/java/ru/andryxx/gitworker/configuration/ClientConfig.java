package ru.andryxx.gitworker.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import ru.andryxx.gitworker.client.GitHubClient;

@Configuration
public class ClientConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_S = "token %s";
    private final ApplicationConfig applicationConfig;
    private final WebClient.Builder webClientBuilder;

    public ClientConfig(ApplicationConfig applicationConfig, WebClient.Builder webClientBuilder) {
        this.applicationConfig = applicationConfig;
        this.webClientBuilder = setAuthorization(webClientBuilder);
    }

    @Bean GitHubClient gitHubClient(@Qualifier("githubRetryTemplate") RetryTemplate retryTemplate) {
        return new GitHubClient(webClientBuilder, applicationConfig.github().baseUrl(), retryTemplate);
    }

    private WebClient.Builder setAuthorization(WebClient.Builder builder) {
        if (applicationConfig.github().token() == null) {
            return builder;
        }
        return builder.defaultHeader(AUTHORIZATION, String.format(TOKEN_S, applicationConfig.github().token()));
    }
}
