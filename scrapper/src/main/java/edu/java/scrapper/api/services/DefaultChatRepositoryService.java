package edu.java.scrapper.api.services;

import edu.java.scrapper.ChatRepository;
import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.exceptions.UserAlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
public class DefaultChatRepositoryService implements ChatRepositoryService {
    //TODO
    private final ChatRepository chatRepository;

    public DefaultChatRepositoryService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void registerChat(Long id) {
        if (!chatRepository.addUser(id)) {
            throw new UserAlreadyExistsException();
        }
    }

    @Override
    public void deleteChat(Long id) {
        if (!chatRepository.deleteUser(id)) {
            throw new ResourceNotFoundException("Chat not found");
        }
    }

}
