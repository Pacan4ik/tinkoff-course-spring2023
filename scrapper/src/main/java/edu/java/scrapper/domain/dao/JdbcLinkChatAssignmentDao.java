package edu.java.scrapper.domain.dao;

import edu.java.scrapper.domain.dto.LinkChatAssignmentDto;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("jdbcLinkChatAssignmentDao")
public class JdbcLinkChatAssignmentDao implements LinkChatAssignmentDao {
    JdbcTemplate jdbcTemplate;

    LinkChatAssignmentDto.LinkChatAssignmentDTORowMapper mapper;

    public JdbcLinkChatAssignmentDao(
        JdbcTemplate jdbcTemplate,
        LinkChatAssignmentDto.LinkChatAssignmentDTORowMapper mapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    public LinkChatAssignmentDto add(Long linkId, Long chatId) {
        return jdbcTemplate.queryForObject(
            "insert into link_chat_assignment(link_id, chat_id) values (?,?) returning *",
            mapper,
            linkId,
            chatId
        );
    }

    public LinkChatAssignmentDto remove(Long id) {
        return jdbcTemplate.queryForObject(
            "delete from link_chat_assignment where id in (?) returning  *",
            mapper,
            id
        );
    }

    public List<LinkChatAssignmentDto> remove(Long... ids) {
        String sql = "delete from link_chat_assignment where id in ("
            + String.join(",", Collections.nCopies(ids.length, "?"))
            + ") returning *";

        return jdbcTemplate.query(sql, mapper, (Object[]) ids);
    }

    public List<LinkChatAssignmentDto> findAll() {
        return jdbcTemplate.query("select * from link_chat_assignment", mapper);
    }

    public Optional<LinkChatAssignmentDto> findById(Long assignmentId) {
        var list = jdbcTemplate.query("select * from link_chat_assignment where id = (?)", mapper, assignmentId);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.getFirst());
    }

    public List<LinkChatAssignmentDto> findByLinkId(Long linkId) {
        return jdbcTemplate.query("select * from link_chat_assignment where link_id = (?)", mapper, linkId);
    }

    public List<LinkChatAssignmentDto> findByChatId(Long chatId) {
        return jdbcTemplate.query("select * from link_chat_assignment where chat_id = (?)", mapper, chatId);
    }

    @Override
    public Optional<LinkChatAssignmentDto> findByLinkIdAndChatId(Long linkId, Long chatId) {
        var list = jdbcTemplate.query(
            "select * from link_chat_assignment where link_id = ? and chat_id = ?",
            mapper,
            linkId,
            chatId
        );
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.getFirst());
    }

}
