package edu.java.scrapper.domain.dto;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
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
    }
}
