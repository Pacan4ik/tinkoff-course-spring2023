package ru.andryxx.gitworker.services.events.handlers.schema.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PullRequest(
    @JsonProperty("html_url")
    URI htmlUrl,
    @JsonProperty("title")
    String title
) {
}
