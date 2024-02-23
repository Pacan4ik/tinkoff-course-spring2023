package edu.java.scrapper.configuration;

import edu.java.scrapper.clients.github.GitHubClient;
import edu.java.scrapper.clients.stackoverflow.StackOverflowClient;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    @Bean
    Scheduler scheduler,
    @NotNull
    BaseUrls baseUrls
) {
    @Bean
    GitHubClient gitHubClient() {
        return new GitHubClient(baseUrls.gitHubApi);
    }

    @Bean
    StackOverflowClient stackOverFlowClient() {
        return new StackOverflowClient(baseUrls.stackOverflowApi);
    }

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record BaseUrls(@NotNull String gitHubApi, @NotNull String stackOverflowApi) {
    }

}
