package edu.java.scrapper.configuration;

import edu.java.scrapper.clients.botClient.BotClient;
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
    BotClient botClient(@Qualifier("botRetryTemplate") RetryTemplate retryTemplate) {
        return new BotClient(webClientBuilder, applicationConfig.bot().baseUrl(), retryTemplate);
    }

}
