package edu.java.scrapper.api.controllers;

import edu.java.scrapper.api.services.ChatRepositoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultChatController implements ChatController {
    private final ChatRepositoryService chatRepositoryService;

    DefaultChatController(@Qualifier("jooqChatService") ChatRepositoryService chatRepositoryService) {
        this.chatRepositoryService = chatRepositoryService;
    }

    @Override
    public ResponseEntity<Void> registerChat(Long id) {
        chatRepositoryService.registerChat(id);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<Void> deleteChat(Long id) {
        chatRepositoryService.deleteChat(id);
        return ResponseEntity.ok(null);
    }
}
