package edu.java.scrapper.domain.dao;

import edu.java.scrapper.domain.dto.ChatDto;
import java.util.List;
import java.util.Optional;

public interface ChatDao {
    ChatDto add(Long id);

    ChatDto remove(Long id);

    List<ChatDto> findAll();

    List<ChatDto> findAll(Long... ids);

    Optional<ChatDto> find(Long id);

}
