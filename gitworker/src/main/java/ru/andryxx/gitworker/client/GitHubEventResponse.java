package ru.andryxx.gitworker.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public record GitHubEventResponse(
    @JsonProperty("id") Long id,
    @JsonProperty("type") String type,
    @JsonProperty("actor") GitHubActor actor,
    @JsonProperty("repo") GitHubRepository repo,
    @JsonProperty("payload") JsonNode payload
) {
    public record GitHubActor(
        @JsonProperty("display_login") String displayLogin
    ) {
    }

    public record GitHubRepository(
        @JsonProperty("name") String name
    ) {
    }
}

