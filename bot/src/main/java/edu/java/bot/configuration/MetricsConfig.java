package edu.java.bot.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MetricsConfig {
    private final MeterRegistry meterRegistry;

    @Bean("messageProcessedCounter")
    public Counter messageProcessedCounter() {
        return Counter.builder("messages.processed")
            .description("Number of messages processed")
            .register(meterRegistry);
    }

    @Bean("messageFailedCounter")
    public Counter messageFailedCounter() {
        return Counter.builder("messages.failed")
            .description("Number of messages that failed to be processed")
            .register(meterRegistry);
    }

}
