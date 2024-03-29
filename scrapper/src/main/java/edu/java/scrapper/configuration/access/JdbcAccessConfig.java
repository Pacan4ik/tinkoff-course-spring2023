package edu.java.scrapper.configuration.access;

import edu.java.scrapper.api.services.ChatService;
import edu.java.scrapper.api.services.LinkService;
import edu.java.scrapper.api.services.jdbc.JdbcChatService;
import edu.java.scrapper.api.services.jdbc.JdbcLinkService;
import edu.java.scrapper.domain.jdbc.dao.ChatRepository;
import edu.java.scrapper.domain.jdbc.dao.LinkRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "data-base-access-type", havingValue = "jdbc")
public class JdbcAccessConfig {
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;

    public JdbcAccessConfig(ChatRepository chatRepository, LinkRepository linkRepository) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
    }

    @Bean
    public ChatService chatService() {
        return new JdbcChatService(chatRepository, linkRepository);
    }

    @Bean
    public LinkService linkService() {
        return new JdbcLinkService(linkRepository, chatRepository);
    }
}