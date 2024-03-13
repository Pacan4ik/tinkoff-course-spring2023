package edu.java.scrapper.domain.dao;

import edu.java.scrapper.domain.dto.ChatDto;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
        String sql = "insert into chats(id) values (?)";
        return executeUpdate(id, sql);
    }

    public ChatDto remove(Long id) {
        String sql = "delete from chats where id in (?)";
        return executeUpdate(id, sql);
    }

    @NotNull private ChatDto executeUpdate(Long id, String sql) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
            con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, id);
                return ps;
            },
            keyHolder
        );
        return mapper.map(Objects.requireNonNull(keyHolder.getKeys()));
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
