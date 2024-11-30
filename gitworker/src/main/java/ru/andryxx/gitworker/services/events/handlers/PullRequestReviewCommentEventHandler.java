package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.schema.PullRequestReviewCommentEventPayload;

@Service
public class PullRequestReviewCommentEventHandler extends AbstractEventHandler {
    private static final String EVENT_TYPE = "PullRequestReviewCommentEvent";
    private static final String PULL_REQUEST_REVIEW_NEW_COMMENT = "Комментарий к пулл-реквесту: %s %s";
    private static final String PULL_REQUEST_REVIEW_COMMENT_DEFAULT = "Комментарий к пулл-реквесту %s: %s %s";
    public static final String CREATED = "created";

    public PullRequestReviewCommentEventHandler(@Qualifier("handlersObjectMapper") ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String process(GitHubEventResponse event) {
        PullRequestReviewCommentEventPayload payload = convertTo(event, PullRequestReviewCommentEventPayload.class);
        if (payload.action().equals(CREATED)) {
            return String.format(
                PULL_REQUEST_REVIEW_NEW_COMMENT,
                payload.pullRequest().title(),
                payload.pullRequest().htmlUrl()
            );
        }
        return String.format(
            PULL_REQUEST_REVIEW_COMMENT_DEFAULT,
            payload.action(),
            payload.pullRequest().title(),
            payload.pullRequest().htmlUrl()
        );
    }

    @Override
    protected boolean isSuitableEvent(GitHubEventResponse event) {
        return EVENT_TYPE.equals(event.type());
    }
}
