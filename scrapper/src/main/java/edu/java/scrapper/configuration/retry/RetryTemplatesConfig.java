package edu.java.scrapper.configuration.retry;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.tinkoff.retry.backoff.BackOff;
import edu.tinkoff.retry.backoff.BackOffMatcher;
import edu.tinkoff.retry.policy.HttpStatusPolicyBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.MaxAttemptsRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

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
        return getRetryTemplate(retryListener, applicationConfig.github().backOff());
    }

    @Bean("stackoverflowRetryTemplate")
    public RetryTemplate stackoverflowRetryTemplate(RetryListener retryListener) {
        return getRetryTemplate(retryListener, applicationConfig.stackoverflow().backOff());
    }

    @Bean("botRetryTemplate")
    public RetryTemplate botRetryTemplate(RetryListener retryListener) {
        return getRetryTemplate(retryListener, applicationConfig.bot().backOff());
    }

    @NotNull private RetryTemplate getRetryTemplate(RetryListener retryListener, BackOff backOff) {
        RetryTemplateBuilder retryTemplateBuilder = new RetryTemplateBuilder();
        RetryPolicy retryPolicy = new MaxAttemptsRetryPolicy(backOff.maxAttempts());

        ExceptionClassifierRetryPolicy httpStatusesPolicy = new HttpStatusPolicyBuilder(retryPolicy)
            .retryOnInternalServerError(true)
            .add(backOff.additionalStatuses())
            .build();

        retryTemplateBuilder.customPolicy(httpStatusesPolicy);
        retryTemplateBuilder.customBackoff(BackOffMatcher.match(backOff));

        RetryTemplate template = retryTemplateBuilder.build();
        template.registerListener(retryListener);
        return template;
    }
}
