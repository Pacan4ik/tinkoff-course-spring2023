package ru.andryxx.gitworker.kafka.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

public record ScrapperRequest(
    @JsonProperty("id")
    Long id,
    @JsonProperty("url")
    URI url
) {
}
