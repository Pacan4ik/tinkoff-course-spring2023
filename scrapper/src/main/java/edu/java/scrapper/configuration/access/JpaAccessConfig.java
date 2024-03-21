package edu.java.scrapper.configuration.access;

import edu.java.scrapper.api.services.ChatService;
import edu.java.scrapper.api.services.LinkService;
import edu.java.scrapper.api.services.jpa.JpaChatService;
import edu.java.scrapper.api.services.jpa.JpaLinkService;
import edu.java.scrapper.domain.jpa.dao.ChatRepository;
import edu.java.scrapper.domain.jpa.dao.LinkRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "data-base-access-type", havingValue = "jpa")
public class JpaAccessConfig {
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;

    public JpaAccessConfig(ChatRepository chatRepository, LinkRepository linkRepository) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
    }

    @Bean
    public ChatService chatService() {
        return new JpaChatService(chatRepository, linkRepository);
    }

    @Bean
    public LinkService linkService() {
        return new JpaLinkService(chatRepository, linkRepository);
    }
}
