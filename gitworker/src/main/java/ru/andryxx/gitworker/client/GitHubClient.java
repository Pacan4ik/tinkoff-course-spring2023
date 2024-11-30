package ru.andryxx.gitworker.client;

import java.util.List;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class GitHubClient extends Client<GitHubEventResponse> {
    private static final String BASE_URL = "https://api.github.com/";
    private static final String URI_STRING = "/repos/{owner}/{repo}/events?page={pagenum}";

    private static final Class<GitHubEventResponse> CLASS_REF = GitHubEventResponse.class;

    public GitHubClient(WebClient.Builder webClientBuilder, String baseUrl, RetryTemplate retryTemplate) {
        super(webClientBuilder, CLASS_REF, baseUrl, retryTemplate);
    }

    public GitHubClient(WebClient.Builder webClientBuilder, String baseUrl) {
        super(webClientBuilder, CLASS_REF, baseUrl);
    }

    public GitHubClient(WebClient.Builder webClientBuilder, RetryTemplate retryTemplate) {
        super(webClientBuilder, CLASS_REF, BASE_URL, retryTemplate);
    }

    public GitHubClient(WebClient.Builder webClientBuilder) {
        super(webClientBuilder, CLASS_REF, BASE_URL);
    }

    public List<GitHubEventResponse> fetchResponse(String username, String repository, int pageNum) {
        return retryTemplate.execute(
            (RetryCallback<List<GitHubEventResponse>, WebClientResponseException>) context -> fetchList(
                username,
                repository,
                pageNum
            ).block()
        );
    }

    private Mono<List<GitHubEventResponse>> fetchList(
        String username,
        String repository,
        int pageNum
    ) {
        return webClient.get()
            .uri(getUriString(), username, repository, pageNum)
            .retrieve()
            .bodyToFlux(CLASS_REF)
            .collectList();
    }

    @Override
    protected String getUriString() {
        return URI_STRING;
    }
}
