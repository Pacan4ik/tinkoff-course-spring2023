package edu.java.scrapper.api.services.jdbc;

import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.exceptions.UserAlreadyExistsException;
import edu.java.scrapper.api.services.ChatRepositoryService;
import edu.java.scrapper.domain.dao.ChatRepository;
import edu.java.scrapper.domain.dao.LinkRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JdbcChatRepositoryService implements ChatRepositoryService {
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;

    public JdbcChatRepositoryService(
        @Qualifier("jdbcChatRepository") ChatRepository chatRepository,
        @Qualifier("jdbcLinkRepository") LinkRepository linkRepository
    ) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
    }

    @Override
    @Transactional
    public void registerChat(Long id) {
        if (chatRepository.find(id).isEmpty()) {
            chatRepository.add(id);
        } else {
            throw new UserAlreadyExistsException("User already exists");
        }
    }

    @Override
    @Transactional
    public void deleteChat(Long id) {
        chatRepository.find(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Long[] linksToDelete = chatRepository.getAllLinks(id).stream()
            .filter(linkId -> linkRepository.getChats(linkId).stream()
                .allMatch(chatId -> chatId.equals(id)))
            .toArray(Long[]::new);
        if (linksToDelete.length != 0) {
            linkRepository.remove(linksToDelete);
        }
        chatRepository.remove(id);
    }
}


