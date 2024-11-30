package ru.andryxx.stackworker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.andryxx.stackworker.services.handlers.AbstractEventHandler;
import ru.andryxx.stackworker.services.handlers.AnswersCountEventHandler;
import ru.andryxx.stackworker.services.handlers.CommentsCountEventHandler;
import ru.andryxx.stackworker.services.handlers.LastActivityDateHandler;


@Configuration
public class EventHandlersConfig {
    @Bean("stackChain")
    public AbstractEventHandler stackChain() {
        AbstractEventHandler answersHandler = new AnswersCountEventHandler();
        AbstractEventHandler commentsHandler = new CommentsCountEventHandler();
        AbstractEventHandler lastActivityHandler = new LastActivityDateHandler();

        answersHandler.setNextSuccessor(commentsHandler);
        commentsHandler.setNextSuccessor(lastActivityHandler);
        return answersHandler;
    }
}
