package edu.java.scrapper.domain.adapters;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LinkInfoDto {
    private Long id;
    private URI url;
    private OffsetDateTime checkedAt;
    private AdditionalInfo additionalInfo;

    /**
     * Returns a copy of {@link AdditionalInfo}. Changes will not affect the original object.
     *
     * @return A copy of {@link AdditionalInfo}.
     **/
    public AdditionalInfo getAdditionalInfo() {
        return new AdditionalInfo(
            additionalInfo.openIssuesCount,
            additionalInfo.pushedAt,
            additionalInfo.updatedAt,
            additionalInfo.answerCount,
            additionalInfo.commentCount,
            additionalInfo.lastActivityDate
        );
    }

    @Data
    @AllArgsConstructor
    public static class AdditionalInfo {
        @JsonProperty("open_issues_count")
        private Long openIssuesCount;

        @JsonProperty("pushed_at")
        private OffsetDateTime pushedAt;

        @JsonProperty("updated_at")
        private OffsetDateTime updatedAt;

        @JsonProperty("answer_count")
        private Long answerCount;

        @JsonProperty("comment_count")
        private Long commentCount;

        @JsonProperty("last_activity_date")
        private OffsetDateTime lastActivityDate;
    }
}
