package edu.java.scrapper.domain.adapters.serializing;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
import java.io.IOException;
import java.time.OffsetDateTime;

public class AdditionalInfoDeserializer extends JsonDeserializer<LinkInfoDto.AdditionalInfo> {

    public static final String OPEN_ISSUES_COUNT = "open_issues_count";
    public static final String PUSHED_AT = "pushed_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String ANSWER_COUNT = "answer_count";
    public static final String COMMENT_COUNT = "comment_count";
    public static final String LAST_ACTIVITY_DATE = "last_activity_date";

    @Override
    public LinkInfoDto.AdditionalInfo deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        Long openIssuesCount =
            node.has(OPEN_ISSUES_COUNT) ? node.get(OPEN_ISSUES_COUNT).asLong() : null;
        OffsetDateTime pushedAt =
            node.has(PUSHED_AT) ? OffsetDateTime.parse(node.get(PUSHED_AT).asText()) : null;
        OffsetDateTime updatedAt =
            node.has(UPDATED_AT) ? OffsetDateTime.parse(node.get(UPDATED_AT).asText()) : null;
        Long answerCount =
            node.has(ANSWER_COUNT) ? node.get(ANSWER_COUNT).asLong() : null;
        Long commentCount =
            node.has(COMMENT_COUNT) ? node.get(COMMENT_COUNT).asLong() : null;
        OffsetDateTime lastActivityDate =
            node.has(LAST_ACTIVITY_DATE)
                ? OffsetDateTime.parse(node.get(LAST_ACTIVITY_DATE).asText())
                : null;

        return new LinkInfoDto.AdditionalInfo(
            openIssuesCount,
            pushedAt,
            updatedAt,
            answerCount,
            commentCount,
            lastActivityDate
        );
    }
}
