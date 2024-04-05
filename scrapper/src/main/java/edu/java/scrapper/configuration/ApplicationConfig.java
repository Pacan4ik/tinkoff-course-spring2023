package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    Kafka kafka
) {

    public record Scheduler(boolean enable,
                            @NotNull Duration interval,
                            @NotNull Duration forceCheckDelay,
                            @NotNull Duration linkCheckingFrequency) {
    }

    public record Client(@NotNull String baseUrl, @NotNull BackOff backOff) {
        public record BackOff(@NotNull Integer maxAttempts,
                              @NotNull Duration delay,
                              @NotNull Policy policy,
                              Integer multiplier,
                              List<Integer> additionalStatuses) {
            public List<Integer> additionalStatuses() {
                return Objects.requireNonNullElse(additionalStatuses, Collections.emptyList());
            }

            public enum Policy {
                CONSTANT, LINEAR, EXPONENTIAL
            }
        }
    }

    public record RateLimiting(@NotNull Long requestsLimit, @NotNull Duration timeDuration) {
    }

    public enum AccessType {
        JDBC, JPA, JOOQ
    }

    public record Kafka(String producerTopic) {
    }
}
