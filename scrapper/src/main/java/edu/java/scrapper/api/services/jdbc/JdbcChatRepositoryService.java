package edu.java.scrapper.api.services.jdbc;

import edu.java.scrapper.api.repositories.ChatRepository;
import edu.java.scrapper.api.services.ChatRepositoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JdbcChatRepositoryService implements ChatRepositoryService {
    private final ChatRepository chatRepository;

    public JdbcChatRepositoryService(@Qualifier("jdbcChatRepository") ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void registerChat(Long id) {
        chatRepository.addChat(id);
    }

    @Override
    public void deleteChat(Long id) {
        chatRepository.deleteChat(id);
    }
}


