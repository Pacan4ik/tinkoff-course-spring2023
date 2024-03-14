package edu.java.scrapper.domain.dao;

import edu.java.scrapper.domain.dto.ChatDto;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("jdbcChatsDao")
public class JdbcChatsDao implements ChatsDao {
    JdbcTemplate jdbcTemplate;
    ChatDto.ChatDTORowMapper mapper;

    public JdbcChatsDao(JdbcTemplate jdbcTemplate, ChatDto.ChatDTORowMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    public ChatDto add(Long id) {
        return jdbcTemplate.queryForObject("insert into chats(id) values (?) returning *", mapper, id);
    }

    public ChatDto remove(Long id) {
        return jdbcTemplate.queryForObject("delete from chats where id in (?) returning *", mapper, id);
    }

    public List<ChatDto> findAll() {
        return jdbcTemplate.query("select * from chats", mapper);
    }

    public List<ChatDto> findAll(Long... ids) {
        String sql = "select * from chats where id in ("
            + String.join(",", Collections.nCopies(ids.length, "?"))
            + ")";
        return jdbcTemplate.query(sql, mapper, (Object[]) ids);
    }

    public Optional<ChatDto> find(Long id) {
        var list = jdbcTemplate.query("select * from chats where id = (?)", mapper, id);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.getFirst());
    }
}
