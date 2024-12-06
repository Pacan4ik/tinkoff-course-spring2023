package ru.andryxx.gitworker.services.events.handlers.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommitCommentEventPayload(
    @JsonProperty("comment")
    Comment comment
) {
    public record Comment(
        @JsonProperty("html_url")
        URI htmlUrl,
        @JsonProperty("body")
        String body,
        @JsonProperty("user")
        User user
    ) {
    }

    public record User(
        @JsonProperty("login")
        String login
    ) {
    }
}
