package edu.java.scrapper.clients.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record StackOverFlowResponse(
    @JsonProperty("items") List<Item> items
) {
    public record Item(
        @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate,
        @JsonProperty("last_edit_date") OffsetDateTime lastEditDate,
        @JsonProperty("link") String link,
        @JsonProperty("title") String title
    ) {
    }
}
