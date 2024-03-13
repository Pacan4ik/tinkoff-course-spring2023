package edu.java.scrapper.domain.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Objects;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

public record ChatDto(
    Long id,
    OffsetDateTime registeredAt
) {
    @Component
    public static class ChatDTORowMapper implements RowMapper<ChatDto> {
        private static final String ID_COLUMN = "id";
        private static final String REGISTERED_AT_COLUMN = "registered_at";

        @Override
        public ChatDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ChatDto(
                rs.getLong(ID_COLUMN),
                rs.getObject(REGISTERED_AT_COLUMN, OffsetDateTime.class)
            );
        }

        public ChatDto map(Map<String, Object> columnObjectMap) {
            Long generatedId = (Long) Objects.requireNonNull(columnObjectMap.get(ID_COLUMN));
            OffsetDateTime generatedTimestamp =
                ((Timestamp) Objects.requireNonNull(columnObjectMap.get(REGISTERED_AT_COLUMN)))
                    .toInstant().atOffset(ZoneOffset.UTC);
            return new ChatDto(generatedId, generatedTimestamp);
        }
    }
}
