package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.schema.IssueCommentEventPayload;

@Service
public class IssueCommentEventHandler extends AbstractEventHandler {
    private static final String EVENT_TYPE = "IssueCommentEvent";
    private static final String ISSUE_COMMENT_DEFAULT = "Комментарий к issue: %s %s %s";

    public IssueCommentEventHandler(@Qualifier("handlersObjectMapper") ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String process(GitHubEventResponse event) {
        IssueCommentEventPayload payload = convertTo(event, IssueCommentEventPayload.class);
        return String.format(
            ISSUE_COMMENT_DEFAULT,
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
