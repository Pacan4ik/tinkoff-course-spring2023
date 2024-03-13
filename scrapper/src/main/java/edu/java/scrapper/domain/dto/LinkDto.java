package edu.java.scrapper.domain.dto;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Objects;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

public record LinkDto(
    Long id,
    URI url,
    String eventDescription,
    OffsetDateTime updatedAt,
    OffsetDateTime createdAt,
    OffsetDateTime checkedAt

) {
    @Component
    public static class LinkDtoRowMapper implements RowMapper<LinkDto> {
        private static final String ID_COLUMN = "id";
        private static final String URL_COLUMN = "url";
        private static final String EVENT_DESCRIPTION_COLUMN = "event_description";
        private static final String UPDATED_AT_COLUMN = "updated_at";
        private static final String CREATED_AT_COLUMN = "created_at";
        private static final String CHECKED_AT_COLUMN = "checked_at";

        @Override
        public LinkDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long id = rs.getLong(ID_COLUMN);
            URI url = URI.create(rs.getString(URL_COLUMN));
            String eventDescription = rs.getString(EVENT_DESCRIPTION_COLUMN);
            OffsetDateTime updatedAt = rs.getObject(UPDATED_AT_COLUMN, OffsetDateTime.class);
            OffsetDateTime createdAt = rs.getObject(CREATED_AT_COLUMN, OffsetDateTime.class);
            OffsetDateTime checkedAt = rs.getObject(CHECKED_AT_COLUMN, OffsetDateTime.class);
            return new LinkDto(id, url, eventDescription, updatedAt, createdAt, checkedAt);
        }

        public LinkDto map(Map<String, Object> columnObjectMap) {
            Long generatedId = (Long) Objects.requireNonNull(columnObjectMap.get(ID_COLUMN));
            URI url = URI.create((String) Objects.requireNonNull(columnObjectMap.get(URL_COLUMN)));
            String generatedDescription =
                (String) Objects.requireNonNull(columnObjectMap.get(EVENT_DESCRIPTION_COLUMN));
            OffsetDateTime generatedUpdatedAt =
                ((Timestamp) Objects.requireNonNull(columnObjectMap.get(UPDATED_AT_COLUMN)))
                    .toInstant().atOffset(ZoneOffset.UTC);
            OffsetDateTime generatedCreatedAt =
                ((Timestamp) Objects.requireNonNull(columnObjectMap.get(CREATED_AT_COLUMN)))
                    .toInstant().atOffset(ZoneOffset.UTC);
            OffsetDateTime generatedCheckedAt =
                ((Timestamp) Objects.requireNonNull(columnObjectMap.get(CHECKED_AT_COLUMN)))
                    .toInstant().atOffset(ZoneOffset.UTC);
            return new LinkDto(
                generatedId,
                url,
                generatedDescription,
                generatedUpdatedAt,
                generatedCreatedAt,
                generatedCheckedAt
            );
        }
    }
}
