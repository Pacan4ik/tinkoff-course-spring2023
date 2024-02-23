package edu.java.configuration;

import edu.java.clients.Client;
import edu.java.clients.github.GitHubClient;
import edu.java.clients.github.GitHubResponse;
import edu.java.clients.stackoverflow.StackOverFlowResponse;
import edu.java.clients.stackoverflow.StackOverflowClient;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    @Bean
    Client<GitHubResponse> gitHubClient() {
        return new GitHubClient();
    }

    @Bean
    Client<StackOverFlowResponse> stackOverFlowClient() {
        return new StackOverflowClient();
    }
}
