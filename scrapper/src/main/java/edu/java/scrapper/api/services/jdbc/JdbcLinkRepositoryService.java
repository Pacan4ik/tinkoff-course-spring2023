package edu.java.scrapper.api.services.jdbc;

import edu.java.scrapper.api.exceptions.LinkAlreadyExistsException;
import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.services.LinkRepositoryService;
import edu.java.scrapper.domain.dao.ChatRepository;
import edu.java.scrapper.domain.dao.LinkRepository;
import edu.java.scrapper.domain.dto.LinkDto;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JdbcLinkRepositoryService implements LinkRepositoryService {
    public static final String USER_NOT_FOUND = "User not found";
    public static final String LINK_NOT_FOUND = "Link not found";
    public static final String USER_HAS_NOT_YET_SUBSCRIBED = "User has not yet subscribed to this link";
    public static final String USER_ALREADY_SUBSCRIBED = "User is already subscribed";
    private final LinkRepository linkRepository;

    private final ChatRepository chatRepository;

    public JdbcLinkRepositoryService(
        @Qualifier("jdbcLinkRepository") LinkRepository linkRepository,
        @Qualifier("jdbcChatRepository") ChatRepository chatRepository
    ) {
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    @Transactional
    public Collection<URI> getUserLinks(Long id) {
        chatRepository.find(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        List<Long> linkIds = chatRepository.getAllLinks(id);
        if (linkIds.isEmpty()) {
            return Collections.emptyList();
        }
        return linkRepository.findAll(linkIds.toArray(Long[]::new)).stream()
            .map(LinkDto::url)
            .toList();
    }

    @Override
    @Transactional
    public URI addLink(Long id, URI link) {
        chatRepository.find(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        LinkDto linkDto = linkRepository.find(link).orElseGet(() -> linkRepository.add(link));

        if (chatRepository.getAllLinks(id).stream().anyMatch(linkId -> linkId.equals(linkDto.id()))) {
            throw new LinkAlreadyExistsException(USER_ALREADY_SUBSCRIBED);
        }

        chatRepository.addLink(id, linkDto.id());
        return linkDto.url();
    }

    @Override
    @Transactional
    public URI removeLink(Long id, URI link) {
        chatRepository.find(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        LinkDto linkDto = linkRepository.find(link).orElseThrow(() -> new ResourceNotFoundException(LINK_NOT_FOUND));
        if (chatRepository.getAllLinks(id).stream().noneMatch(linkId -> linkId.equals(linkDto.id()))) {
            throw new ResourceNotFoundException(USER_HAS_NOT_YET_SUBSCRIBED);
        }

        chatRepository.removeLink(id, linkDto.id());

        if (linkRepository.getChats(linkDto.id()).isEmpty()) {
            linkRepository.remove(linkDto.id());
        }

        return linkDto.url();
    }
}
