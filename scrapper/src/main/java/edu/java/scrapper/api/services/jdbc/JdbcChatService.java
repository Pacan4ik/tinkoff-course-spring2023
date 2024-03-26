package edu.java.scrapper.api.services.jdbc;

import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.exceptions.UserAlreadyExistsException;
import edu.java.scrapper.api.services.ChatService;
import edu.java.scrapper.domain.jdbc.dao.ChatRepository;
import edu.java.scrapper.domain.jdbc.dao.LinkRepository;
import edu.java.scrapper.domain.jdbc.dto.LinkDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

public class JdbcChatService implements ChatService {
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;

    public JdbcChatService(
        @Qualifier("jdbcChatRepository") ChatRepository chatRepository,
        @Qualifier("jdbcLinkRepository") LinkRepository linkRepository
    ) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
    }

    @Override
    public void registerChat(Long id) {
        try {
            chatRepository.add(id);
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistsException("User already exists", e);
        }
    }

    @Override
    @Transactional
    public void deleteChat(Long id) {
        Long[] userLinks = linkRepository.getAllLinks(id).stream()
            .map(LinkDto::id)
            .toArray(Long[]::new);
        try {
            chatRepository.remove(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("User not found", e);
        }
        if (userLinks.length != 0) {
            linkRepository.removeUnassigned(userLinks);
        }
    }
}


