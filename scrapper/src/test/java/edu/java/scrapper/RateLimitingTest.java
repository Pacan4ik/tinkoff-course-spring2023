package edu.java.scrapper;

import edu.java.scrapper.api.controllers.DefaultChatController;
import edu.java.scrapper.api.exceptions.handlers.GlobalExceptionHandler;
import edu.java.scrapper.api.services.ChatService;
import edu.java.scrapper.api.services.LinkService;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.configuration.RateLimitConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableConfigurationProperties(value = ApplicationConfig.class)
@TestPropertySource(properties = {
    "app.rate-limiting.requests-limit=3",
    "app.rate-limiting.time-duration=1m",
})
@ContextConfiguration(classes = {RateLimitConfig.class})
@Import(value = {DefaultChatController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class RateLimitingTest {
    @MockBean ChatService chatService;
    @MockBean LinkService linkService;
    @Autowired MockMvc mockMvc;

    @Test
    void shouldBlock() throws Exception {
        mockMvc.perform(post("/tg-chat/{id}", 123))
            .andExpect(status().isOk());

        mockMvc.perform(post("/tg-chat/{id}", 123))
            .andExpect(status().isOk());

        mockMvc.perform(post("/tg-chat/{id}", 123))
            .andExpect(status().isOk());

        mockMvc.perform(post("/tg-chat/{id}", 123))
            .andExpect(status().isTooManyRequests());
    }

}
