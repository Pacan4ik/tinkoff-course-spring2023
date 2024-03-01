package edu.java.scrapper.configuration;

import edu.java.scrapper.clients.github.GitHubClient;
import edu.java.scrapper.clients.stackoverflow.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientsConfig {
    ApplicationConfig applicationConfig;

    ClientsConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean
    GitHubClient gitHubClient() {
        return new GitHubClient(applicationConfig.baseUrls().gitHubApi());
    }

    @Bean
    StackOverflowClient stackOverFlowClient() {
        return new StackOverflowClient(applicationConfig.baseUrls().stackOverflowApi());
    }
}
