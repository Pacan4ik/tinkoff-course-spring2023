package edu.java.scrapper.api.services.jdbc;

import edu.java.scrapper.api.exceptions.LinkAlreadyExistsException;
import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.model.LinkResponse;
import edu.java.scrapper.api.services.LinkService;
import edu.java.scrapper.domain.jdbc.dao.ChatRepository;
import edu.java.scrapper.domain.jdbc.dao.LinkRepository;
import edu.java.scrapper.domain.jdbc.dto.LinkDto;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

public class JdbcLinkService implements LinkService {
    public static final String USER_NOT_FOUND = "User not found";
    public static final String LINK_NOT_FOUND = "Link not found";
    public static final String USER_HAS_NOT_YET_SUBSCRIBED = "User has not yet subscribed to this link";
    public static final String USER_ALREADY_SUBSCRIBED = "User is already subscribed";
    private final LinkRepository linkRepository;

    private final ChatRepository chatRepository;

    public JdbcLinkService(
        @Qualifier("jdbcLinkRepository") LinkRepository linkRepository,
        @Qualifier("jdbcChatRepository") ChatRepository chatRepository
    ) {
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    @Transactional
    public List<LinkResponse> getUserLinks(Long id) {
        chatRepository.find(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        List<Long> linkIds = chatRepository.getAllLinks(id);
        if (linkIds.isEmpty()) {
            return Collections.emptyList();
        }
        return linkRepository.findAll(linkIds.toArray(Long[]::new)).stream()
            .map((dto) -> new LinkResponse(dto.id(), dto.url()))
            .toList();
    }

    @Override
    @Transactional
    public LinkResponse addLink(Long id, URI link) {
        chatRepository.find(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        LinkDto linkDto = linkRepository.find(link).orElseGet(() -> linkRepository.add(link));

        if (chatRepository.getAllLinks(id).stream().anyMatch(linkId -> linkId.equals(linkDto.id()))) {
            throw new LinkAlreadyExistsException(USER_ALREADY_SUBSCRIBED);
        }

        chatRepository.addLink(id, linkDto.id());
        return new LinkResponse(linkDto.id(), linkDto.url());
    }

    @Override
    @Transactional
    public LinkResponse removeLink(Long id, URI link) {
        chatRepository.find(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        LinkDto linkDto = linkRepository.find(link).orElseThrow(() -> new ResourceNotFoundException(LINK_NOT_FOUND));
        if (chatRepository.getAllLinks(id).stream().noneMatch(linkId -> linkId.equals(linkDto.id()))) {
            throw new ResourceNotFoundException(USER_HAS_NOT_YET_SUBSCRIBED);
        }

        chatRepository.removeLink(id, linkDto.id());

        if (linkRepository.getChats(linkDto.id()).isEmpty()) {
            linkRepository.remove(linkDto.id());
        }

        return new LinkResponse(linkDto.id(), linkDto.url());
    }
}
