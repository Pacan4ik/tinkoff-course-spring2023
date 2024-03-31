package edu.java.bot.configuration;

import edu.java.bot.scrapperClient.ScrapperClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    ScrapperClient scrapperClient() {
        return new ScrapperClient(webClientBuilder, applicationConfig.scrapper().baseUrl());
    }
}
