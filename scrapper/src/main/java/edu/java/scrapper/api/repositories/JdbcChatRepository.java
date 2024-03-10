package edu.java.scrapper.api.repositories;

import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.exceptions.UserAlreadyExistsException;
import edu.java.scrapper.domain.dao.ChatDao;
import java.util.List;
import java.util.Optional;
import edu.java.scrapper.domain.dao.LinkChatAssignmentDao;
import edu.java.scrapper.domain.dao.LinksDao;
import edu.java.scrapper.domain.dto.ChatDto;
import edu.java.scrapper.domain.dto.LinkChatAssignmentDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcChatRepository implements ChatRepository {
    ChatDao chatDao;
    LinksDao linksDao;
    LinkChatAssignmentDao linkChatAssignmentDao;

    public JdbcChatRepository(ChatDao chatDao, LinksDao linksDao, LinkChatAssignmentDao linkChatAssignmentDao) {
        this.chatDao = chatDao;
        this.linksDao = linksDao;
        this.linkChatAssignmentDao = linkChatAssignmentDao;
    }

    @Override
    public Long addChat(Long id) {
        if (findChat(id).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        return chatDao.add(id).id();
    }

    @Override
    @Transactional public Long deleteChat(Long id) {
        Long chatId = chatDao.find(id).orElseThrow(() -> new ResourceNotFoundException("User not found")).id();
        List<LinkChatAssignmentDto> assignmentDtoList = linkChatAssignmentDao.findByChatId(chatId);
        List<Long> userLinksIds = assignmentDtoList.stream()
            .map(LinkChatAssignmentDto::linkId)
            .toList();
        for (Long linkId : userLinksIds) {
            if (linkChatAssignmentDao.findByLinkId(linkId).stream()
                .allMatch((dto) -> dto.chatId().equals(chatId))
            ) {
                linksDao.remove(linkId);
            }
        }
        return chatDao.remove(id).id();
    }

    @Override
    public Optional<Long> findChat(Long id) {
        return chatDao.find(id).map(ChatDto::id);
    }
}
