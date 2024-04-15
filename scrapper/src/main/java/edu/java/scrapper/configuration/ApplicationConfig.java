package edu.java.scrapper.configuration;

import edu.tinkoff.retry.backoff.BackOff;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
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
    RateLimiting rateLimiting
) {

    public record Scheduler(boolean enable,
                            @NotNull Duration interval,
                            @NotNull Duration forceCheckDelay,
                            @NotNull Duration linkCheckingFrequency) {
    }

    public record Client(@NotNull String baseUrl, @NotNull BackOff backOff) {

    }

    public record RateLimiting(@NotNull Long requestsLimit, @NotNull Duration timeDuration) {
    }

    public enum AccessType {
        JDBC, JPA, JOOQ
    }
}
