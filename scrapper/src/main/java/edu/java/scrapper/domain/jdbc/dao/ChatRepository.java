package edu.java.scrapper.domain.jdbc.dao;

import edu.java.scrapper.domain.jdbc.dto.ChatDto;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    ChatDto add(Long id);

    ChatDto remove(Long id);

    List<ChatDto> findAll();

    List<ChatDto> findAll(Long... ids);

    Optional<ChatDto> find(Long id);

    Long addLink(Long chatId, Long linkId);

    Long removeLink(Long chatId, Long linkId);

    List<Long> getAllLinks(Long id);

}
