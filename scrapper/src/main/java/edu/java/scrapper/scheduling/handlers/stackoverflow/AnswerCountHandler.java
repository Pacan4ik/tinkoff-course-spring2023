package edu.java.scrapper.scheduling.handlers.stackoverflow;

import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.domain.jdbc.dto.LinkDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;

public class AnswerCountHandler extends AbstractAdditionalHandler<StackOverFlowResponse> {

    public static final String ANSWER_COUNT = "answer_count";
    public static final String NEW_ANSWER_MESSAGE = "Появился новый ответ";

    @Override
    public AdditionalHandlerResult handle(
        StackOverFlowResponse stackOverFlowResponse,
        LinkDto linkDto,
        AdditionalHandlerResult result
    ) {
        Long answerCountResponse = stackOverFlowResponse.items().getFirst().answerCount();
        processNumber(answerCountResponse, ANSWER_COUNT, linkDto, NEW_ANSWER_MESSAGE, result);
        return handleNext(stackOverFlowResponse, linkDto, result);
    }
}
