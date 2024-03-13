package edu.java.scrapper;

import edu.java.scrapper.domain.dao.LinkChatAssignmentDao;
import edu.java.scrapper.domain.dto.LinkChatAssignmentDto;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class LinkChatAssignmentDaoTest extends IntegrationTest {
    @Autowired
    private LinkChatAssignmentDao linkChatAssignmentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LinkChatAssignmentDto.LinkChatAssignmentDTORowMapper mapper;

    private static final String EXAMPLE_URL = "https://example.com/";
    private static final String EXAMPLE2_URL = "https://example2.com/";

    @Test
    @Transactional
    @Rollback
    void shouldAdd() {
        //given
        jdbcTemplate.update("insert into links(id, url) values (123, ?)", EXAMPLE_URL);
        jdbcTemplate.update("insert into chats(id) values (1)");

        //when
        var dto = linkChatAssignmentDao.add(123L, 1L);
        Assertions.assertEquals(123L, dto.linkId());
        Assertions.assertEquals(1L, dto.chatId());
        Assertions.assertNotNull(dto.chatId());

        //then
        var queryDto = jdbcTemplate.queryForObject(
            "select * from link_chat_assignment where link_id = 123 and chat_id = 1",
            mapper
        );
        Assertions.assertEquals(queryDto, dto);
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindById() {
        //given
        jdbcTemplate.update("insert into links(id, url) values (123, ?), (1234, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update("insert into chats(id) values (1), (2), (3)");
        jdbcTemplate.update(
            "insert into link_chat_assignment(id, link_id, chat_id) values (1, 123, 1), (2, 1234, 2), (3, 123, 3)"
        );

        //when
        var dto = linkChatAssignmentDao.findById(2L).get();

        //then
        Assertions.assertEquals(new LinkChatAssignmentDto(2L, 1234L, 2L), dto);
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindByChatId() {
        //given
        jdbcTemplate.update("insert into links(id, url) values (123, ?), (1234, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update("insert into chats(id) values (1), (2)");
        jdbcTemplate.update(
            "insert into link_chat_assignment(id, link_id, chat_id) values (1, 123, 1), (2, 1234, 2), (3, 123, 2)"
        );

        //when
        var dtoList = linkChatAssignmentDao.findByChatId(2L);

        //then
        Assertions.assertEquals(
            List.of(
                new LinkChatAssignmentDto(2L, 1234L, 2L),
                new LinkChatAssignmentDto(3L, 123L, 2L)
            ),
            dtoList
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindByLinkId() {
        //given
        jdbcTemplate.update("insert into links(id, url) values (123, ?), (1234, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update("insert into chats(id) values (1), (2), (3)");
        jdbcTemplate.update(
            "insert into link_chat_assignment(id, link_id, chat_id) values (1, 123, 1), (2, 1234, 2), (3, 123, 3)"
        );

        //when
        var dtoList = linkChatAssignmentDao.findByLinkId(123L);

        //then
        Assertions.assertEquals(
            List.of(
                new LinkChatAssignmentDto(1L, 123L, 1L),
                new LinkChatAssignmentDto(3L, 123L, 3L)
            ),
            dtoList
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindByLinkIdAndChatID() {
        //given
        jdbcTemplate.update("insert into links(id, url) values (123, ?), (1234, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update("insert into chats(id) values (1), (2)");
        jdbcTemplate.update(
            "insert into link_chat_assignment(id, link_id, chat_id) values (1, 123, 1), (2, 1234, 2), (3, 123, 2)"
        );

        //when
        var dto = linkChatAssignmentDao.findByLinkIdAndChatId(123L, 2L).get();

        //then
        Assertions.assertEquals(new LinkChatAssignmentDto(3L, 123L, 2L), dto);
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindAll() {
        //given
        jdbcTemplate.update("insert into links(id, url) values (123, ?), (1234, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update("insert into chats(id) values (1), (2), (3)");
        jdbcTemplate.update(
            "insert into link_chat_assignment(id, link_id, chat_id) values (1, 123, 1), (2, 1234, 2), (3, 123, 3)"
        );

        //when
        var dtoList = linkChatAssignmentDao.findAll();

        //then
        var queryDto = jdbcTemplate.query("select * from link_chat_assignment", mapper);
        Assertions.assertEquals(queryDto, dtoList);
    }

    @Test
    @Transactional
    @Rollback
    void shouldDelete() {
        //given
        jdbcTemplate.update("insert into links(id, url) values (123, ?), (1234, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update("insert into chats(id) values (1)");
        jdbcTemplate.update(
            "insert into link_chat_assignment(id, link_id, chat_id) values (1, 123, 1), (2, 1234, 1)"
        );

        //when
        linkChatAssignmentDao.remove(2L);

        //then
        Assertions.assertTrue(
            jdbcTemplate.query("select * from link_chat_assignment where id = 2", mapper)
                .isEmpty()
        );
    }

    @Test
    @Transactional
    @Rollback
    void shouldDeleteBySeveralIds() {
        //given
        jdbcTemplate.update("insert into links(id, url) values (123, ?), (1234, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update("insert into chats(id) values (1), (2), (3)");
        jdbcTemplate.update(
            "insert into link_chat_assignment(id, link_id, chat_id) values (1, 123, 1), (2, 1234, 2), (3, 123, 3)"
        );

        //when
        linkChatAssignmentDao.remove(2L, 3L);

        //then
        Assertions.assertTrue(
            jdbcTemplate.query("select * from link_chat_assignment where id = 2 or id = 3", mapper)
                .isEmpty()
        );
    }
}
