package edu.tinkoff.retry.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.classify.Classifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class HttpStatusPolicyBuilder {
    private boolean retryOnInternalServerError = false;

    private List<Integer> retryableStatuses = new ArrayList<>();

    private final RetryPolicy retryPolicy;

    public HttpStatusPolicyBuilder(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    public HttpStatusPolicyBuilder retryOnInternalServerError(boolean bool) {
        this.retryOnInternalServerError = bool;
        return this;
    }

    public HttpStatusPolicyBuilder add(int httpStatus) {
        this.retryableStatuses.add(httpStatus);
        return this;
    }

    public HttpStatusPolicyBuilder add(List<Integer> httpStatuses) {
        this.retryableStatuses.addAll(httpStatuses);
        return this;
    }

    public ExceptionClassifierRetryPolicy build() {
        List<Integer> statusesList = Collections.unmodifiableList(this.retryableStatuses);
        ExceptionClassifierRetryPolicy classifierRetryPolicy = new ExceptionClassifierRetryPolicy();
        classifierRetryPolicy.setExceptionClassifier((Classifier<Throwable, RetryPolicy>) classifiable -> {
            if (classifiable instanceof WebClientResponseException) {
                HttpStatusCode status = ((WebClientResponseException) classifiable).getStatusCode();
                if ((status.is5xxServerError() && retryOnInternalServerError)
                    || statusesList.contains(status.value())) {
                    return retryPolicy;
                }
            }
            return new NeverRetryPolicy();
        });
        return classifierRetryPolicy;
    }

}
