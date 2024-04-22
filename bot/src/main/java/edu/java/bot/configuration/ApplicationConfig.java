package edu.java.bot.configuration;

import edu.tinkoff.retry.backoff.BackOff;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
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
    public record Client(@NotNull String baseUrl, @NotNull BotBackoff backOff) {
        static class BotBackoff extends BackOff {
            BotBackoff(
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
}
