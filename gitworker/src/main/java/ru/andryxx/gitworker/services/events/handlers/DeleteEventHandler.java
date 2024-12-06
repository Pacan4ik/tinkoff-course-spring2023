package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.schema.DeleteEventPayload;

@Service
public class DeleteEventHandler extends AbstractEventHandler {
    private static final String EVENT_TYPE = "DeleteEvent";
    private static final String DELETE_DEFAULT = "Удален ";

    public DeleteEventHandler(@Qualifier("handlersObjectMapper") ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String process(GitHubEventResponse event) {
        DeleteEventPayload payload = convertTo(event, DeleteEventPayload.class);
        return DELETE_DEFAULT + payload.refType();
    }

    @Override
    protected boolean isSuitableEvent(GitHubEventResponse event) {
        return EVENT_TYPE.equals(event.type());
    }
}
