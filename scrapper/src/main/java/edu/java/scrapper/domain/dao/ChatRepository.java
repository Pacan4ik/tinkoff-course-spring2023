package edu.java.scrapper.domain.dao;

import edu.java.scrapper.domain.dto.ChatDto;
import edu.java.scrapper.domain.dto.LinkDto;
import java.util.List;
import java.util.Optional;

public interface ChatsRepository {
    ChatDto add(Long id);

    ChatDto remove(Long id);

    List<ChatDto> findAll();

    List<ChatDto> findAll(Long... ids);

    Optional<ChatDto> find(Long id);

    LinkDto addLink(Long chatId, Long linkId);

    LinkDto removeLink(Long chatId, Long linkId);

    List<LinkDto> getAllLinks(Long id);

}
