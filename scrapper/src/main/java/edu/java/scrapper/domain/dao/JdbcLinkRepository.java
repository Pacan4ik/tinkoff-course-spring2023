package edu.java.scrapper.domain.dao;

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

@Repository("jdbcLinkRepository")
public class JdbcLinkRepository implements LinkRepository {
    JdbcTemplate jdbcTemplate;
    LinkDto.LinkDtoRowMapper mapper;

    @Autowired
    public JdbcLinkRepository(JdbcTemplate jdbcTemplate, LinkDto.LinkDtoRowMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    public LinkDto add(URI url) {
        return jdbcTemplate.queryForObject(
            "insert into link(url) values (?) returning *",
            mapper,
            url.toString()
        );
    }

    public LinkDto add(URI url, String description) {
        return jdbcTemplate.queryForObject(
            "insert into link(url, event_description) values (?, ?) returning *",
            mapper,
            url.toString(),
            description
        );
    }

    public LinkDto remove(URI url) {
        return jdbcTemplate.queryForObject(
            "delete from link where url in (?) returning *",
            mapper,
            url.toString()
        );
    }

    public LinkDto remove(Long id) {
        return jdbcTemplate.queryForObject(
            "delete from link where id in (?) returning *",
            mapper,
            id
        );
    }

    @Override
    public List<LinkDto> remove(Long... ids) {
        String sql = "delete from link where id in ("
                     + String.join(",", Collections.nCopies(ids.length, "?"))
                     + ") returning *";
        return jdbcTemplate.query(sql, mapper, (Object[]) ids);
    }

    @Override
    public List<LinkDto> removeUnassigned(Long... ids) {
        String sql = "delete from link where link.id in ("
                     + String.join(",", Collections.nCopies(ids.length, "?"))
                     + ") and link.id not in (select link_id from link_chat_assignment) returning *";

        return jdbcTemplate.query(sql, mapper, (Object[]) ids);
    }

    public List<LinkDto> findAll() {
        return jdbcTemplate.query("select * from link", mapper);
    }

    public List<LinkDto> findAll(URI... urls) {
        String sql = "select * from link where url in ("
                     + String.join(",", Collections.nCopies(urls.length, "?"))
                     + ")";
        return jdbcTemplate.query(sql, mapper, Arrays.stream(urls).map(Object::toString).toArray());
    }

    public Optional<LinkDto> find(URI url) {
        var list = jdbcTemplate.query("select * from link where url=(?)", mapper, url.toString());
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.getFirst());
    }

    public List<LinkDto> findAll(Long... ids) {
        String sql = "select * from link where id in ("
                     + String.join(",", Collections.nCopies(ids.length, "?"))
                     + ")";
        return jdbcTemplate.query(sql, mapper, (Object[]) ids);
    }

    public Optional<LinkDto> find(Long id) {
        var list = jdbcTemplate.query("select * from link where id=(?)", mapper, id);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.getFirst());
    }

    @Override
    public List<LinkDto> findAllWhereCheckedAtBefore(OffsetDateTime offsetDateTime) {
        return jdbcTemplate.query("select * from link where checked_at < (?)", mapper, offsetDateTime);
    }

    @Override
    public LinkDto updateUpdatedAt(Long id, OffsetDateTime newOffsetDateTime) {
        return jdbcTemplate.queryForObject(
            "update link set updated_at = (?) where id = (?) returning *",
            mapper,
            newOffsetDateTime,
            id
        );
    }

    @Override
    public LinkDto updateCheckedAt(Long id, OffsetDateTime newOffsetDateTime) {
        return jdbcTemplate.queryForObject(
            "update link set checked_at = (?) where id = (?) returning *",
            mapper,
            newOffsetDateTime,
            id
        );
    }

    @Override
    public List<LinkDto> getAllLinks(Long chatId) {
        return jdbcTemplate.query(
            "select * from link join public.link_chat_assignment lca on link.id = lca.link_id and lca.chat_id = (?)",
            mapper,
            chatId
        );
    }

    @Override
    public LinkDto updateAdditionalInfo(Long id, String fieldName, Object value) {
        String sql = "update link set additional_info = jsonb_set(coalesce(additional_info::jsonb, '{}'::jsonb),"
                     + "concat('{', ?, '}')::text[], to_jsonb(?) ) where id = ? returning *";

        return jdbcTemplate.queryForObject(
            sql,
            mapper,
            fieldName,
            value,
            id
        );
    }

}
