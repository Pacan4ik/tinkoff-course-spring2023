package edu.java.scrapper.api.services.jpa;

import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.exceptions.UserAlreadyExistsException;
import edu.java.scrapper.api.services.ChatService;
import edu.java.scrapper.domain.jpa.dao.ChatRepository;
import edu.java.scrapper.domain.jpa.dao.LinkRepository;
import edu.java.scrapper.domain.jpa.model.Chat;
import org.springframework.transaction.annotation.Transactional;

public class JpaChatService implements ChatService {
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;

    public JpaChatService(ChatRepository chatRepository, LinkRepository linkRepository) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
    }

    @Override
    @Transactional
    public void registerChat(Long id) {
        if (!chatRepository.existsById(id)) {
            Chat chat = new Chat();
            chat.setId(id);
            chatRepository.saveAndFlush(chat);
        } else {
            throw new UserAlreadyExistsException("User already exists");
        }
    }

    @Override
    @Transactional
    public void deleteChat(Long id) {
        Chat chat = chatRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        linkRepository.deleteBySubscribedChatsContainsOnlyChatId(id);
        chatRepository.delete(chat);

        linkRepository.flush();
        chatRepository.flush();
    }
}
