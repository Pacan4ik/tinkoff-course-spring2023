package edu.java.scrapper.repository;

import edu.java.scrapper.domain.jdbc.dao.ChatRepository;
import edu.java.scrapper.domain.jdbc.dto.ChatDto;
import java.util.List;

import edu.java.scrapper.integration.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ChatRepositoryTest extends IntegrationTest {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ChatDto.ChatDTORowMapper mapper;

    private static final String EXAMPLE_URL = "https://example.com/";
    private static final String EXAMPLE2_URL = "https://example2.com/";

    @Test
    @Transactional
    @Rollback
    void shouldAdd() {
        var dto = chatRepository.add(0L);
        Assertions.assertEquals(0L, dto.id());
        Assertions.assertNotNull(dto.registeredAt());

        var dtoQuery = jdbcTemplate.queryForObject("select * from chat where id = 0", mapper);
        Assertions.assertEquals(dto, dtoQuery);
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindBySingleId() {
        //given
        jdbcTemplate.update("insert into chat(id) values (0), (1), (2)");

        //when
        var dto = chatRepository.find(0L).get();

        //then
        var dtoQuery = jdbcTemplate.queryForObject("select * from chat where id = 0", mapper);
        Assertions.assertEquals(dto, dtoQuery);
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindAll() {
        //given
        jdbcTemplate.update("insert into chat(id) values (0), (1), (2)");

        //when
        var dtoList = chatRepository.findAll();

        //then
        Assertions.assertEquals(dtoList.stream().map(ChatDto::id).toList(), List.of(0L, 1L, 2L));
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindAllWithSeveralIds() {
        //given
        jdbcTemplate.update("insert into chat(id) values (0), (1), (2)");

        //when
        var dtoList = chatRepository.findAll(1L, 2L);

        //then
        Assertions.assertEquals(dtoList.stream().map(ChatDto::id).toList(), List.of(1L, 2L));
    }

    @Test
    @Transactional
    @Rollback
    void shouldDelete() {
        //given
        jdbcTemplate.update("insert into chat(id) values (0), (1), (2)");

        //when
        chatRepository.remove(1L);

        //then
        Assertions.assertTrue(
            jdbcTemplate.query("select * from chat where id = 1", mapper)
                .isEmpty()
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldAddLink() {
        //given
        jdbcTemplate.update("insert into link(id, url) values (123, ?)", EXAMPLE_URL);
        jdbcTemplate.update("insert into chat(id) values (1)");

        //when
        chatRepository.addLink(1L, 123L);

        //then
        Assertions.assertEquals(
            123L,
            jdbcTemplate.queryForObject("select link_id from link_chat_assignment where chat_id = 1", Long.class)
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldReturnAssignedChatsByLinkId() {
        //given
        jdbcTemplate.update("insert into link(id, url) values (123, ?)", EXAMPLE_URL);
        jdbcTemplate.update("insert into chat(id) values (1), (2), (3)");
        jdbcTemplate.update("insert into link_chat_assignment(link_id, chat_id) values (123, 1), (123, 2), (123, 3)");

        //when
        List<ChatDto> chats = chatRepository.getAllChats(123L);

        //then
        Assertions.assertEquals(
            List.of(1L, 2L, 3L),
            chats.stream().map(ChatDto::id).toList()
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldDeleteAssignedLink() {
        //given
        jdbcTemplate.update("insert into link(id, url) values (1, ?)", EXAMPLE_URL);
        jdbcTemplate.update("insert into chat(id) values (123)");
        jdbcTemplate.update("insert into link_chat_assignment(link_id, chat_id) values (1, 123)");

        //when
        chatRepository.removeLink(123L, 1L);

        //then
        Assertions.assertTrue(
            jdbcTemplate.queryForList("select * from link_chat_assignment where chat_id = 123", Long.class)
                .isEmpty()
        );
    }

}
