package edu.java.scrapper;

import edu.java.scrapper.api.repositories.JdbcChatRepository;
import edu.java.scrapper.domain.dao.ChatDao;
import edu.java.scrapper.domain.dto.ChatDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
public class JdbcChatTest extends IntegrationTest {
    @Autowired
    private ChatDao chatDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ChatDto.ChatDTORowMapper mapper = new ChatDto.ChatDTORowMapper();

    @Test
    @Transactional
    @Rollback
    void shouldAdd() {
        var dto = chatDao.add(0L);
        Assertions.assertEquals(0L, dto.id());
        Assertions.assertNotNull(dto.registeredAt());

        var dtoQuery = jdbcTemplate.queryForObject("select * from chats where id = 0", mapper);
        Assertions.assertEquals(dto, dtoQuery);
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindBySingleId() {
        //given
        jdbcTemplate.update("insert into chats(id) values (0), (1), (2)");

        //when
        var dto = chatDao.find(0L).get();

        //then
        var dtoQuery = jdbcTemplate.queryForObject("select * from chats where id = 0", mapper);
        Assertions.assertEquals(dto, dtoQuery);
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindAll() {
        //given
        jdbcTemplate.update("insert into chats(id) values (0), (1), (2)");

        //when
        var dtoList = chatDao.findAll();

        //then
        Assertions.assertEquals(dtoList.stream().map(ChatDto::id).toList(), List.of(0L, 1L, 2L));
    }

    @Test
    @Transactional
    @Rollback
    void shouldFindAllWithSeveralIds() {
        //given
        jdbcTemplate.update("insert into chats(id) values (0), (1), (2)");

        //when
        var dtoList = chatDao.findAll(1L, 2L);

        //then
        Assertions.assertEquals(dtoList.stream().map(ChatDto::id).toList(), List.of(1L, 2L));
    }

    @Test
    @Transactional
    @Rollback
    void shouldDelete() {
        //given
        jdbcTemplate.update("insert into chats(id) values (0), (1), (2)");

        //when
        chatDao.remove(1L);

        //then
        Assertions.assertTrue(
            jdbcTemplate.query("select * from chats where id = 1", mapper)
                .isEmpty()
        );
    }

}
