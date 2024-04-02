package edu.java.scrapper.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ListLinksResponse(
    @JsonProperty("links")
    List<LinkResponse> links,

    @JsonProperty("size")
    Integer size
) {
    public ListLinksResponse(List<LinkResponse> links) {
        this(links, links.size());
    }
}
