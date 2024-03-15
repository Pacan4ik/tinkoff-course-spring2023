package edu.java.scrapper.domain.dao;

import edu.java.scrapper.domain.dto.ChatDto;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("jdbcChatRepository")
public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ChatDto.ChatDTORowMapper chatDtoMapper;

    public JdbcChatRepository(
        JdbcTemplate jdbcTemplate,
        ChatDto.ChatDTORowMapper chatDtoMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.chatDtoMapper = chatDtoMapper;
    }

    public ChatDto add(Long id) {
        return jdbcTemplate.queryForObject("insert into chat(id) values (?) returning *", chatDtoMapper, id);
    }

    public ChatDto remove(Long id) {
        return jdbcTemplate.queryForObject("delete from chat where id in (?) returning *", chatDtoMapper, id);
    }

    public List<ChatDto> findAll() {
        return jdbcTemplate.query("select * from chat", chatDtoMapper);
    }

    public List<ChatDto> findAll(Long... ids) {
        String sql = "select * from chat where id in ("
            + String.join(",", Collections.nCopies(ids.length, "?"))
            + ")";
        return jdbcTemplate.query(sql, chatDtoMapper, (Object[]) ids);
    }

    public Optional<ChatDto> find(Long id) {
        var list = jdbcTemplate.query("select * from chat where id = (?)", chatDtoMapper, id);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.getFirst());
    }

    @Override
    public Long addLink(Long chatId, Long linkId) {
        return jdbcTemplate.queryForObject(
            "insert into link_chat_assignment(chat_id, link_id) values (?,?) returning link_id",
            Long.class,
            chatId,
            linkId
        );
    }

    @Override
    public Long removeLink(Long chatId, Long linkId) {
        return jdbcTemplate.queryForObject(
            "delete from link_chat_assignment where chat_id = (?) and link_id = (?) returning link_id",
            Long.class,
            chatId,
            linkId
        );
    }

    @Override
    public List<Long> getAllLinks(Long id) {
        return jdbcTemplate.queryForList(
            "select link_id from link_chat_assignment where chat_id = (?)",
            Long.class,
            id
        );
    }
}
