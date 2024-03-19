package edu.java.bot.scrapperClient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

public record LinkResponse(
    @JsonProperty("id")
    Long id,
    @JsonProperty("url")
    URI url
) {

}
