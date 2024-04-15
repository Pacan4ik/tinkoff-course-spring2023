package edu.tinkoff.retry.backoff;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
