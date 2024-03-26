package edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ChatControllerTest extends IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void registerChat() throws Exception {
        mockMvc.perform(post("/tg-chat/{id}", 123))
            .andExpect(status().isOk());
    }

    @Test
    void deleteChat() throws Exception {
        mockMvc.perform(post("/tg-chat/{id}", 123));

        mockMvc.perform(delete("/tg-chat/{id}", 123))
            .andExpect(status().isOk());
    }

    @Test
    void notFoundIfDelete() throws Exception {
        mockMvc.perform(delete("/tg-chat/{id}", 12345))
            .andExpect(status().isNotFound());
    }
}
