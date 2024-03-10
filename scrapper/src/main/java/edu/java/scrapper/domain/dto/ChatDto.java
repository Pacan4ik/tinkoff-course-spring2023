package edu.java.scrapper.domain.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Objects;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

public record ChatDto(
    Long id,
    OffsetDateTime registeredAt
) {
    @Component
    public static class ChatDTORowMapper implements RowMapper<ChatDto> {
        @Override
        public ChatDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ChatDto(
                rs.getLong("id"),
                rs.getObject("registered_at", OffsetDateTime.class)
            );
        }

        public ChatDto map(Map<String, Object> columnObjectMap) {
            Long generatedId = (Long) Objects.requireNonNull(columnObjectMap.get("id"));
            OffsetDateTime generatedTimestamp =
                ((Timestamp) Objects.requireNonNull(columnObjectMap.get("registered_at")))
                    .toInstant().atOffset(ZoneOffset.UTC);
            return new ChatDto(generatedId, generatedTimestamp);
        }
    }
}
