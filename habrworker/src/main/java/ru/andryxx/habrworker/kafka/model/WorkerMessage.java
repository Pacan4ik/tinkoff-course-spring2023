package ru.andryxx.habrworker.kafka.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

public record WorkerMessage(
    @JsonProperty("id")
    Long id,
    @JsonProperty("url")
    URI url,
    @JsonProperty("message")
    String message
) {
}

