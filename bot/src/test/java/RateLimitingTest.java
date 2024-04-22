import edu.java.bot.api.controllers.DefaultUpdateController;
import edu.java.bot.api.exceptions.handlers.GlobalExceptionHandler;
import edu.java.bot.api.services.DefaultUpdateHandlerService;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.RateLimitConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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
@Import(value = {DefaultUpdateController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class RateLimitingTest {
    public static final String CONTENT = """
        {
          "id": 0,
          "url": "string",
          "description": "string",
          "tgChatIds": [
            123
          ]
        }
            """;
    @MockBean DefaultUpdateHandlerService chatService;
    @Autowired MockMvc mockMvc;

    @Test
    void shouldBlock() throws Exception {
        mockMvc.perform(post("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CONTENT))
            .andExpect(status().isOk());

        mockMvc.perform(post("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CONTENT))
            .andExpect(status().isOk());

        mockMvc.perform(post("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CONTENT))
            .andExpect(status().isOk());

        mockMvc.perform(post("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CONTENT))
            .andExpect(status().isTooManyRequests());
    }

}


