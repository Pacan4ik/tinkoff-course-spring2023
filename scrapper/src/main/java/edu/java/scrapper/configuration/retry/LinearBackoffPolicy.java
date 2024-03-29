package edu.java.scrapper.configuration.retry;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.retry.backoff.SleepingBackOffPolicy;
import org.springframework.retry.backoff.ThreadWaitSleeper;

@Slf4j
public class LinearBackoffPolicy implements SleepingBackOffPolicy<LinearBackoffPolicy> {

    private static final long DEFAULT_INITIAL_PERIOD = 100L;
    private static final long DEFAULT_MAX_PERIOD = 30000L;

    @Getter
    private Long initialPeriod = DEFAULT_INITIAL_PERIOD;

    @Getter
    private Long maxPeriod = DEFAULT_MAX_PERIOD;

    @Setter
    private Sleeper sleeper = new ThreadWaitSleeper();

    public void setInitialPeriod(long backOffPeriod) {
        this.initialPeriod = (backOffPeriod > 0 ? backOffPeriod : DEFAULT_INITIAL_PERIOD);
    }

    public void setMaxPeriod(long maxPeriod) {
        this.maxPeriod = (maxPeriod > 0 ? maxPeriod : DEFAULT_MAX_PERIOD);
    }

    @Override
    public LinearBackoffPolicy withSleeper(Sleeper sleeper) {
        LinearBackoffPolicy res = new LinearBackoffPolicy();
        res.setInitialPeriod(initialPeriod);
        res.setMaxPeriod(maxPeriod);
        res.setSleeper(sleeper);
        return res;
    }

    @Override
    public BackOffContext start(RetryContext context) {
        return new LinearBackOffContext(initialPeriod, maxPeriod);
    }

    @Override
    public void backOff(BackOffContext backOffContext) throws BackOffInterruptedException {
        LinearBackOffContext linearBackOffContext = (LinearBackOffContext) backOffContext;
        long sleepTime = linearBackOffContext.getSleepAndIncrement();
        log.debug("Sleeping for " + sleepTime);
        try {
            this.sleeper.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BackOffInterruptedException("Thread interrupted while sleeping", e);
        }
    }

    static class LinearBackOffContext implements BackOffContext {
        private final long startInterval;
        private long interval;
        private final long maxInterval;

        LinearBackOffContext(long startInterval, long maxInterval) {
            this.startInterval = startInterval;
            this.interval = startInterval;
            this.maxInterval = maxInterval;
        }

        public synchronized long getSleepAndIncrement() {
            interval += startInterval;
            if (interval > maxInterval) {
                interval = maxInterval;
            }
            return interval;
        }

    }
}
