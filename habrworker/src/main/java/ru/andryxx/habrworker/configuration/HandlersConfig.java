package ru.andryxx.habrworker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.andryxx.habrworker.services.handlers.AbstractHandler;
import ru.andryxx.habrworker.services.handlers.CommentCountHandler;
import ru.andryxx.habrworker.services.handlers.ContentHashHandler;

@Configuration
public class HandlersConfig {
    @Bean("habrChain")
    public AbstractHandler habrChain() {
        CommentCountHandler commentCountHandler = new CommentCountHandler();
        ContentHashHandler contentHashHandler = new ContentHashHandler();
        commentCountHandler.setNextSuccessor(contentHashHandler);
        return commentCountHandler;
    }
}
