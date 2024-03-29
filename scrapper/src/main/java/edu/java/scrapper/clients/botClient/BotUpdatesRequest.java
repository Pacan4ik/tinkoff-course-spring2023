package edu.java.scrapper.clients.botClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.util.Collection;

public record BotUpdatesRequest(
    @JsonProperty("id")
    Long id,
    @JsonProperty("url")
    URI url,
    @JsonProperty("description")
    String description,
    @JsonProperty("tgChatIds")
    Collection<Long> tgChatIds
) {
}
