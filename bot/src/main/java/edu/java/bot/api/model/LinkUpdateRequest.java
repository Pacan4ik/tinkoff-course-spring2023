package edu.java.bot.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(
    @JsonProperty("id")
    Long id,
    @JsonProperty("url")
    URI url,
    @JsonProperty("description")
    String description,
    @JsonProperty("tgChatIds")
    List<Long> tgChatIds
) {
}
