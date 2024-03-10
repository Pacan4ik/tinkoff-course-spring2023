package edu.java.scrapper.domain.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

public record LinkChatAssignmentDto(
    Long id,
    Long linkId,
    Long chatId
) {
    @Component
    public static class LinkChatAssignmentDTORowMapper implements RowMapper<LinkChatAssignmentDto> {
        @Override
        public LinkChatAssignmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LinkChatAssignmentDto(
                rs.getLong("id"),
                rs.getLong("link_id"),
                rs.getLong("chat_id")
            );
        }

        public LinkChatAssignmentDto map(Map<String, Object> columnObjectMap) {
            Long generatedId = (Long) Objects.requireNonNull(columnObjectMap.get("id"));
            Long generatedLinkId = (Long) Objects.requireNonNull(columnObjectMap.get("link_id"));
            Long generatedChatId = (Long) Objects.requireNonNull(columnObjectMap.get("chat_id"));
            return new LinkChatAssignmentDto(generatedId, generatedLinkId, generatedChatId);
        }

    }
}
