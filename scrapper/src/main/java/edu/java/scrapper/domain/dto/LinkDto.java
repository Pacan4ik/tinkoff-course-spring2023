package edu.java.scrapper.domain.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Objects;

public record LinkDto(
    Long id,
    URI url,
    String eventDescription,
    OffsetDateTime updatedAt,
    OffsetDateTime createdAt

) {
    @Component
    public static class LinkDtoRowMapper implements RowMapper<LinkDto> {
        @Override
        public LinkDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long id = rs.getLong("id");
            URI url = URI.create(rs.getString("url"));
            String eventDescription = rs.getString("event_description");
            OffsetDateTime updatedAt = rs.getObject("updated_at", OffsetDateTime.class);
            OffsetDateTime createdAt = rs.getObject("created_at", OffsetDateTime.class);
            return new LinkDto(id, url, eventDescription, updatedAt, createdAt);
        }

        public LinkDto map(Map<String, Object> columnObjectMap) {
            Long generatedId = (Long) Objects.requireNonNull(columnObjectMap.get("id"));
            URI url = URI.create((String) Objects.requireNonNull(columnObjectMap.get("url")));
            String generatedDescription = (String) Objects.requireNonNull(columnObjectMap.get("event_description"));
            OffsetDateTime generatedUpdatedAt =
                ((Timestamp) Objects.requireNonNull(columnObjectMap.get("updated_at")))
                    .toInstant().atOffset(ZoneOffset.UTC);
            OffsetDateTime generatedCreatedAt =
                ((Timestamp) Objects.requireNonNull(columnObjectMap.get("created_at")))
                    .toInstant().atOffset(ZoneOffset.UTC);
            return new LinkDto(generatedId, url, generatedDescription, generatedUpdatedAt, generatedCreatedAt);
        }
    }
}
