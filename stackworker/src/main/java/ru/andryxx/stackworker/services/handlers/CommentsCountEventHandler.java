package ru.andryxx.stackworker.services.handlers;

import java.util.List;
import ru.andryxx.stackworker.client.StackOverFlowResponse;
import ru.andryxx.stackworker.domain.model.StackEntity;
import ru.andryxx.stackworker.services.handlers.exceptions.NoSuitableHandlersException;

public class CommentsCountEventHandler extends AbstractEventHandler {

    public static final String NEW_COMMENT = "Новый комментарий";

    @Override
    protected List<String> process(
        StackOverFlowResponse event,
        StackEntity stackEntity,
        List<String> messages
    ) throws NoSuitableHandlersException {
        messages.add(NEW_COMMENT);
        return handleNext(event, stackEntity, messages);
    }

    @Override
    protected boolean isSuitableEvent(StackOverFlowResponse event, StackEntity stackEntity) {
        return stackEntity.getCommentCount() == null
               || stackEntity.getCommentCount() < event.items().getFirst().commentCount();
    }
}
