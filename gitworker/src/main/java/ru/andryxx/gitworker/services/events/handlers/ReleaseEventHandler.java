package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.schema.ReleaseEventPayload;

@Service
public class ReleaseEventHandler extends AbstractEventHandler {
    private static final String EVENT_TYPE = "ReleaseEvent";
    private static final String RELEASE_DEFAULT = "Новый релиз: %s";
    public static final String PUBLISHED = "published";

    public ReleaseEventHandler(@Qualifier("handlersObjectMapper") ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String process(GitHubEventResponse event) {
        ReleaseEventPayload payload = convertTo(event, ReleaseEventPayload.class);
        if (payload.action().equals(PUBLISHED)) {
            return String.format(RELEASE_DEFAULT, payload.release().htmlUrl());
        }
        return String.format(RELEASE_DEFAULT, payload.release().htmlUrl()) + " " + payload.action();
    }

    @Override
    protected boolean isSuitableEvent(GitHubEventResponse event) {
        return EVENT_TYPE.equals(event.type());
    }
}
