package edu.java.scrapper.api.exceptions.handlers;

import edu.java.scrapper.api.exceptions.BadRequestException;
import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.exceptions.ScrapperException;
import edu.java.scrapper.api.exceptions.UserAlreadyExistsException;
import edu.java.scrapper.api.model.ApiErrorResponse;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ScrapperException.class)
    public ResponseEntity<ApiErrorResponse> handleException(ScrapperException e) {
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
        return new ResponseEntity<>(new ApiErrorResponse(
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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new ApiErrorResponse(
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            Integer.toString(HttpStatus.NOT_FOUND.value()),
            e.getClass().getName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList())
        ),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleException(UserAlreadyExistsException e) {
        return new ResponseEntity<>(new ApiErrorResponse(
            HttpStatus.CONFLICT.getReasonPhrase(),
            Integer.toString(HttpStatus.CONFLICT.value()),
            e.getClass().getName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList())
        ),
            HttpStatus.CONFLICT
        );
    }
}
