package edu.java.scrapper;

import edu.java.scrapper.domain.dao.LinkRepository;
import edu.java.scrapper.domain.dto.LinkDto;
import java.net.URI;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class LinkRepositoryTest extends IntegrationTest {
    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LinkDto.LinkDtoRowMapper mapper;

    private static final String EXAMPLE_URL = "https://example.com/";
    private static final String EXAMPLE2_URL = "https://example2.com/";

    @Test
    @Transactional
    @Rollback
    void shouldAdd() {
        var dto = linkRepository.add(URI.create(EXAMPLE_URL));
        Assertions.assertEquals(EXAMPLE_URL, dto.url().toString());
        Assertions.assertNotNull(dto.id());
        Assertions.assertNotNull(dto.createdAt());
        Assertions.assertNotNull(dto.updatedAt());
        Assertions.assertNotNull(dto.eventDescription());

        var dtoQuery = jdbcTemplate.queryForObject("select * from link where url = ?", mapper, EXAMPLE_URL);
        Assertions.assertEquals(dto, dtoQuery);
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindById() {
        //given
        jdbcTemplate.update("insert into link(id, url) values (10, ?)", EXAMPLE_URL);

        //when
        var dto = linkRepository.find(10L).get();

        //then
        var queryDto = jdbcTemplate.queryForObject("select * from link where id = 10", mapper);
        Assertions.assertEquals(queryDto, dto);
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindBySeveralIds() {
        //given
        jdbcTemplate.update(
            "insert into link(id, url) values (10, ?), (11, ?), (12, ?)",
            EXAMPLE_URL,
            EXAMPLE2_URL,
            "https://otherurl.com"
        );

        //when
        var listDto = linkRepository.findAll(10L, 11L);

        //then
        var queryDtos = jdbcTemplate.query("select * from link where id in (10, 11)", mapper);
        Assertions.assertEquals(queryDtos, listDto);
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindByUrl() {
        //given
        jdbcTemplate.update("insert into link(id, url) values (10, ?)", EXAMPLE_URL);

        //when
        var dto = linkRepository.find(URI.create(EXAMPLE_URL)).get();

        //then
        var queryDto =
            jdbcTemplate.queryForObject("select * from link where url = ?", mapper, EXAMPLE_URL);
        Assertions.assertEquals(queryDto, dto);
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindBySeveralUrls() {
        //given
        jdbcTemplate.update(
            "insert into link(url) values (?), (?), ('https://otherurl.com/')",
            EXAMPLE_URL,
            EXAMPLE2_URL
        );

        //when
        var listDto = linkRepository.findAll(URI.create(EXAMPLE_URL), URI.create(EXAMPLE2_URL));

        //then
        var queryDtos =
            jdbcTemplate.query(
                "select * from link where url in (?, ?)",
                mapper,
                EXAMPLE_URL,
                EXAMPLE2_URL
            );
        Assertions.assertEquals(listDto, queryDtos);
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindAll() {
        //given
        jdbcTemplate.update(
            "insert into link(url) values (?), (?), ('https://otherurl.com/')",
            EXAMPLE_URL,
            EXAMPLE2_URL
        );

        //when
        var listDto = linkRepository.findAll();

        //then
        var queryDtos =
            jdbcTemplate.query(
                "select * from link",
                mapper
            );
        Assertions.assertEquals(listDto, queryDtos);
    }

    @Test
    @Transactional
    @Rollback
    void shouldDeleteById() {
        //given
        jdbcTemplate.update(
            "insert into link(id, url) values (1, ?), (2, ?), (3, 'https://otherurl.com/')",
            EXAMPLE_URL,
            EXAMPLE2_URL
        );

        //when
        linkRepository.remove(2L);

        //then
        Assertions.assertTrue(
            jdbcTemplate.query("select * from link where id = 2", mapper)
                .isEmpty()
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldDeleteByUrl() {
        //given
        jdbcTemplate.update(
            "insert into link(id, url) values (1, ?), (2, ?), (3, 'https://otherurl.com/')",
            EXAMPLE_URL,
            EXAMPLE2_URL
        );

        //when
        linkRepository.remove(2L);

        //then
        Assertions.assertTrue(
            jdbcTemplate.query("select * from link where url = (?)", mapper, EXAMPLE2_URL)
                .isEmpty()
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindCheckedBefore() {
        jdbcTemplate.update(
            "insert into link (id, url, checked_at) values (1, ?, ?), (2, ?, ?)",
            EXAMPLE_URL,
            OffsetDateTime.parse("2024-03-12T21:30:00.0+00:00"),
            EXAMPLE2_URL,
            OffsetDateTime.parse("2024-03-10T10:45:00.0+00:00")
        );

        var dtoList = linkRepository.findAllWhereCheckedAtBefore(OffsetDateTime.parse("2024-03-11T00:00:00.0+00:00"));
        Assertions.assertEquals(1, dtoList.size());
        Assertions.assertEquals(EXAMPLE2_URL, dtoList.getFirst().url().toString());
    }

    @Test
    @Transactional
    @Rollback
    void shouldUpdateUpdatedAtField() {
        //given
        jdbcTemplate.update("insert into link(id, url) values (1, ?)", EXAMPLE_URL);

        //when
        OffsetDateTime newOffsetDateTime = OffsetDateTime.parse("2019-03-11T00:00:00.0+00:00");
        linkRepository.updateUpdatedAt(1L, newOffsetDateTime);

        //then
        var timestamp = jdbcTemplate.queryForObject("select updated_at from link", Timestamp.class);
        Assertions.assertEquals(newOffsetDateTime.toInstant(), timestamp.toInstant());
    }

    @Test
    @Transactional
    @Rollback
    void shouldUpdateCheckedAtField() {
        //given
        jdbcTemplate.update("insert into link(id, url) values (1, ?)", EXAMPLE_URL);

        //when
        OffsetDateTime newOffsetDateTime = OffsetDateTime.parse("2019-03-11T00:00:00.0+00:00");
        linkRepository.updateCheckedAt(1L, newOffsetDateTime);

        //then
        var timestamp = jdbcTemplate.queryForObject("select checked_at from link", Timestamp.class);
        Assertions.assertEquals(newOffsetDateTime.toInstant(), timestamp.toInstant());
    }

    @Test
    @Transactional
    @Rollback
    void shouldReturnAssignedChats() {
        //given
        jdbcTemplate.update("insert into link(id, url) values (123, ?)", EXAMPLE_URL);
        jdbcTemplate.update("insert into chat(id) values (1), (2), (3)");
        jdbcTemplate.update("insert into link_chat_assignment(link_id, chat_id) values (123, 1), (123, 2), (123, 3)");

        //when
        var chatsIds = linkRepository.getChats(123L);

        //then
        Assertions.assertEquals(
            List.of(1L, 2L, 3L),
            chatsIds
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldSetAdditionalInfo() {
        //given
        jdbcTemplate.update("insert into link(id, url) values (1, ?)", EXAMPLE_URL);

        //when
        linkRepository.updateAdditionalInfo(1L, "field1", 10L);

        //then
        LinkDto dto = jdbcTemplate.queryForObject("select * from link where id = 1", mapper);
        Assertions.assertTrue(dto.additionalInfo().has("field1"));
        Assertions.assertEquals(10L, dto.additionalInfo().findValue("field1").asLong());
    }

    @Test
    @Transactional
    @Rollback
    void shouldRewriteAdditionalInfo() {
        //given
        jdbcTemplate.update(
            "insert into link(id, url, additional_info) values (1, ?, ?::jsonb)",
            EXAMPLE_URL,
            "{\"key1\": \"value1\", \"key2\": \"value2\"}"
        );

        //when
        linkRepository.updateAdditionalInfo(1L, "key2", "value3");

        //then
        LinkDto dto = jdbcTemplate.queryForObject("select * from link where id = 1", mapper);
        Assertions.assertTrue(dto.additionalInfo().has("key1"));
        Assertions.assertTrue(dto.additionalInfo().has("key2"));
        Assertions.assertEquals("value3", dto.additionalInfo().findValue("key2").asText());
    }
}
