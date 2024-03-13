package edu.java.scrapper.api.repositories;

import edu.java.scrapper.api.exceptions.LinkAlreadyExistsException;
import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.domain.dao.ChatsDao;
import edu.java.scrapper.domain.dao.LinkChatAssignmentDao;
import edu.java.scrapper.domain.dao.LinksDao;
import edu.java.scrapper.domain.dto.LinkChatAssignmentDto;
import edu.java.scrapper.domain.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcLinkRepository implements LinkRepository {
    private final LinksDao linksDao;
    private final LinkChatAssignmentDao linkChatAssignmentDao;
    private final ChatsDao chatsDao;
    private static final String USER_NOT_FOUND = "User not found";
    private static final String LINK_NOT_FOUND = "Link not found";

    private static final String USER_ALREADY_SUBSCRIBED = "User already subscribed";

    public JdbcLinkRepository(
        @Qualifier("jdbcLinksDao") LinksDao linksDao,
        @Qualifier("jdbcChatsDao") ChatsDao chatsDao,
        @Qualifier("jdbcLinkChatAssignmentDao") LinkChatAssignmentDao linkChatAssignmentDao
    ) {
        this.linksDao = linksDao;
        this.linkChatAssignmentDao = linkChatAssignmentDao;
        this.chatsDao = chatsDao;
    }

    @Override
    public Optional<Long> findLink(URI url) {
        return linksDao.find(url).map(LinkDto::id);
    }

    @Override
    public List<URI> getLinks(Long chatId) {
        chatsDao.find(chatId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Long[] linksIds = linkChatAssignmentDao.findByChatId(chatId).stream()
            .map(LinkChatAssignmentDto::linkId)
            .toArray(Long[]::new);
        if (linksIds.length == 0) {
            return Collections.emptyList();
        }
        return linksDao.findAll(linksIds).stream()
            .map(LinkDto::url)
            .toList();
    }

    @Override
    @Transactional public URI addLink(Long chatId, URI url) {
        chatsDao.find(chatId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        LinkDto linkDto = linksDao.find(url).orElseGet(() -> linksDao.add(url));
        if (linkChatAssignmentDao.findByLinkIdAndChatId(linkDto.id(), chatId).isPresent()) {
            throw new LinkAlreadyExistsException(USER_ALREADY_SUBSCRIBED);
        } else {
            linkChatAssignmentDao.add(linkDto.id(), chatId);
        }
        return linkDto.url();
    }

    @Override
    @Transactional public URI delete(Long chatId, URI url) {
        chatsDao.find(chatId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Long linkId = linksDao.find(url).orElseThrow(() -> new ResourceNotFoundException(LINK_NOT_FOUND)).id();
        Long assignmentId = linkChatAssignmentDao.findByLinkIdAndChatId(linkId, chatId)
            .orElseThrow(() -> new ResourceNotFoundException(LINK_NOT_FOUND)).id();
        linkChatAssignmentDao.remove(assignmentId);
        if (linkChatAssignmentDao.findByLinkId(linkId).isEmpty()) {
            return linksDao.remove(linkId).url();
        }
        return url;
    }

    @Override
    public List<URI> getLinksCheckedBefore(OffsetDateTime offsetDateTime) {
        return linksDao.findAllWhereCheckedAtBefore(offsetDateTime).stream()
            .map(LinkDto::url)
            .toList();
    }

    @Override
    public OffsetDateTime updateCheckedAt(URI url, OffsetDateTime newOffsetDateTime) {
        LinkDto linkDto = linksDao.find(url).orElseThrow(() -> new ResourceNotFoundException(LINK_NOT_FOUND));
        return linksDao.updateCheckedAt(linkDto.id(), newOffsetDateTime).updatedAt();
    }

    @Override
    public OffsetDateTime updateUpdatedAt(URI url, OffsetDateTime newOffsetDateTime) {
        LinkDto linkDto = linksDao.find(url).orElseThrow(() -> new ResourceNotFoundException(LINK_NOT_FOUND));
        return linksDao.updateUpdatedAt(linkDto.id(), newOffsetDateTime).updatedAt();
    }

    @Override
    public OffsetDateTime getLastUpdate(URI url) {
        LinkDto linkDto = linksDao.find(url).orElseThrow(() -> new ResourceNotFoundException(LINK_NOT_FOUND));
        return linkDto.updatedAt();
    }

    @Override
    public List<Long> getSubscribedChats(URI url) {
        LinkDto linkDto = linksDao.find(url).orElseThrow(() -> new ResourceNotFoundException(LINK_NOT_FOUND));
        return linkChatAssignmentDao.findByLinkId(linkDto.id()).stream()
            .map(LinkChatAssignmentDto::chatId)
            .toList();
    }

}
