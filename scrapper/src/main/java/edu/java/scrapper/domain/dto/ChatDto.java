package edu.java.scrapper.domain.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
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
    }
}
