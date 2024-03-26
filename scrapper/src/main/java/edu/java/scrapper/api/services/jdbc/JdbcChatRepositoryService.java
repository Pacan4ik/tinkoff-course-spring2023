package edu.java.scrapper.api.services.jdbc;

import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.exceptions.UserAlreadyExistsException;
import edu.java.scrapper.api.services.ChatRepositoryService;
import edu.java.scrapper.domain.dao.ChatRepository;
import edu.java.scrapper.domain.dao.LinkRepository;
import edu.java.scrapper.domain.dto.LinkDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("jdbcChatService")
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


