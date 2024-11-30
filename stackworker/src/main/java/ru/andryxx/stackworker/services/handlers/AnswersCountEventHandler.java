package ru.andryxx.stackworker.services.handlers;

import java.util.List;
import ru.andryxx.stackworker.client.StackOverFlowResponse;
import ru.andryxx.stackworker.domain.model.StackEntity;

public class AnswersCountEventHandler extends AbstractEventHandler {

    public static final String NEW_ANSWER = "Новый ответ";

    @Override
    protected List<String> process(
        StackOverFlowResponse event,
        StackEntity stackEntity,
        List<String> messages
    ) {
        messages.add(NEW_ANSWER);
        return messages;
    }

    @Override
    protected boolean isSuitableEvent(StackOverFlowResponse event, StackEntity stackEntity) {
        return stackEntity.getAnswerCount() == null
               || stackEntity.getAnswerCount() < event.items().getFirst().answerCount();
    }
}
