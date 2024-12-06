package ru.andryxx.stackworker.services.handlers;

import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import ru.andryxx.stackworker.client.StackOverFlowResponse;
import ru.andryxx.stackworker.domain.model.StackEntity;
import ru.andryxx.stackworker.services.handlers.exceptions.NoSuitableHandlersException;

public abstract class AbstractEventHandler {
    @Setter
    protected AbstractEventHandler nextSuccessor;

    public final List<String> handle(StackOverFlowResponse event, StackEntity stackEntity, List<String> messages)
        throws NoSuitableHandlersException {
        setList(messages);
        if (!isSuitableEvent(event, stackEntity)) {
            return handleNext(event, stackEntity, messages);
        }
        return process(event, stackEntity, messages);

    }

    protected final List<String> handleNext(StackOverFlowResponse event, StackEntity stackEntity, List<String> messages)
        throws NoSuitableHandlersException {
        if (nextSuccessor != null) {
            return nextSuccessor.handle(event, stackEntity, messages);
        } else if (messages.isEmpty()) {
            throw new NoSuitableHandlersException("No suitable handler for " + event.items().getFirst().questionId());
        } else {
            return messages;
        }
    }

    //CHECKSTYLE:OFF
    protected final void setList(List<String> messages) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
    }
    //CHECKSTYLE:ON

    @Nullable
    protected abstract List<String> process(
        StackOverFlowResponse event,
        StackEntity stackEntity,
        List<String> messages
    ) throws NoSuitableHandlersException;

    protected abstract boolean isSuitableEvent(StackOverFlowResponse event, StackEntity stackEntity);
}
