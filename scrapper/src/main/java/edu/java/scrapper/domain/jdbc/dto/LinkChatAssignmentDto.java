package edu.java.scrapper.domain.jdbc.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

public record LinkChatAssignmentDto(
    Long id,
    Long linkId,
    Long chatId
) {
    @Component
    public static class LinkChatAssignmentDTORowMapper implements RowMapper<LinkChatAssignmentDto> {
        private static final String ID_COLUMN = "id";
        private static final String LINK_ID_COLUMN = "link_id";
        private static final String CHAT_ID_COLUMN = "chat_id";

        @Override
        public LinkChatAssignmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LinkChatAssignmentDto(
                rs.getLong(ID_COLUMN),
                rs.getLong(LINK_ID_COLUMN),
                rs.getLong(CHAT_ID_COLUMN)
            );
        }
    }
}
