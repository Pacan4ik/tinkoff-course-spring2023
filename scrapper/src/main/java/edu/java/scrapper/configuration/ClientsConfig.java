package edu.java.scrapper.configuration;

import edu.java.scrapper.clients.botClient.BotClient;
import edu.java.scrapper.clients.github.GitHubClient;
import edu.java.scrapper.clients.stackoverflow.StackOverflowClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientsConfig {
    private final ApplicationConfig applicationConfig;
    private final WebClient.Builder webClientBuilder;

    ClientsConfig(ApplicationConfig applicationConfig, WebClient.Builder webClientBuilder) {
        this.applicationConfig = applicationConfig;
        this.webClientBuilder = webClientBuilder;
    }

    @Bean
    GitHubClient gitHubClient(@Qualifier("githubRetryTemplate") RetryTemplate retryTemplate) {
        return new GitHubClient(webClientBuilder, applicationConfig.github().baseUrl(), retryTemplate);
    }

    @Bean
    StackOverflowClient stackOverFlowClient(@Qualifier("stackoverflowRetryTemplate") RetryTemplate retryTemplate) {
        return new StackOverflowClient(webClientBuilder, applicationConfig.stackoverflow().baseUrl(), retryTemplate);
    }

    @Bean
    BotClient botClient(@Qualifier("botRetryTemplate") RetryTemplate retryTemplate) {
        return new BotClient(webClientBuilder, applicationConfig.bot().baseUrl(), retryTemplate);
    }

}
