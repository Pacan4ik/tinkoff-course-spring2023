package edu.java.scrapper.api.repositories;

import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.domain.dao.ChatDao;
import edu.java.scrapper.domain.dao.LinkChatAssignmentDao;
import edu.java.scrapper.domain.dao.LinksDao;
import edu.java.scrapper.domain.dto.LinkChatAssignmentDto;
import edu.java.scrapper.domain.dto.LinkDto;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcLinkRepository implements LinkRepository {
    //TODO
    private final LinksDao linksDao;
    private final LinkChatAssignmentDao linkChatAssignmentDao;
    private final ChatDao chatDao;
    private static final String USER_NOT_FOUND = "User not found";
    private static final String LINK_NOT_FOUND = "Link not found";

    public JdbcLinkRepository(LinksDao linksDao, LinkChatAssignmentDao linkChatAssignmentDao, ChatDao chatDao) {
        this.linksDao = linksDao;
        this.linkChatAssignmentDao = linkChatAssignmentDao;
        this.chatDao = chatDao;
    }

    @Override
    public List<URI> getLinks(Long chatId) {
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
        chatDao.find(chatId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        LinkDto linkDto = linksDao.find(url).orElseGet(() -> linksDao.add(url));
        linkChatAssignmentDao.add(linkDto.id(), chatId);
        return linkDto.url();
    }

    @Override
    @Transactional public URI delete(Long chatId, URI url) {
        chatDao.find(chatId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Long linkId = linksDao.find(url).orElseThrow(() -> new ResourceNotFoundException(LINK_NOT_FOUND)).id();
        Long assignmentId = linkChatAssignmentDao.findByLinkIdAndChatId(linkId, chatId)
            .orElseThrow(() -> new ResourceNotFoundException(LINK_NOT_FOUND)).id();
        linkChatAssignmentDao.remove(assignmentId);
        if (linkChatAssignmentDao.findByLinkId(linkId).isEmpty()) {
            return linksDao.remove(linkId).url();
        }
        return url;
    }
}
