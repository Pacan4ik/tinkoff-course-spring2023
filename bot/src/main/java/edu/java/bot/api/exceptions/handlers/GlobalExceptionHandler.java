package edu.java.bot.api.exceptions.handlers;

import edu.java.bot.api.exceptions.BadRequestException;
import edu.java.bot.api.exceptions.BotException;
import edu.java.bot.api.model.ApiErrorResponse;
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
        return new ResponseEntity<>(
            new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e.getClass().getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.toList())
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleException(BadRequestException e) {
        return new ResponseEntity<>(
            new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                Integer.toString(HttpStatus.BAD_REQUEST.value()),
                e.getClass().getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.toList())
            ),
            HttpStatus.BAD_REQUEST
        );
    }
}
