package edu.java.bot.api.controllers;

import edu.java.bot.api.model.LinkUpdateRequest;
import edu.java.bot.api.services.UpdateHandlerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class DefaultUpdateController implements UpdateController {
    private final UpdateHandlerService updateHandlerService;

    public DefaultUpdateController(UpdateHandlerService updateHandlerService) {
        this.updateHandlerService = updateHandlerService;
    }

    @Override
    public ResponseEntity<Void> sendUpdates(LinkUpdateRequest linkUpdateRequest) {
        updateHandlerService.handleUpdate(
            linkUpdateRequest.url(),
            linkUpdateRequest.description(),
            linkUpdateRequest.tgChatIds()
        );
        return ResponseEntity.ok(null);
    }
}
