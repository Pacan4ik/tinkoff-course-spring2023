package ru.andryxx.gitworker.services.events.handlers.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.andryxx.gitworker.services.events.handlers.schema.shared.PullRequest;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PullRequestReviewEventPayload(
    @JsonProperty("action")
    String action,
    @JsonProperty("pull_request")
    PullRequest pullRequest
) {
}
