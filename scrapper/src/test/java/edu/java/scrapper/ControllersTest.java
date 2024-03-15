package edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllersTest extends IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String EXAMPLE_URL = "https://example.com";
    private static final String EXAMPLE2_URL = "https://example2.com";

    @Test
    @Transactional
    @Rollback
    void registerChat() throws Exception {
        mockMvc.perform(post("/tg-chat/{id}", 123))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback
    void conflictOnRegisterChat() throws Exception {
        jdbcTemplate.update("insert into chat(id) values (123)");
        mockMvc.perform(post("/tg-chat/{id}", 123))
            .andExpect(status().isConflict());
    }

    @Test
    @Transactional
    @Rollback
    void deleteChat() throws Exception {
        jdbcTemplate.update("insert into chat(id) values (123)");

        mockMvc.perform(delete("/tg-chat/{id}", 123))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback
    void notFoundIfDeleteChat() throws Exception {
        mockMvc.perform(delete("/tg-chat/{id}", 12345))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback
    void getLinks() throws Exception {
        jdbcTemplate.update("insert into link(id, url) values (1, ?), (2, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update("insert into chat(id) values (123)");
        jdbcTemplate.update("insert into link_chat_assignment (link_id, chat_id) values (1, 123), (2, 123)");

        mockMvc.perform(get("/links").header("Tg-Chat-Id", 123L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.links[0].url").exists())
            .andExpect(jsonPath("$.links[0].url").value(EXAMPLE_URL))
            .andExpect(jsonPath("$.links[0].id").exists())
            .andExpect(jsonPath("$.links[0].id").value(1))
            .andExpect(jsonPath("$.links[1].url").exists())
            .andExpect(jsonPath("$.links[1].url").value(EXAMPLE2_URL))
            .andExpect(jsonPath("$.links[1].id").exists())
            .andExpect(jsonPath("$.links[1].id").value(2))
            .andExpect(jsonPath("$.size").value(2));

    }

    @Test
    @Transactional
    @Rollback
    void notFoundOnGetLinks() throws Exception {
        mockMvc.perform(get("/links").header("Tg-Chat-Id", 123L))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback
    void postLinks() throws Exception {
        jdbcTemplate.update("insert into chat(id) values (123)");

        mockMvc.perform(post("/links")
                .header("Tg-Chat-Id", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "link": "https://example.com"
                    }
                    """
                )
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.url").exists())
            .andExpect(jsonPath("$.url").value("https://example.com")
            );

    }

    @Test
    @Transactional
    @Rollback
    void notFoundOnPostLinks() throws Exception {
        mockMvc.perform(post("/links")
            .header("Tg-Chat-Id", 123L)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "link": "https://example.com"
                }
                """
            )
        ).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback
    void conflictOnPostLink() throws Exception {
        jdbcTemplate.update("insert into link(id, url) values (1, ?)", EXAMPLE_URL);
        jdbcTemplate.update("insert into chat(id) values (123)");
        jdbcTemplate.update("insert into link_chat_assignment (link_id, chat_id) values (1, 123)");

        mockMvc.perform(post("/links")
            .header("Tg-Chat-Id", 123L)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "link": "https://example.com"
                }
                """
            )
        ).andExpect(status().isConflict());
    }

    @Test
    @Transactional
    @Rollback
    void shouldDeleteLink() throws Exception {
        jdbcTemplate.update("insert into link(id, url) values (1, ?), (2, ?)", EXAMPLE_URL, EXAMPLE2_URL);
        jdbcTemplate.update("insert into chat(id) values (123)");
        jdbcTemplate.update("insert into link_chat_assignment (link_id, chat_id) values (1, 123), (2, 123)");

        mockMvc.perform(delete("/links")
                .header("Tg-Chat-Id", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "link": "https://example.com"
                    }
                    """
                )
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.url").value(EXAMPLE_URL));
    }

    @Test
    @Transactional
    @Rollback
    void notFoundOnDeleteLinks() throws Exception {
        mockMvc.perform(delete("/links")
            .header("Tg-Chat-Id", 123L)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "link": "https://example.com"
                }
                """
            )
        ).andExpect(status().isNotFound());
    }

}
