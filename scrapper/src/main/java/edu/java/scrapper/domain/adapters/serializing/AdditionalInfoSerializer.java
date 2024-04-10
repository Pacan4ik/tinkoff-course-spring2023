package edu.java.scrapper.domain.adapters.serializing;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
import java.io.IOException;

public class AdditionalInfoSerializer extends JsonSerializer<LinkInfoDto.AdditionalInfo> {
    @Override
    public void serialize(LinkInfoDto.AdditionalInfo value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
        gen.writeStartObject();
        if (value.getOpenIssuesCount() != null) {
            gen.writeNumberField("open_issues_count", value.getOpenIssuesCount());
        }
        if (value.getPushedAt() != null) {
            gen.writeStringField("pushed_at", value.getPushedAt().toString());
        }
        if (value.getUpdatedAt() != null) {
            gen.writeStringField("updated_at", value.getUpdatedAt().toString());
        }
        if (value.getAnswerCount() != null) {
            gen.writeNumberField("answer_count", value.getAnswerCount());
        }
        if (value.getCommentCount() != null) {
            gen.writeNumberField("comment_count", value.getCommentCount());
        }
        if (value.getLastActivityDate() != null) {
            gen.writeStringField("last_activity_date", value.getLastActivityDate().toString());
        }
        gen.writeEndObject();
    }
}
