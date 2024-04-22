package edu.tinkoff.retry.backoff;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BackOff {
    private final Integer maxAttempts;
    private final Duration delay;
    private final Policy policy;
    private final Integer multiplier;
    private final List<Integer> additionalStatuses;

    public BackOff(
        Integer maxAttempts,
        Duration delay,
        Policy policy,
        Integer multiplier,
        List<Integer> additionalStatuses
    ) {
        this.maxAttempts = maxAttempts;
        this.delay = delay;
        this.policy = policy;
        this.multiplier = multiplier;
        this.additionalStatuses = additionalStatuses;
    }

    public List<Integer> additionalStatuses() {
        return Objects.requireNonNullElse(additionalStatuses, Collections.emptyList());
    }

    public Integer maxAttempts() {
        return maxAttempts;
    }

    public Duration delay() {
        return delay;
    }

    public Policy policy() {
        return policy;
    }

    public Integer multiplier() {
        return multiplier;
    }

    public enum Policy {
        CONSTANT, LINEAR, EXPONENTIAL
    }
}
