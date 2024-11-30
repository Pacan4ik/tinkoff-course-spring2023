package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.exceptions.NoSuitableHandlersException;

public abstract class AbstractEventHandler {
    @Setter
    protected AbstractEventHandler nextSuccessor;

    private final ObjectMapper objectMapper;

    protected AbstractEventHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected final <T> T convertTo(GitHubEventResponse event, Class<T> clazz) {
        return objectMapper.convertValue(event.payload(), clazz);
    }

    @Nullable
    public final String handle(GitHubEventResponse event) throws NoSuitableHandlersException {
        if (!isSuitableEvent(event)) {
            return handleNext(event);
        }
        return process(event);

    }

    protected final String handleNext(GitHubEventResponse event)
        throws NoSuitableHandlersException {
        if (nextSuccessor != null) {
            return nextSuccessor.handle(event);
        } else {
            throw new NoSuitableHandlersException("No suitable handler for " + event.type());
        }
    }

    @Nullable
    protected abstract String process(GitHubEventResponse event);

    protected abstract boolean isSuitableEvent(GitHubEventResponse event);
}
