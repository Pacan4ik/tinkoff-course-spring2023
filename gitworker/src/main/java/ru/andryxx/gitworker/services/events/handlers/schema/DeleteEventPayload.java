package ru.andryxx.gitworker.services.events.handlers.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeleteEventPayload(
    @JsonProperty("ref")
    String ref,
    @JsonProperty("ref_type")
    String refType
) {
}
