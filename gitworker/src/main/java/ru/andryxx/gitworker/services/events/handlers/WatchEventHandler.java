package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.schema.WatchEventPayload;

@Service
public class WatchEventHandler extends AbstractEventHandler {
    private static final String EVENT_TYPE = "WatchEvent";
    private static final String WATCH_DEFAULT = "Новая звезда";
    public static final String STARTED = "started";

    public WatchEventHandler(@Qualifier("handlersObjectMapper") ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String process(GitHubEventResponse event) {
        WatchEventPayload payload = convertTo(event, WatchEventPayload.class);
        if (STARTED.equals(payload.action())) {
            return WATCH_DEFAULT;
        }
        return null;
    }

    @Override
    protected boolean isSuitableEvent(GitHubEventResponse event) {
        return EVENT_TYPE.equals(event.type());
    }
}
