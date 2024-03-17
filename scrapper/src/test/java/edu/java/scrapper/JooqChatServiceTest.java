package edu.java.scrapper;


import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.exceptions.UserAlreadyExistsException;
import edu.java.scrapper.api.services.ChatRepositoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JooqChatServiceTest extends IntegrationTest {
    @Autowired
    @Qualifier("jooqChatService")
    ChatRepositoryService chatRepositoryService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String EXAMPLE_URL = "https://example.com/";
    private static final String EXAMPLE2_URL = "https://example2.com/";

    @Test
    @Transactional
    @Rollback
    void shouldRegisterChat() {
        chatRepositoryService.registerChat(123L);

        Assertions.assertEquals(
            123L,
            jdbcTemplate.queryForObject("select id from chat where id = 123", Long.class)
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfChatExists() {
        jdbcTemplate.update("insert into chat(id) values (123)");

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> chatRepositoryService.registerChat(123L));
    }

    @Test
    @Transactional
    @Rollback
    void shouldDeleteChat() {
        //given
        jdbcTemplate.update("insert into chat(id) values (123)");

        //when
        chatRepositoryService.deleteChat(123L);

        //then
        Assertions.assertTrue(
            jdbcTemplate.queryForList("select id from chat where id = 123", Long.class).isEmpty()
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldDeleteUnassignedLinksIfChatDeleted() {
        //given
        jdbcTemplate.update("insert into link(id, url) values (1, ?), (2, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update("insert into chat(id) values (123), (1234)");
        jdbcTemplate.update("insert into link_chat_assignment(link_id, chat_id) values (1, 123), (1, 1234), (2, 123)");

        //when
        chatRepositoryService.deleteChat(123L);

        //then
        Assertions.assertTrue(
            jdbcTemplate.queryForList("select id from link where id = 2", Long.class).isEmpty()
        );
        Assertions.assertFalse(
            jdbcTemplate.queryForList("select id from link where id = 1", Long.class).isEmpty()
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldThrowExceptionIfChatDoesntExistOnDelete() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> chatRepositoryService.deleteChat(123L));
    }
}
