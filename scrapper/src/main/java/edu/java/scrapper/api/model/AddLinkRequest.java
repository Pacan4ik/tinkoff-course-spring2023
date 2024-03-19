package edu.java.scrapper.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

public record AddLinkRequest(
    @JsonProperty("link")
    URI link
) {
}
