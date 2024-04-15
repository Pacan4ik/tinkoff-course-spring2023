package edu.java.scrapper.configuration.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.api.services.ChatService;
import edu.java.scrapper.api.services.LinkService;
import edu.java.scrapper.api.services.jooq.JooqChatService;
import edu.java.scrapper.api.services.jooq.JooqLinkService;
import edu.java.scrapper.domain.adapters.JooqLinkInfoAdapter;
import edu.java.scrapper.domain.adapters.LinkInfoAdapter;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "data-base-access-type", havingValue = "jooq")
@RequiredArgsConstructor
public class JooqAccessConfig {
    private final DSLContext context;
    private final ObjectMapper objectMapper;

    @Bean
    public ChatService chatService() {
        return new JooqChatService(context);
    }

    @Bean
    public LinkService linkService() {
        return new JooqLinkService(context);
    }

    @Bean
    public LinkInfoAdapter linkInfoAdapter() {
        return new JooqLinkInfoAdapter(context, objectMapper);
    }
}
