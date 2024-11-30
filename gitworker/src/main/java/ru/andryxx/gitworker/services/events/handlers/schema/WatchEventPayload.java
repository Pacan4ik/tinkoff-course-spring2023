package ru.andryxx.gitworker.services.events.handlers.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WatchEventPayload(
    @JsonProperty("action")
    String action
) {
}
