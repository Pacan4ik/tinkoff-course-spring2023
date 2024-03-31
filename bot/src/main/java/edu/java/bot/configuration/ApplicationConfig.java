package edu.java.bot.configuration;

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
        public record BackOff(@NotNull Integer maxAttempts,
                              @NotNull Duration delay,
                              @NotNull Policy policy,
                              Integer multiplier) {
            public enum Policy {
                CONSTANT, LINEAR, EXPONENTIAL
            }
        }
    }

    public record RateLimiting(@NotNull Long requestsLimit, @NotNull Duration timeDuration) {
    }
}
