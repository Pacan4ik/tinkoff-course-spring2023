package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;

@Service
public class GollumEventHandler extends AbstractEventHandler {
    private static final String EVENT_TYPE = "GollumEvent";
    private static final String GOLLUM_DEFAULT = "Обновлена страница вики";

    public GollumEventHandler(@Qualifier("handlersObjectMapper") ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String process(GitHubEventResponse event) {
        return GOLLUM_DEFAULT;
    }

    @Override
    protected boolean isSuitableEvent(GitHubEventResponse event) {
        return EVENT_TYPE.equals(event.type());
    }
}
