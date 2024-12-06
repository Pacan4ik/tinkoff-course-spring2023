package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.schema.CommitCommentEventPayload;

@Service
public class CommitCommentEventHandler extends AbstractEventHandler {

    private static final String EVENT_TYPE = "CommitCommentEvent";
    public static final String USER_COMMENT_ON_COMMIT = "Пользователь %s оставил комментарий on commit: %s";

    public CommitCommentEventHandler(@Qualifier("handlersObjectMapper") ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String process(GitHubEventResponse event) {
        CommitCommentEventPayload payload = convertTo(event, CommitCommentEventPayload.class);
        return String.format(USER_COMMENT_ON_COMMIT, payload.comment().user().login(), payload.comment().htmlUrl());
    }

    @Override
    protected boolean isSuitableEvent(GitHubEventResponse event) {
        return EVENT_TYPE.equals(event.type());
    }
}
