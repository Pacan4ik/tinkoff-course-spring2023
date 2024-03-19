package edu.java.bot.api.controllers;

import edu.java.bot.api.model.ApiErrorResponse;
import edu.java.bot.api.model.LinkUpdateRequest;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@OpenAPIDefinition(info = @Info(title = "Bot API",
                                version = "0.0.1",
                                description = "API for Bot",
                                contact = @Contact(name = "Andrey Potekhin")))
public interface UpdateController {
    @PostMapping(value = "/updates", produces = "application/json")
    @Operation(summary = "Отправить обновление")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200",
                         description = "Обновление обработано",
                         content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "400",
                         description = "Некорректные параметры запроса",
                         content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ApiErrorResponse.class)))
        }
    )
    ResponseEntity<Void> sendUpdates(@RequestBody LinkUpdateRequest linkUpdateRequest);
}
