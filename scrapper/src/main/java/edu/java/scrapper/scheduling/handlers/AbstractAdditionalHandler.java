package edu.java.scrapper.scheduling.handlers;

import edu.java.scrapper.domain.adapters.LinkInfoDto;
import java.util.function.Consumer;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Setter
public abstract class AbstractAdditionalHandler<T> {
    protected AbstractAdditionalHandler<T> nextSuccessor;

    public abstract AdditionalHandlerResult handle(
        T response,
        LinkInfoDto.AdditionalInfo additionalInfo,
        AdditionalHandlerResult result
    );

    protected final AdditionalHandlerResult handleNext(
        T response,
        LinkInfoDto.AdditionalInfo additionalInfo,
        AdditionalHandlerResult result
    ) {
        if (nextSuccessor != null) {
            return nextSuccessor.handle(response, additionalInfo, result);
        }
        return result;
    }

    protected Consumer<LinkInfoDto.AdditionalInfo> addToConsumer(
        @NotNull AdditionalHandlerResult result,
        @NotNull Consumer<LinkInfoDto.AdditionalInfo> consumer
    ) {
        return result.getAdditionalInfoConsumer()
            .andThen(consumer);
    }
}
