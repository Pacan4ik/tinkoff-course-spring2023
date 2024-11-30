package ru.andryxx.gitworker.services.events.handlers.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ForkEventPayload(
    @JsonProperty("forkee")
    Forkee forkee
) {
    public record Forkee(
        @JsonProperty("full_name")
        String fullName,
        @JsonProperty("html_url")
        URI htmlUrl
    ) {
    }
}

