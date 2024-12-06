package ru.andryxx.gitworker.services.events;

import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.AbstractEventHandler;
import ru.andryxx.gitworker.services.events.handlers.exceptions.NoSuitableHandlersException;

@Service
@Slf4j
public class EventsProcessor {
    private final AbstractEventHandler eventHandlerChain;

    public EventsProcessor(@Qualifier("eventHandlerChain") AbstractEventHandler eventHandlerChain) {
        this.eventHandlerChain = eventHandlerChain;

    }

    @Nullable
    public String processEvents(List<GitHubEventResponse> events) {
        StringBuilder sb = new StringBuilder();
        for (GitHubEventResponse event : events) {
            String eventResult = "";
            try {
                eventResult = Objects.requireNonNullElse(eventHandlerChain.handle(event), "");
            } catch (NoSuitableHandlersException e) {
                log.error("No suitable handlers found for event: {}", event);
            }
            sb.append(eventResult).append("\n");
        }
        String result = sb.toString();
        return result.isEmpty() ? null : result;
    }
}
