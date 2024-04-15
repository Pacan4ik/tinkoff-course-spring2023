package edu.tinkoff.retry.backoff;

import java.util.Objects;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;

public class BackOffMatcher {
    private BackOffMatcher() {
    }

    public static BackOffPolicy match(BackOff backOff) {
        return switch (backOff.policy()) {
            case EXPONENTIAL -> {
                ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
                exponentialBackOffPolicy.setInitialInterval(backOff.delay().toMillis());
                exponentialBackOffPolicy.setMultiplier(Objects.requireNonNull(
                    backOff.multiplier(),
                    "Multiplier is not present"
                ));
                yield exponentialBackOffPolicy;
            }
            case CONSTANT -> {
                FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
                fixedBackOffPolicy.setBackOffPeriod(backOff.delay().toMillis());
                yield fixedBackOffPolicy;
            }
            case LINEAR -> {
                LinearBackOffPolicy linearBackoffPolicy = new LinearBackOffPolicy();
                linearBackoffPolicy.setInitialPeriod(backOff.delay().toMillis());
                yield linearBackoffPolicy;
            }
            case null -> throw new IllegalArgumentException("Retry policy not present");
        };
    }
}
