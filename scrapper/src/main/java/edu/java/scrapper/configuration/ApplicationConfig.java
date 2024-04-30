package edu.java.scrapper.configuration;

import edu.tinkoff.retry.backoff.BackOff;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    @NotNull
    Client github,
    @NotNull
    Client stackoverflow,
    @NotNull
    Client bot,
    @NotNull
    AccessType dataBaseAccessType,
    @NotNull
    RateLimiting rateLimiting,
    @NotNull
    SendingMethod sendingMethod,
    Kafka kafka
) {

    public record Scheduler(boolean enable,
                            @NotNull Duration interval,
                            @NotNull Duration linkCheckingFrequency) {
    }

    public record Client(@NotNull String baseUrl, @NotNull ScrapperBackOff backOff) {
        static class ScrapperBackOff extends BackOff {
            ScrapperBackOff(
                @NotNull
                Integer maxAttempts,
                @NotNull
                Duration delay,
                @NotNull
                Policy policy,
                Integer multiplier,
                List<Integer> additionalStatuses
            ) {
                super(maxAttempts, delay, policy, multiplier, additionalStatuses);
            }
        }
    }

    public record RateLimiting(@NotNull Long requestsLimit, @NotNull Duration timeDuration) {
    }

    public enum AccessType {
        JDBC, JPA, JOOQ
    }

    public enum SendingMethod {
        HTTP, QUEUE
    }

    public record Kafka(String producerTopic) {
    }
}
