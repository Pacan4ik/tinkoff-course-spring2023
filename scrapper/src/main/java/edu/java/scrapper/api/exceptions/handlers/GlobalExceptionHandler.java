package edu.java.scrapper.api.exceptions.handlers;

import edu.java.scrapper.api.exceptions.BadRequestException;
import edu.java.scrapper.api.exceptions.LinkAlreadyExistsException;
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
        return generateResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleException(BadRequestException e) {
        return generateResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(ResourceNotFoundException e) {
        return generateResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleException(UserAlreadyExistsException e) {
        return generateResponseEntity(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LinkAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleException(LinkAlreadyExistsException e) {
        return generateResponseEntity(e, HttpStatus.CONFLICT);
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
