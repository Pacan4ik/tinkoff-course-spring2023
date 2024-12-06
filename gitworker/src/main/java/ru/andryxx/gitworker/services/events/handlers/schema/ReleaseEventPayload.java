package ru.andryxx.gitworker.services.events.handlers.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ReleaseEventPayload(
    @JsonProperty("action")
    String action,
    @JsonProperty("release")
    Release release
) {
    public record Release(
        @JsonProperty("html_url")
        URI htmlUrl
    ) {
    }
}
