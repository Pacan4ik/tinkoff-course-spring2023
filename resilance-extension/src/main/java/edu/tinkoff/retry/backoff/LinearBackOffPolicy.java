package edu.tinkoff.retry.backoff;

import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.retry.backoff.SleepingBackOffPolicy;
import org.springframework.retry.backoff.ThreadWaitSleeper;

public class LinearBackOffPolicy implements SleepingBackOffPolicy<LinearBackOffPolicy> {

    private static final long DEFAULT_INITIAL_PERIOD = 100L;
    private static final long DEFAULT_MAX_PERIOD = 30000L;
    private Long initialPeriod = DEFAULT_INITIAL_PERIOD;
    private Long maxPeriod = DEFAULT_MAX_PERIOD;

    public Long getInitialPeriod() {
        return initialPeriod;
    }

    public Long getMaxPeriod() {
        return maxPeriod;
    }

    public void setSleeper(Sleeper sleeper) {
        this.sleeper = sleeper;
    }

    private Sleeper sleeper = new ThreadWaitSleeper();

    public void setInitialPeriod(long backOffPeriod) {
        this.initialPeriod = (backOffPeriod > 0 ? backOffPeriod : DEFAULT_INITIAL_PERIOD);
    }

    public void setMaxPeriod(long maxPeriod) {
        this.maxPeriod = (maxPeriod > 0 ? maxPeriod : DEFAULT_MAX_PERIOD);
    }

    @Override
    public LinearBackOffPolicy withSleeper(Sleeper sleeper) {
        LinearBackOffPolicy res = new LinearBackOffPolicy();
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
