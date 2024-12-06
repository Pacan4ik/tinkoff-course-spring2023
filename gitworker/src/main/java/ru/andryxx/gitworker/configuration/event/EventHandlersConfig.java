package ru.andryxx.gitworker.configuration.event;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.andryxx.gitworker.services.events.handlers.AbstractEventHandler;

@Configuration
public class EventHandlersConfig {
    @Bean("eventHandlerChain")
    public AbstractEventHandler eventHandlerChain(List<AbstractEventHandler> eventHandlers) {
        if (eventHandlers.isEmpty()) {
            throw new IllegalArgumentException("No event handlers found");
        }
        for (int i = 0; i < eventHandlers.size() - 1; i++) {
            eventHandlers.get(i).setNextSuccessor(eventHandlers.get(i + 1));
        }
        return eventHandlers.getFirst();
    }
}
