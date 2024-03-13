package edu.java.scrapper;

import edu.java.scrapper.api.exceptions.LinkAlreadyExistsException;
import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.exceptions.UserAlreadyExistsException;
import edu.java.scrapper.api.repositories.ChatRepository;
import edu.java.scrapper.api.repositories.LinkRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ReposTest extends IntegrationTest {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String EXAMPLE_URL = "https://example.com/";
    private static final String EXAMPLE2_URL = "https://example2.com/";

    @Test
    @Transactional
    @Rollback
    void shouldAddUser() {
        Long id = chatRepository.addChat(123L);
        Assertions.assertEquals(123L, id);

        Long bdId = jdbcTemplate.queryForObject("select id from chats where id = 123", Long.class);
        Assertions.assertEquals(123L, bdId);
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindUser() {
        //given
        jdbcTemplate.update("insert into chats(id) values (123)");

        //when
        Long id = chatRepository.findChat(123L).get();

        //then
        Assertions.assertEquals(123L, id);
    }

    @Test
    @Transactional
    @Rollback
    void shouldDeleteUser() {
        //given
        jdbcTemplate.update("insert into chats(id) values (123)");

        //when
        chatRepository.deleteChat(123L);

        //then
        Assertions.assertTrue(
            jdbcTemplate.queryForList("select id from chats where id = 123", Long.class)
                .isEmpty()
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldDeleteLinkIfUserDeleted() {
        //given
        jdbcTemplate.update("insert into chats(id) values (123)");
        jdbcTemplate.update("insert into links(id, url) values (1, 'https://example.com/')");
        jdbcTemplate.update("insert into link_chat_assignment(id, link_id, chat_id) values (1, 1, 123)");

        //when
        chatRepository.deleteChat(123L);

        //then
        Assertions.assertTrue(
            jdbcTemplate.queryForList("select id from chats where id = 123", Long.class)
                .isEmpty()
        );
        Assertions.assertTrue(
            jdbcTemplate.queryForList("select id from links where id = 1", Long.class)
                .isEmpty()
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldntDeleteLinkIfUserDeletedAndOtherAssignmentsExists() {
        //given
        jdbcTemplate.update("insert into chats(id) values (123), (1234)");
        jdbcTemplate.update("insert into links(id, url) values (1, 'https://example.com/')");
        jdbcTemplate.update("insert into link_chat_assignment(id, link_id, chat_id) values (1, 1, 123), (2,1, 1234)");

        //when
        chatRepository.deleteChat(123L);

        //then
        Assertions.assertTrue(
            jdbcTemplate.queryForList("select id from chats where id = 123", Long.class)
                .isEmpty()
        );
        Assertions.assertEquals(
            1L,
            jdbcTemplate.queryForObject("select id from links where id = 1", Long.class)

        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfUserAlreadyExists() {
        jdbcTemplate.update("insert into chats(id) values (1)");

        Assertions.assertThrows(
            UserAlreadyExistsException.class,
            () -> chatRepository.addChat(1L)
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfUserNotFoundOnDelete() {
        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> chatRepository.deleteChat(1L)
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldReturnOptionalEmptyIfNotFound() {
        Optional<Long> optional = chatRepository.findChat(1L);

        Assertions.assertTrue(optional.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void shouldReturnLinks() {
        //given
        jdbcTemplate.update("insert into chats(id) values (123)");
        jdbcTemplate.update("insert into links(id, url) values (1, ?), (2, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update("insert into link_chat_assignment(id, link_id, chat_id) values (1, 1, 123), (2, 2, 123)");

        //when
        var linksList = linkRepository.getLinks(123L);

        //then
        Assertions.assertEquals(
            List.of(URI.create(EXAMPLE_URL), URI.create(EXAMPLE2_URL)),
            linksList
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindLink() {
        //given
        jdbcTemplate.update("insert into links(id, url) values (1, ?), (2, ?)", EXAMPLE_URL, EXAMPLE2_URL);

        //when
        Long id = linkRepository.findLink(URI.create(EXAMPLE2_URL)).get();

        //then
        Assertions.assertEquals(2L, id);
    }

    @Test
    @Transactional
    @Rollback
    void shouldReturnLastUpdate() {
        //given
        var time = OffsetDateTime.parse("2024-03-12T21:30:00.0+00:00");
        jdbcTemplate.update(
            "insert into links (id, url, updated_at) values (1, ?, ?)",
            EXAMPLE_URL,
            time
        );

        //when
        var res = linkRepository.getLastUpdate(URI.create(EXAMPLE_URL));

        //then
        Assertions.assertEquals(time, res);
    }

    @Test
    @Transactional
    @Rollback
    void shouldAddLink() {
        //given
        jdbcTemplate.update("insert into chats(id) values (123)");

        //when
        URI link = linkRepository.addLink(123L, URI.create(EXAMPLE_URL));
        Assertions.assertEquals(EXAMPLE_URL, link.toString());

        //then
        Assertions.assertEquals(
            EXAMPLE_URL,
            jdbcTemplate.queryForObject("select url from links", String.class)
        );
        Assertions.assertEquals(
            123L,
            jdbcTemplate.queryForObject("select chat_id from link_chat_assignment", Long.class)
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldDeleteLink() {
        //given
        jdbcTemplate.update("insert into chats(id) values (123)");
        jdbcTemplate.update("insert into links(id, url) values (1, ?), (2, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update("insert into link_chat_assignment(id, link_id, chat_id) values (1, 1, 123), (2, 2, 123)");

        //when
        var deletedLink = linkRepository.delete(123L, URI.create(EXAMPLE2_URL));

        //then
        Assertions.assertTrue(
            jdbcTemplate.queryForList("select * from link_chat_assignment where chat_id = 123 and link_id = 2")
                .isEmpty()
        );
        Assertions.assertTrue(
            jdbcTemplate.queryForList("select * from links where url = (?)", deletedLink.toString())
                .isEmpty()
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldReturnSubscribedChats() {
        //given
        jdbcTemplate.update("insert into chats(id) values (123), (1234), (12345)");
        jdbcTemplate.update("insert into links(id, url) values (1, ?), (2, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update(
            "insert into link_chat_assignment(link_id, chat_id) values (1, 123), (1, 1234), (2, 123), (2, 1234), (2, 12345)"
        );

        //when
        var subList = linkRepository.getSubscribedChats(URI.create(EXAMPLE2_URL));

        //then
        Assertions.assertEquals(List.of(123L, 1234L, 12345L), subList);
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfUserNotFoundOnAddLink() {
        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> linkRepository.addLink(123L, URI.create(EXAMPLE_URL))
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfUserNotFoundOnGetLinks() {
        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> linkRepository.getLinks(123L)
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfUserNotFoundOnDeleteLink() {
        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> linkRepository.delete(123L, URI.create(EXAMPLE_URL))
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfUserAlreadySubscribed() {
        jdbcTemplate.update("insert into chats(id) values (123)");
        jdbcTemplate.update("insert into links(id, url) values (1, ?)", EXAMPLE_URL);
        jdbcTemplate.update("insert into link_chat_assignment(link_id, chat_id) values (1, 123)");

        Assertions.assertThrows(
            LinkAlreadyExistsException.class,
            () -> linkRepository.addLink(123L, URI.create(EXAMPLE_URL))
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfLinkNotFoundInLinksOnDelete() {
        jdbcTemplate.update("insert into chats(id) values (123)");

        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> linkRepository.delete(123L, URI.create(EXAMPLE_URL))
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfLinkNotFoundInAssignmentOnDelete() {
        jdbcTemplate.update("insert into chats(id) values (123)");
        jdbcTemplate.update("insert into links(url) values (?)", EXAMPLE_URL);

        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> linkRepository.delete(123L, URI.create(EXAMPLE_URL))
        );
    }
}
