package edu.java.bot.api.exceptions.handlers;

import edu.java.bot.api.exceptions.BadRequestException;
import edu.java.bot.api.exceptions.BotException;
import edu.java.bot.api.model.ApiErrorResponse;
import edu.tinkoff.ratelimiting.OutOfTokensException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BotException.class)
    public ResponseEntity<ApiErrorResponse> handleException(BotException e) {
        return generateResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleException(BadRequestException e) {
        return generateResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OutOfTokensException.class)
    public ResponseEntity<ApiErrorResponse> handleException(OutOfTokensException e) {
        return generateResponseEntity(e, HttpStatus.TOO_MANY_REQUESTS);
    }

    private ResponseEntity<ApiErrorResponse> generateResponseEntity(Exception e, HttpStatus httpStatus) {
        return new ResponseEntity<>(
            new ApiErrorResponse(
                httpStatus.getReasonPhrase(),
                Integer.toString(httpStatus.value()),
                e.getClass().getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.toList())
            ),
            httpStatus
        );
    }
}
