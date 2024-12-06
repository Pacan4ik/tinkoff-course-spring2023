package ru.andryxx.habrworker.services.handlers;

import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import ru.andryxx.habrworker.domain.model.HabrEntity;
import ru.andryxx.habrworker.services.fetchers.model.HabrDTO;
import ru.andryxx.habrworker.services.handlers.exceptions.NoSuitableHandlersException;

public class ContentHashHandler extends AbstractHandler {

    public static final String ARTICLE_HAS_BEEN_CHANGED = "Статья была изменена";

    @Override
    protected @Nullable List<String> process(HabrDTO event, HabrEntity habrEntity, List<String> messages)
        throws NoSuitableHandlersException {
        messages.add(ARTICLE_HAS_BEEN_CHANGED);
        return handleNext(event, habrEntity, messages);
    }

    @Override
    protected boolean isSuitableEvent(HabrDTO event, HabrEntity habrEntity) {
        return habrEntity.getContentSha1Hash() != null
               && !Objects.equals(event.sha1Hash(), habrEntity.getContentSha1Hash());
    }
}
