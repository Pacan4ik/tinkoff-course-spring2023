package edu.java.bot.configuration;

import edu.tinkoff.linearbackoffextension.LinearBackoffPolicy;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryInterceptorBuilder.StatelessRetryInterceptorBuilder;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.MaxAttemptsRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Configuration
@EnableRetry
public class RetryInterceptorConfig {
    private final ApplicationConfig applicationConfig;

    public RetryInterceptorConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean("scrapperRetryInterceptor")
    MethodInterceptor retryInterceptor() {
        RetryInterceptorBuilder.StatelessRetryInterceptorBuilder builder = RetryInterceptorBuilder.stateless();
        RetryPolicy retryPolicy = new MaxAttemptsRetryPolicy(applicationConfig.scrapper().backOff().maxAttempts());

        retryOnStatusCustomizer(
            retryPolicy,
            applicationConfig.scrapper().backOff().additionalStatuses()
        )
            .accept(builder);

        retryBackoffPolicyCustomizer(applicationConfig.scrapper().backOff())
            .accept(builder);

        return builder.build();
    }

    public Consumer<StatelessRetryInterceptorBuilder> retryOnStatusCustomizer(
        RetryPolicy retryPolicy,
        List<Integer> additionalRetryableList
    ) {
        ExceptionClassifierRetryPolicy classifierRetryPolicy = new ExceptionClassifierRetryPolicy();
        classifierRetryPolicy.setExceptionClassifier((Classifier<Throwable, RetryPolicy>) classifiable -> {
            if (classifiable instanceof WebClientResponseException) {
                HttpStatusCode status = ((WebClientResponseException) classifiable).getStatusCode();
                if (status.is5xxServerError() || additionalRetryableList.contains(status.value())) {
                    return retryPolicy;
                }
            }
            return new NeverRetryPolicy();
        });

        return builder -> builder.retryPolicy(classifierRetryPolicy);
    }

    public Consumer<StatelessRetryInterceptorBuilder> retryBackoffPolicyCustomizer(
        ApplicationConfig.Client.BackOff backOff
    ) {
        return switch (backOff.policy()) {
            case EXPONENTIAL -> (builder) -> {
                ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
                exponentialBackOffPolicy.setInitialInterval(backOff.delay().toMillis());
                exponentialBackOffPolicy.setMultiplier(Objects.requireNonNull(
                    backOff.multiplier(),
                    "Multiplier is not present"
                ));
                builder.backOffPolicy(exponentialBackOffPolicy);
            };
            case CONSTANT -> (builder) -> {
                FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
                fixedBackOffPolicy.setBackOffPeriod(backOff.delay().toMillis());
                builder.backOffPolicy(fixedBackOffPolicy);
            };
            case LINEAR -> (builder) -> {
                LinearBackoffPolicy linearBackoffPolicy = new LinearBackoffPolicy();
                linearBackoffPolicy.setInitialPeriod(backOff.delay().toMillis());
                builder.backOffPolicy(linearBackoffPolicy);
            };
            case null -> throw new IllegalArgumentException("Retry policy not present");
        };
    }
}
