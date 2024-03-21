package edu.java.scrapper.scheduling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import edu.java.scrapper.domain.jdbc.dao.LinkRepository;
import edu.java.scrapper.domain.jdbc.dto.LinkDto;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Setter
public abstract class AbstractAdditionalHandler<T> {
    protected AbstractAdditionalHandler<T> nextSuccessor;

    public abstract AdditionalHandlerResult handle(
        T response,
        LinkDto linkDto,
        AdditionalHandlerResult result
    );

    protected final AdditionalHandlerResult handleNext(
        T response,
        LinkDto linkDto,
        AdditionalHandlerResult result
    ) {
        if (nextSuccessor != null) {
            return nextSuccessor.handle(response, linkDto, result);
        }
        return result;
    }

    protected AdditionalHandlerResult processNumber(
        Long responseValue,
        String fieldName,
        LinkDto dto,
        String message,
        AdditionalHandlerResult result
    ) {
        if (responseValue == null) {
            return result;
        }
        JsonNode jsonNode = dto.additionalInfo().findValue(fieldName);
        try {
            Long dtoValue = Objects.requireNonNull(jsonNode).longValue();

            if (!dtoValue.equals(responseValue)) {
                result.setRowUpdateConsumer(addToConsumerUpdateAdditionalInfo(fieldName, responseValue, dto, result));

                if (responseValue > dtoValue) {
                    result.getDescriptions().add(message);
                }
            }
        } catch (NullPointerException e) {
            result.setRowUpdateConsumer(addToConsumerUpdateAdditionalInfo(fieldName, responseValue, dto, result));
        }
        return result;
    }

    protected Consumer<LinkRepository> addToConsumerUpdateAdditionalInfo(
        @NotNull String fieldName,
        @NotNull Object newValue,
        @NotNull LinkDto dto,
        @NotNull AdditionalHandlerResult result
    ) {
        return result.getRowUpdateConsumer()
            .andThen(linkRepository -> linkRepository.updateAdditionalInfo(
                    dto.id(),
                    fieldName,
                    newValue
                )
            );

    }
}
