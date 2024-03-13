package edu.java.scrapper.domain.dao;

import edu.java.scrapper.domain.dto.LinkChatAssignmentDto;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
        String sql = "insert into link_chat_assignment(link_id, chat_id) values (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
            con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, linkId);
                ps.setLong(2, chatId);
                return ps;
            },
            keyHolder
        );
        return mapper.map(Objects.requireNonNull(keyHolder.getKeys()));
    }

    public LinkChatAssignmentDto remove(Long id) {
        String sql = "delete from link_chat_assignment where id in (?)";

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

    public List<LinkChatAssignmentDto> remove(Long... ids) {
        String sql = "delete from link_chat_assignment where id in ("
            + String.join(",", Collections.nCopies(ids.length, "?"))
            + ")";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
            con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < ids.length; i++) {
                    ps.setLong(i + 1, ids[i]);
                }
                return ps;
            },
            keyHolder
        );
        var keyList = keyHolder.getKeyList();
        List<LinkChatAssignmentDto> resultList = new ArrayList<>();
        for (var rowMap : keyList) {
            resultList.add(mapper.map(rowMap));
        }
        return resultList;
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
