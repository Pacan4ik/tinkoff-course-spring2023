package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.schema.ForkEventPayload;

@Service
public class ForkEventHandler extends AbstractEventHandler {
    private static final String EVENT_TYPE = "ForkEvent";
    private static final String FORK_DEFAULT = "Новый форк: %s %s";

    public ForkEventHandler(@Qualifier("handlersObjectMapper") ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String process(GitHubEventResponse event) {
        ForkEventPayload payload = convertTo(event, ForkEventPayload.class);
        return String.format(FORK_DEFAULT, payload.forkee().fullName(), payload.forkee().htmlUrl());
    }

    @Override
    protected boolean isSuitableEvent(GitHubEventResponse event) {
        return EVENT_TYPE.equals(event.type());
    }
}
