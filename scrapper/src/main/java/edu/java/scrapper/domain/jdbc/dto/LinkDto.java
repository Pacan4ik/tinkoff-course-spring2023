package edu.java.scrapper.domain.jdbc.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

public record LinkDto(
    Long id,
    URI url,
    OffsetDateTime updatedAt,
    OffsetDateTime createdAt,
    OffsetDateTime checkedAt,
    JsonNode additionalInfo

) {
    @Component
    @Slf4j
    public static class LinkDtoRowMapper implements RowMapper<LinkDto> {
        private static final String ID_COLUMN = "id";
        private static final String URL_COLUMN = "url";
        private static final String UPDATED_AT_COLUMN = "updated_at";
        private static final String CREATED_AT_COLUMN = "created_at";
        private static final String CHECKED_AT_COLUMN = "checked_at";
        private static final String ADDITIONAL_INFO_COLUMN = "additional_info";

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public LinkDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long id = rs.getLong(ID_COLUMN);
            URI url = URI.create(rs.getString(URL_COLUMN));
            OffsetDateTime updatedAt = rs.getObject(UPDATED_AT_COLUMN, OffsetDateTime.class);
            OffsetDateTime createdAt = rs.getObject(CREATED_AT_COLUMN, OffsetDateTime.class);
            OffsetDateTime checkedAt = rs.getObject(CHECKED_AT_COLUMN, OffsetDateTime.class);
            String jsonString = rs.getString(ADDITIONAL_INFO_COLUMN);
            JsonNode jsonNode;
            try {
                jsonNode = objectMapper.readTree(jsonString);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return new LinkDto(id, url, updatedAt, createdAt, checkedAt, jsonNode);
        }
    }
}
