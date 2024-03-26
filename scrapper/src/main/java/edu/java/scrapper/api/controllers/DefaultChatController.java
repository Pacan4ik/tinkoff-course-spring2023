package edu.java.scrapper.api.controllers;

import edu.java.scrapper.api.services.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultChatController implements ChatController {
    private final ChatService chatService;

    DefaultChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public ResponseEntity<Void> registerChat(Long id) {
        chatService.registerChat(id);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<Void> deleteChat(Long id) {
        chatService.deleteChat(id);
        return ResponseEntity.ok(null);
    }
}
