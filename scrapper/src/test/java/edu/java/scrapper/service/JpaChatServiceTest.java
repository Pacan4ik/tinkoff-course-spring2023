package edu.java.scrapper.service;

import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.exceptions.UserAlreadyExistsException;
import edu.java.scrapper.api.services.ChatService;
import edu.java.scrapper.api.services.jpa.JpaChatService;
import edu.java.scrapper.domain.jpa.dao.ChatRepository;
import edu.java.scrapper.domain.jpa.dao.LinkRepository;
import edu.java.scrapper.integration.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JpaChatServiceTest extends IntegrationTest {
    private final JdbcTemplate jdbcTemplate;
    private final ChatService chatService;
    private static final String EXAMPLE_URL = "https://example.com/";
    private static final String EXAMPLE2_URL = "https://example2.com/";

    @Autowired
    public JpaChatServiceTest(
        JdbcTemplate jdbcTemplate,
        ChatRepository chatRepository,
        LinkRepository linkRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.chatService = new JpaChatService(chatRepository, linkRepository);
    }

    @Test
    @Transactional
    @Rollback
    void shouldRegisterChat() {
        chatService.registerChat(123L);

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

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> chatService.registerChat(123L));
    }

    @Test
    @Transactional
    @Rollback
    void shouldDeleteChat() {
        //given
        jdbcTemplate.update("insert into chat(id) values (123)");

        //when
        chatService.deleteChat(123L);

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
        chatService.deleteChat(123L);

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
        Assertions.assertThrows(ResourceNotFoundException.class, () -> chatService.deleteChat(123L));
    }
}
