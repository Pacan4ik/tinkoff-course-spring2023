package edu.java.scrapper.clients.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubResponse(
    @JsonProperty("pushed_at") OffsetDateTime pushedAt,
    @JsonProperty("updated_at") OffsetDateTime updatedAt,
    @JsonProperty("html_url") String htmlUrl,
    @JsonProperty("description") String description,
    @JsonProperty("open_issues_count") Long openIssuesCount
) {
}
