package edu.java.scrapper.api.repositories;

import java.util.Optional;

public interface ChatRepository {
    Long addChat(Long id);

    Long deleteChat(Long id);

    Optional<Long> findChat(Long id);
}
