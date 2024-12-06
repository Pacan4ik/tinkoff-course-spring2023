package edu.java.scrapper.kafka.producers.workers;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

public record WorkerCheckRequest(
    @JsonProperty("id")
    Long id,
    @JsonProperty("url")
    URI url
) {
}
