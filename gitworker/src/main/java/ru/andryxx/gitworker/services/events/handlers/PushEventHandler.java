package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.schema.PushEventPayload;

@Service
public class PushEventHandler extends AbstractEventHandler{
    private static final String EVENT_TYPE = "PushEvent";
    private static final String PUSH_DEFAULT = "Новый push %s";

    public PushEventHandler(@Qualifier("handlersObjectMapper") ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String process(GitHubEventResponse event) {
        PushEventPayload payload = convertTo(event, PushEventPayload.class);
        return String.format(PUSH_DEFAULT, payload.ref());
    }

    @Override
    protected boolean isSuitableEvent(GitHubEventResponse event) {
        return EVENT_TYPE.equals(event.type());
    }
}
