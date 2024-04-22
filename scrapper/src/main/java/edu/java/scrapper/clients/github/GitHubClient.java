package edu.java.scrapper.clients.github;

import edu.java.scrapper.clients.Client;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient extends Client<GitHubResponse> {
    private static final String BASE_URL = "https://api.github.com/";
    private static final String URI_STRING = "/repos/{owner}/{repo}";

    private static final Class<GitHubResponse> CLASS_REF = GitHubResponse.class;

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

    public GitHubResponse fetchResponse(String username, String repository) {
        return super.fetchResponse(username, repository);
    }

    @Override
    protected String getUriString() {
        return URI_STRING;
    }
}
