package ru.andryxx.gitworker.services.events.handlers.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.andryxx.gitworker.services.events.handlers.schema.shared.Issue;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IssuesEvent(
    @JsonProperty("action")
    String action,
    @JsonProperty("issue")
    Issue issue
) {
}
