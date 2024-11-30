package ru.andryxx.stackworker.configuration;

import edu.tinkoff.retry.backoff.BackOff;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;
import java.util.List;

@Validated
@ConfigurationProperties(prefix = "app", ignoreInvalidFields = false, ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Client stackoverflow,
    @NotNull
    Kafka kafka
) {
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

    public record Kafka(@NotNull String consumerTopic, @NotNull String producerTopic) {
    }
}
