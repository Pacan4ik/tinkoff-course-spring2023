package edu.java.scrapper;

import edu.java.scrapper.domain.dao.ChatsRepository;
import edu.java.scrapper.domain.dto.ChatDto;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ChatsRepositoryTest extends IntegrationTest {
    @Autowired
    private ChatsRepository chatsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ChatDto.ChatDTORowMapper mapper;

    @Test
    @Transactional
    @Rollback
    void shouldAdd() {
        var dto = chatsRepository.add(0L);
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
        var dto = chatsRepository.find(0L).get();

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
        var dtoList = chatsRepository.findAll();

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
        var dtoList = chatsRepository.findAll(1L, 2L);

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
        chatsRepository.remove(1L);

        //then
        Assertions.assertTrue(
            jdbcTemplate.query("select * from chats where id = 1", mapper)
                .isEmpty()
        );
    }

}
