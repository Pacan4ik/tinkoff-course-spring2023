package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.schema.CreateEventPayload;

@Service
public class CreateEventHandler extends AbstractEventHandler {
    private static final String EVENT_TYPE = "CreateEvent";
    public static final String CREATE_DEFAULT = "Создан новый";

    public CreateEventHandler(@Qualifier("handlersObjectMapper") ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String process(GitHubEventResponse event) {
        CreateEventPayload payload = convertTo(event, CreateEventPayload.class);
        return CREATE_DEFAULT + payload.refType() + " " + payload.ref();
    }

    @Override
    protected boolean isSuitableEvent(GitHubEventResponse event) {
        return EVENT_TYPE.equals(event.type());
    }
}
