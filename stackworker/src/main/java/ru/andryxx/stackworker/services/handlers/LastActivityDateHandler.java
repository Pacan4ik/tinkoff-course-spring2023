package ru.andryxx.stackworker.services.handlers;

import java.util.List;
import ru.andryxx.stackworker.client.StackOverFlowResponse;
import ru.andryxx.stackworker.domain.model.StackEntity;
import ru.andryxx.stackworker.services.handlers.exceptions.NoSuitableHandlersException;

// Should be last in chain
public class LastActivityDateHandler extends AbstractEventHandler {

    public static final String NEW_ACTIVITY = "Новая активность";

    @Override
    protected List<String> process(
        StackOverFlowResponse event,
        StackEntity stackEntity,
        List<String> messages
    ) throws NoSuitableHandlersException {
        if (messages.isEmpty()) {
            messages.add(NEW_ACTIVITY);
        }
        return handleNext(event, stackEntity, messages);
    }

    @Override
    protected boolean isSuitableEvent(StackOverFlowResponse event, StackEntity stackEntity) {
        return stackEntity.getLastActivityDate() == null
               || stackEntity.getLastActivityDate().isBefore(event.items().getFirst().lastActivityDate());
    }
}
