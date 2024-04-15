package edu.java.bot.configuration;

import edu.tinkoff.retry.backoff.BackOff;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    @NotNull
    Client scrapper,
    @NotNull
    RateLimiting rateLimiting
) {
    public record Client(@NotNull String baseUrl, @NotNull BackOff backOff) {
    }

    public record RateLimiting(@NotNull Long requestsLimit, @NotNull Duration timeDuration) {
    }
}
