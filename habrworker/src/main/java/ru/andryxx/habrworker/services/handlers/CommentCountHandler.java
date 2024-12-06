package ru.andryxx.habrworker.services.handlers;

import org.jetbrains.annotations.Nullable;
import ru.andryxx.habrworker.domain.model.HabrEntity;
import ru.andryxx.habrworker.services.fetchers.model.HabrDTO;
import ru.andryxx.habrworker.services.handlers.exceptions.NoSuitableHandlersException;
import java.util.List;

public class CommentCountHandler extends AbstractHandler {

    public static final String NEW_COMMENT = "Новый комментарий";

    @Override
    protected @Nullable List<String> process(HabrDTO event, HabrEntity habrEntity, List<String> messages)
        throws NoSuitableHandlersException {
        messages.add(NEW_COMMENT);
        return handleNext(event, habrEntity, messages);
    }

    @Override
    protected boolean isSuitableEvent(HabrDTO event, HabrEntity habrEntity) {
        return event.commentCount() != null && habrEntity.getCommentCount() > event.commentCount();
    }
}
