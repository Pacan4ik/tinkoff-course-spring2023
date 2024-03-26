package edu.java.scrapper;

import edu.java.scrapper.api.exceptions.LinkAlreadyExistsException;
import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.model.LinkResponse;
import edu.java.scrapper.api.services.LinkRepositoryService;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JooqLinkServiceTest extends IntegrationTest {
    @Autowired
    @Qualifier("jooqLinkService")
    LinkRepositoryService linkRepositoryService;

    @Autowired
    JdbcTemplate jdbcTemplate;
    private static final String EXAMPLE_URL = "https://example.com/";
    private static final String EXAMPLE2_URL = "https://example2.com/";

    @Test
    @Transactional
    @Rollback
    void shouldAddLink() {
        //given
        jdbcTemplate.update("insert into chat values (123)");

        //when
        linkRepositoryService.addLink(123L, URI.create(EXAMPLE_URL));

        //then
        Assertions.assertEquals(
            EXAMPLE_URL,
            jdbcTemplate.queryForObject(
                "select url from link join link_chat_assignment lca on link.id = lca.link_id",
                String.class
            )
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfUserDoesntExistOnAdd() {
        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> linkRepositoryService.addLink(123L, URI.create(EXAMPLE_URL))
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfAssignmentAlreadyExists() {
        jdbcTemplate.update("insert into link(id, url) values (1, ?)", EXAMPLE_URL);
        jdbcTemplate.update("insert into chat(id) values (123)");
        jdbcTemplate.update("insert into link_chat_assignment(link_id, chat_id) values (1, 123)");

        Assertions.assertThrows(
            LinkAlreadyExistsException.class,
            () -> linkRepositoryService.addLink(123L, URI.create(EXAMPLE_URL))
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldReturnLinks() {
        //given
        jdbcTemplate.update("insert into link(id, url) values (1, ?), (2, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update("insert into chat(id) values (123)");
        jdbcTemplate.update("insert into link_chat_assignment(link_id, chat_id) values (1, 123), (2, 123)");

        //when
        var listResponse = linkRepositoryService.getUserLinks(123L);

        //then
        Assertions.assertEquals(
            List.of(new LinkResponse(1L, URI.create(EXAMPLE_URL)), new LinkResponse(2L, URI.create(EXAMPLE2_URL))),
            listResponse
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldReturnEmptyCollectionIfNoAssignments() {
        jdbcTemplate.update("insert into chat(id) values (123)");

        Assertions.assertTrue(linkRepositoryService.getUserLinks(123L).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfUserDoesntExistOnGet() {
        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> linkRepositoryService.getUserLinks(123L)
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldDeleteLink() {
        //given
        jdbcTemplate.update("insert into link(id, url) values (1, ?)", EXAMPLE_URL);
        jdbcTemplate.update("insert into chat(id) values (123)");
        jdbcTemplate.update("insert into link_chat_assignment(link_id, chat_id) values (1, 123)");

        //when
        linkRepositoryService.removeLink(123L, URI.create(EXAMPLE_URL));

        //then
        Assertions.assertTrue(
            jdbcTemplate.queryForList(
                "select * from link_chat_assignment", Long.class
            ).isEmpty()
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldDeleteUnassignedLinks() {
        //given
        jdbcTemplate.update("insert into link(id, url) values (1, ?), (2, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update("insert into chat(id) values (123), (1234)");
        jdbcTemplate.update("insert into link_chat_assignment(link_id, chat_id) values (1, 123), (1, 1234), (2, 123)");

        //when
        linkRepositoryService.removeLink(123L, URI.create(EXAMPLE2_URL));

        //then
        Assertions.assertEquals(
            EXAMPLE_URL,
            jdbcTemplate.queryForObject(
                "select distinct(url) from link",
                String.class
            )
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfUserDoesntExistOnDelete() {
        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> linkRepositoryService.removeLink(123L, URI.create(EXAMPLE_URL)),
            "User not found"
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfLinkDoesntExistOnDelete() {
        jdbcTemplate.update("insert into chat(id) values (123)");

        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> linkRepositoryService.removeLink(123L, URI.create(EXAMPLE_URL)),
            "Link not found"
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfNoAssignmentOnDelete() {
        jdbcTemplate.update("insert into chat(id) values (123)");
        jdbcTemplate.update("insert into link(url) values (?)", EXAMPLE_URL);

        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> linkRepositoryService.removeLink(123L, URI.create(EXAMPLE_URL)),
            "User has not yet subscribed to this link"
        );
    }
}
