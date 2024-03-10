package edu.java.scrapper.domain.dao;

import edu.java.scrapper.domain.dto.LinkChatAssignmentDto;
import java.util.List;
import java.util.Optional;

public interface LinkChatAssignmentDao {
    LinkChatAssignmentDto add(Long linkId, Long chatId);

    LinkChatAssignmentDto remove(Long id);

    List<LinkChatAssignmentDto> remove(Long... ids);

    List<LinkChatAssignmentDto> findAll();

    Optional<LinkChatAssignmentDto> findById(Long assignmentId);

    List<LinkChatAssignmentDto> findByLinkId(Long linkId);

    List<LinkChatAssignmentDto> findByChatId(Long chatId);

    Optional<LinkChatAssignmentDto> findByLinkIdAndChatId(Long linkId, Long chatId);
}
