package edu.java.scrapper.api.repositories;

import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.exceptions.UserAlreadyExistsException;
import edu.java.scrapper.domain.dao.ChatsDao;
import edu.java.scrapper.domain.dao.LinkChatAssignmentDao;
import edu.java.scrapper.domain.dao.LinksDao;
import edu.java.scrapper.domain.dto.ChatDto;
import edu.java.scrapper.domain.dto.LinkChatAssignmentDto;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcChatRepository implements ChatRepository {
    ChatsDao chatsDao;
    LinksDao linksDao;
    LinkChatAssignmentDao linkChatAssignmentDao;

    public JdbcChatRepository(
        @Qualifier("jdbcChatsDao") ChatsDao chatsDao,
        @Qualifier("jdbcLinksDao") LinksDao linksDao,
        @Qualifier("jdbcLinkChatAssignmentDao") LinkChatAssignmentDao linkChatAssignmentDao
    ) {
        this.chatsDao = chatsDao;
        this.linksDao = linksDao;
        this.linkChatAssignmentDao = linkChatAssignmentDao;
    }

    @Override
    public Long addChat(Long id) {
        if (findChat(id).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        return chatsDao.add(id).id();
    }

    @Override
    @Transactional public Long deleteChat(Long id) {
        Long chatId = chatsDao.find(id).orElseThrow(() -> new ResourceNotFoundException("User not found")).id();
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
        return chatsDao.remove(id).id();
    }

    @Override
    public Optional<Long> findChat(Long id) {
        return chatsDao.find(id).map(ChatDto::id);
    }
}
