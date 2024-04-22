package edu.java.bot.configuration;

import edu.tinkoff.retry.backoff.BackOffMatcher;
import edu.tinkoff.retry.policy.HttpStatusPolicyBuilder;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.MaxAttemptsRetryPolicy;

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

        ExceptionClassifierRetryPolicy httpStatusesPolicy = new HttpStatusPolicyBuilder(retryPolicy)
            .retryOnInternalServerError(true)
            .add(applicationConfig.scrapper().backOff().additionalStatuses())
            .build();

        builder.retryPolicy(httpStatusesPolicy);

        builder.backOffPolicy(BackOffMatcher.match(applicationConfig.scrapper().backOff()));

        return builder.build();
    }
}
