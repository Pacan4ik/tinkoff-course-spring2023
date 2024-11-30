package ru.andryxx.stackworker.client;

import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient extends Client<StackOverFlowResponse> {
    private static final String BASE_URL = "https://api.stackexchange.com/";
    private static final String URI_STRING =
        "/2.3/questions/{userInfo}?site=stackoverflow&filter=!nNPvSNR9ie";

    private static final Class<StackOverFlowResponse> CLASS_REF = StackOverFlowResponse.class;

    public StackOverflowClient(WebClient.Builder webClientBuilder, String baseUrl, RetryTemplate retryTemplate) {
        super(webClientBuilder, CLASS_REF, baseUrl, retryTemplate);
    }

    public StackOverflowClient(WebClient.Builder webClientBuilder, String baseUrl) {
        super(webClientBuilder, CLASS_REF, baseUrl);
    }

    public StackOverflowClient(WebClient.Builder webClientBuilder, RetryTemplate retryTemplate) {
        super(webClientBuilder, CLASS_REF, BASE_URL, retryTemplate);
    }

    public StackOverflowClient(WebClient.Builder webClientBuilder) {
        super(webClientBuilder, CLASS_REF, BASE_URL);
    }

    public StackOverFlowResponse fetchResponse(String questionId) {
        return super.fetchResponse(questionId);
    }

    @Override
    protected String getUriString() {
        return URI_STRING;
    }
}
