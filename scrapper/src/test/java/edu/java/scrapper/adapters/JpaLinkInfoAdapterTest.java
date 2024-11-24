package edu.java.scrapper.adapters;

import edu.java.scrapper.domain.adapters.LinkDto;
import edu.java.scrapper.domain.adapters.LinkInfoAdapter;
import edu.java.scrapper.integration.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "app.data-base-access-type=jpa")
public class JpaLinkInfoAdapterTest extends IntegrationTest {
    @Autowired JdbcTemplate jdbcTemplate;
    @Autowired LinkInfoAdapter linkInfoAdapter;

    @Test
    @Transactional
    @Rollback
    void shouldReturnCorrectObject() {
        //given
        jdbcTemplate.update(
            "insert into link(id, url, checked_at, additional_info)"
            + "values (1, 'https://example.com', ?, "
            + "'{\"open_issues_count\":1, \"updated_at\":\"2024-04-10T00:00:00Z\", \"answer_count\":6}'::json)",
            OffsetDateTime.parse("2024-04-10T00:00:00Z")
        );

        //when
        LinkDto linkDto =
            linkInfoAdapter.findAllCheckedAtBefore(OffsetDateTime.parse("2030-04-10T00:00:00Z")).getFirst();

        //then
        LinkDto expected =
            new LinkDto(
                1L,
                URI.create("https://example.com"),
                OffsetDateTime.parse("2024-04-10T00:00:00Z")
            );
        Assertions.assertEquals(expected, linkDto);
    }

    @Test
    @Transactional
    @Rollback
    void shouldUpdateCheckedAt() {
        //given
        jdbcTemplate.update(
            "insert into link(id, url, checked_at) values (1, 'https://example.com', ?)",
            OffsetDateTime.parse("2024-04-10T00:00Z")
        );

        //when
        linkInfoAdapter.updateCheckedAt(1L, OffsetDateTime.parse("2024-05-10T00:00Z"));

        //then
        Assertions.assertEquals(
            OffsetDateTime.parse("2024-05-10T00:00Z"),
            jdbcTemplate.queryForObject("select checked_at from link where id = 1", OffsetDateTime.class)
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldGetSubscribedChats() {
        //given
        jdbcTemplate.update("insert into link(id, url) values (1, 'https://example.com')");
        jdbcTemplate.update("insert into chat(id) values (1), (2), (5), (10), (11)");
        jdbcTemplate.update("insert into link_chat_assignment(link_id, chat_id) values (1, 1), (1, 2), (1, 5), (1, 10)");

        //when
        Collection<Long> subscribedChats = linkInfoAdapter.getSubscribedChats(1L);

        //then
        assertThat(subscribedChats).containsExactlyInAnyOrder(1L, 2L, 5L, 10L);
    }

}
