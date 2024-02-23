package edu.java.clients.github;

import edu.java.clients.Client;

public class GitHubClient extends Client<GitHubResponse> {
    private static final String BASE_URL = "https://api.github.com/";
    private static final String URI_STRING = "/repos/{owner}/{repo}";

    private static final Class<GitHubResponse> CLASS_REF = GitHubResponse.class;

    public GitHubClient(String baseUrl) {
        super(CLASS_REF, baseUrl);
    }

    public GitHubClient() {
        super(CLASS_REF, BASE_URL);
    }

    @Override
    protected String getUriString() {
        return URI_STRING;
    }
}