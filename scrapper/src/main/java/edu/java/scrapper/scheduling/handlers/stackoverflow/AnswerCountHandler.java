package edu.java.scrapper.scheduling.handlers.stackoverflow;

import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;
import java.util.Objects;

public class AnswerCountHandler extends AbstractAdditionalHandler<StackOverFlowResponse> {
    public static final String NEW_ANSWER_MESSAGE = "Появился новый ответ";

    @Override
    public AdditionalHandlerResult handle(
        StackOverFlowResponse stackOverFlowResponse,
        LinkInfoDto.AdditionalInfo additionalInfo,
        AdditionalHandlerResult result
    ) {
        return handleNext(
            stackOverFlowResponse,
            additionalInfo,
            processAnswers(stackOverFlowResponse, additionalInfo, result)
        );
    }

    private AdditionalHandlerResult processAnswers(
        StackOverFlowResponse response,
        LinkInfoDto.AdditionalInfo additionalInfo,
        AdditionalHandlerResult result
    ) {
        Long responseAnswers;
        try {
            responseAnswers = Objects.requireNonNull(response.items().getFirst().answerCount());
        } catch (NullPointerException e) {
            return result;
        }

        Long dtoInfoAnswers = additionalInfo.getAnswerCount();

        if (!responseAnswers.equals(dtoInfoAnswers)) {
            result.setAdditionalInfoConsumer(addToConsumer(
                result,
                info -> info.setAnswerCount(responseAnswers)
            ));

            if (dtoInfoAnswers != null && responseAnswers > dtoInfoAnswers) {
                result.getDescriptions().add(NEW_ANSWER_MESSAGE);
            }
        }
        return result;
    }
}
