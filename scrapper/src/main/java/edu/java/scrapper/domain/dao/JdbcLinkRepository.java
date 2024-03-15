package edu.java.scrapper.domain.dao;

import edu.java.scrapper.domain.dto.ChatDto;
import edu.java.scrapper.domain.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("jdbcLinksRepository")
public class JdbcLinksRepository implements LinksRepository {
    JdbcTemplate jdbcTemplate;
    LinkDto.LinkDtoRowMapper mapper;

    @Autowired
    public JdbcLinksRepository(JdbcTemplate jdbcTemplate, LinkDto.LinkDtoRowMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    public LinkDto add(URI url) {
        return jdbcTemplate.queryForObject(
            "insert into links(url) values (?) returning *",
            mapper,
            url.toString()
        );
    }

    public LinkDto add(URI url, String description) {
        return jdbcTemplate.queryForObject(
            "insert into links(url, event_description) values (?, ?) returning *",
            mapper,
            url.toString(),
            description
        );
    }

    public LinkDto remove(URI url) {
        return jdbcTemplate.queryForObject(
            "delete from links where url in (?) returning *",
            mapper,
            url.toString()
        );
    }

    public LinkDto remove(Long id) {
        return jdbcTemplate.queryForObject(
            "delete from links where id in (?) returning *",
            mapper,
            id
        );
    }

    public List<LinkDto> findAll() {
        return jdbcTemplate.query("select * from links", mapper);
    }

    public List<LinkDto> findAll(URI... urls) {
        String sql = "select * from links where links.url in ("
            + String.join(",", Collections.nCopies(urls.length, "?"))
            + ")";
        return jdbcTemplate.query(sql, mapper, Arrays.stream(urls).map(Object::toString).toArray());
    }

    public Optional<LinkDto> find(URI url) {
        var list = jdbcTemplate.query("select * from links where url=(?)", mapper, url.toString());
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

    @Override
    public List<LinkDto> findAllWhereCheckedAtBefore(OffsetDateTime offsetDateTime) {
        return jdbcTemplate.query("select * from links where checked_at < (?)", mapper, offsetDateTime);
    }

    @Override
    public LinkDto updateUpdatedAt(Long id, OffsetDateTime newOffsetDateTime) {
        return jdbcTemplate.queryForObject(
            "update links set updated_at = (?) where id = (?) returning *",
            mapper,
            newOffsetDateTime,
            id
        );
    }

    @Override
    public LinkDto updateCheckedAt(Long id, OffsetDateTime newOffsetDateTime) {
        return jdbcTemplate.queryForObject(
            "update links set checked_at = (?) where id = (?) returning *",
            mapper,
            newOffsetDateTime,
            id
        );
    }

    @Override
    public List<ChatDto> getChats(Long linkId) {
        return null;
    }

}
