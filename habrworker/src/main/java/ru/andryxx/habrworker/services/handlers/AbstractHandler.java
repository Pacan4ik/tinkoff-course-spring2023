package ru.andryxx.habrworker.services.handlers;

import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import ru.andryxx.habrworker.domain.model.HabrEntity;
import ru.andryxx.habrworker.services.fetchers.model.HabrDTO;
import ru.andryxx.habrworker.services.handlers.exceptions.NoSuitableHandlersException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHandler {
    @Setter
    protected AbstractHandler nextSuccessor;

    public final List<String> handle(HabrDTO event, HabrEntity habrEntity, List<String> messages)
        throws NoSuitableHandlersException {
        setList(messages);
        if (!isSuitableEvent(event, habrEntity)) {
            return handleNext(event, habrEntity, messages);
        }
        return process(event, habrEntity, messages);

    }

    protected final List<String> handleNext(HabrDTO event, HabrEntity habrEntity, List<String> messages)
        throws NoSuitableHandlersException {
        if (nextSuccessor != null) {
            return nextSuccessor.handle(event, habrEntity, messages);
        } else if (messages.isEmpty()) {
            throw new NoSuitableHandlersException("No suitable handlers");
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
        HabrDTO event,
        HabrEntity habrEntity,
        List<String> messages
    ) throws NoSuitableHandlersException;

    protected abstract boolean isSuitableEvent(HabrDTO event, HabrEntity habrEntity);
}
