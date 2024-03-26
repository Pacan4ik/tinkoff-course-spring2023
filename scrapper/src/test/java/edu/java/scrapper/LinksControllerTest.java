package edu.java.scrapper;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LinksControllerTest extends IntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @Disabled
    void getLinks() throws Exception {
        var ans = mockMvc.perform(get("/links").header("Tg-Chat-ID", 1L));
        ans.andExpect(status().isOk());
    }
}
