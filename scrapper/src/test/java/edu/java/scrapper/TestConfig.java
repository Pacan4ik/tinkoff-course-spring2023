package edu.java.scrapper;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import java.net.URI;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    ChatRepository chatRepository() {
        ChatRepository chatRepository = new ChatRepository();
        chatRepository.addLink(1, URI.create("https://example.com/"));
        return chatRepository;
    }
}
