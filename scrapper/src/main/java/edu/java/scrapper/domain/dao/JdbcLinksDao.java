package edu.java.scrapper.domain.dao;

import edu.java.scrapper.domain.dto.LinkDto;
import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class JdbcLinksDao implements LinksDao {
    JdbcTemplate jdbcTemplate;
    LinkDto.LinkDtoRowMapper mapper;

    @Autowired
    public JdbcLinksDao(JdbcTemplate jdbcTemplate, LinkDto.LinkDtoRowMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    public LinkDto add(URI url) {
        String sql = "insert into links(url) values(?)";
        return executeUpdate(url.toString(), sql);
    }

    public LinkDto add(URI url, String description) {
        String sql = "insert into links(url, event_description) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
            con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, url);
                ps.setObject(2, description);
                return ps;
            },
            keyHolder
        );
        return mapper.map(Objects.requireNonNull(keyHolder.getKeys()));
    }

    public LinkDto remove(URI url) {
        String sql = "delete from links where url in (?)";
        return executeUpdate(url.toString(), sql);
    }

    public LinkDto remove(Long id) {
        String sql = "delete from links where id in (?)";
        return executeUpdate(id, sql);
    }

    @NotNull private <T> LinkDto executeUpdate(T obj, String sql) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
            con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, obj);
                return ps;
            },
            keyHolder
        );
        return mapper.map(Objects.requireNonNull(keyHolder.getKeys()));
    }

    public List<LinkDto> findAll() {
        return jdbcTemplate.query("select * from links", mapper);
    }

    public List<LinkDto> findAll(URI... urls) {
        String sql = "select * from links where links.url in ("
            + String.join(",", Collections.nCopies(urls.length, "?"))
            + ")";
        return jdbcTemplate.query(sql, mapper, (Object[]) urls);
    }

    public Optional<LinkDto> find(URI url) {
        var list = jdbcTemplate.query("select * from links where url=(?)", mapper, url);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.getFirst());
    }

    public List<LinkDto> findAll(Long... ids) {
        String sql = "select * from links where links.id in ("
            + String.join(",", Collections.nCopies(ids.length, "?"))
            + ")";
        return jdbcTemplate.query(sql, mapper, (Object[]) ids);
    }

    public Optional<LinkDto> find(Long id) {
        var list = jdbcTemplate.query("select * from links where id=(?)", mapper, id);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.getFirst());
    }

}
