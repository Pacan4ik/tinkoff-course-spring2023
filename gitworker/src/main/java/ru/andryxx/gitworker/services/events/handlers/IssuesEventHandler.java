package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.schema.IssuesEvent;

@Service
public class IssuesEventHandler extends AbstractEventHandler {
    private static final String EVENT_TYPE = "IssuesEvent";
    private static final String ISSUE_DEFAULT = "Изменения состояния issue: %s %s %s";

    public IssuesEventHandler(@Qualifier("handlersObjectMapper") ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String process(GitHubEventResponse event) {
        IssuesEvent payload = convertTo(event, IssuesEvent.class);
        return String.format(
            ISSUE_DEFAULT,
            payload.issue().title(),
            payload.action(),
            payload.issue().htmlUrl()
        );
    }

    @Override
    protected boolean isSuitableEvent(GitHubEventResponse event) {
        return EVENT_TYPE.equals(event.type());
    }
}
