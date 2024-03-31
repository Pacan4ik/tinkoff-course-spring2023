package edu.java.scrapper.configuration.retry;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.tinkoff.linearbackoffextension.LinearBackoffPolicy;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.MaxAttemptsRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Configuration
@Slf4j
public class RetryTemplatesConfig {
    private final ApplicationConfig applicationConfig;

    public RetryTemplatesConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean
    public RetryListener retryListener() {
        return new RetryListener() {
            @Override
            public <T, E extends Throwable> void onError(
                RetryContext context,
                RetryCallback<T, E> callback,
                Throwable throwable
            ) {
                log.warn("Retryable method threw {}th exception {}",
                    context.getRetryCount(), throwable.toString()
                );
            }

            @Override
            public <T, E extends Throwable> void onSuccess(
                RetryContext context,
                RetryCallback<T, E> callback,
                T result
            ) {
                log.debug("Success after {} attempts", context.getRetryCount());
            }
        };
    }

    @Bean("githubRetryTemplate")
    public RetryTemplate gitHubRetryTemplate(RetryListener retryListener) {
        RetryTemplateBuilder retryTemplateBuilder = new RetryTemplateBuilder();
        RetryPolicy retryPolicy = new MaxAttemptsRetryPolicy(applicationConfig.github().backOff().maxAttempts());

        retryOnStatusCustomizer(retryPolicy, applicationConfig.github().backOff().additionalStatuses())
            .accept(retryTemplateBuilder);
        retryBackoffPolicyCustomizer(applicationConfig.github().backOff())
            .accept(retryTemplateBuilder);

        RetryTemplate template = retryTemplateBuilder.build();
        template.registerListener(retryListener);
        return template;
    }

    @Bean("stackoverflowRetryTemplate")
    public RetryTemplate stackoverflowRetryTemplate(RetryListener retryListener) {
        RetryTemplateBuilder retryTemplateBuilder = new RetryTemplateBuilder();
        RetryPolicy retryPolicy = new MaxAttemptsRetryPolicy(applicationConfig.stackoverflow().backOff().maxAttempts());

        retryOnStatusCustomizer(retryPolicy, applicationConfig.stackoverflow().backOff().additionalStatuses())
            .accept(retryTemplateBuilder);
        retryBackoffPolicyCustomizer(applicationConfig.stackoverflow().backOff())
            .accept(retryTemplateBuilder);

        RetryTemplate template = retryTemplateBuilder.build();
        template.registerListener(retryListener);
        return template;
    }

    @Bean("botRetryTemplate")
    public RetryTemplate botRetryTemplate(RetryListener retryListener) {
        RetryTemplateBuilder retryTemplateBuilder = new RetryTemplateBuilder();
        RetryPolicy retryPolicy = new MaxAttemptsRetryPolicy(applicationConfig.bot().backOff().maxAttempts());

        retryOnStatusCustomizer(retryPolicy, applicationConfig.bot().backOff().additionalStatuses())
            .accept(retryTemplateBuilder);
        retryBackoffPolicyCustomizer(applicationConfig.bot().backOff())
            .accept(retryTemplateBuilder);

        RetryTemplate template = retryTemplateBuilder.build();
        template.registerListener(retryListener);
        return template;
    }

    public Consumer<RetryTemplateBuilder> retryOnStatusCustomizer(
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

        return builder -> builder.customPolicy(classifierRetryPolicy);
    }

    public Consumer<RetryTemplateBuilder> retryBackoffPolicyCustomizer(ApplicationConfig.Client.BackOff backOff) {
        return switch (backOff.policy()) {
            case EXPONENTIAL -> (builder) -> {
                ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
                exponentialBackOffPolicy.setInitialInterval(backOff.delay().toMillis());
                exponentialBackOffPolicy.setMultiplier(Objects.requireNonNull(
                    backOff.multiplier(),
                    "Multiplier is not present"
                ));
                builder.customBackoff(exponentialBackOffPolicy);
            };
            case CONSTANT -> (builder) -> {
                FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
                fixedBackOffPolicy.setBackOffPeriod(backOff.delay().toMillis());
                builder.customBackoff(fixedBackOffPolicy);
            };
            case LINEAR -> (builder) -> {
                LinearBackoffPolicy linearBackoffPolicy = new LinearBackoffPolicy();
                linearBackoffPolicy.setInitialPeriod(backOff.delay().toMillis());
                builder.customBackoff(linearBackoffPolicy);
            };
            case null -> throw new IllegalArgumentException("Retry policy not present");
        };
    }
}
