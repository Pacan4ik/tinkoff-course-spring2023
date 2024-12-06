package ru.andryxx.habrworker.configuration;

import edu.tinkoff.retry.backoff.BackOff;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreInvalidFields = false, ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Client habr,
    @NotNull
    Kafka kafka
) {
    public record Client(@NotNull String commentSelector,
                         @NotNull String contentSelector,
                         @NotNull ClientBackOff backOff) {
        static class ClientBackOff extends BackOff {
            ClientBackOff(
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
