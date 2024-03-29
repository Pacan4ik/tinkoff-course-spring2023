package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
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
    AccessType dataBaseAccessType
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
                              Integer multiplier) {
            public enum Policy {
                CONSTANT, LINEAR, EXPONENTIAL
            }
        }
    }

//    public record BaseUrls(@NotNull String gitHubApi, @NotNull String stackOverflowApi, @NotNull String botApi) {
//    }

    public enum AccessType {
        JDBC, JPA, JOOQ
    }
}
