package edu.java.scrapper.configuration;

import edu.java.scrapper.botClient.BotClient;
import edu.java.scrapper.clients.github.GitHubClient;
import edu.java.scrapper.clients.stackoverflow.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientsConfig {
    ApplicationConfig applicationConfig;

    WebClient.Builder webClientBuilder;

    ClientsConfig(ApplicationConfig applicationConfig, WebClient.Builder webClientBuilder) {
        this.applicationConfig = applicationConfig;
        this.webClientBuilder = webClientBuilder;
    }

    @Bean
    GitHubClient gitHubClient() {
        return new GitHubClient(webClientBuilder, applicationConfig.baseUrls().gitHubApi());
    }

    @Bean
    StackOverflowClient stackOverFlowClient() {
        return new StackOverflowClient(webClientBuilder, applicationConfig.baseUrls().stackOverflowApi());
    }

    @Bean BotClient botClient() {
        return new BotClient(webClientBuilder, applicationConfig.baseUrls().botApi());
    }
}
