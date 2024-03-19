package edu.java.scrapper.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

public record LinkResponse(
    @JsonProperty("id")
    Long id,
    @JsonProperty("url")
    URI url
) {
}
