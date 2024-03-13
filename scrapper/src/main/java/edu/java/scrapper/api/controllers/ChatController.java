package edu.java.scrapper.api.controllers;

import edu.java.scrapper.api.model.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public interface ChatController {
    @PostMapping(value = "/tg-chat/{id}", produces = "application/json")
    @Operation(summary = "Зарегистрировать чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Чат зарегистрирован"),

        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<Void> registerChat(@PathVariable Long id);

    @DeleteMapping(value = "/tg-chat/{id}", produces = "application/json")
    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Чат успешно удалён"),

        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class))),

        @ApiResponse(responseCode = "404",
                     description = "Чат не существует",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<Void> deleteChat(@PathVariable Long id);
}
