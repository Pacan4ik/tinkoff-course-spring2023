package ru.andryxx.gitworker.services.events.handlers.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//Wiki page updated
//no tracking
@JsonIgnoreProperties(ignoreUnknown = true)
public record GollumEventPayload() {
}
