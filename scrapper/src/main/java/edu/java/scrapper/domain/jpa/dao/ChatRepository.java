package edu.java.scrapper.domain.jpa.dao;

import edu.java.scrapper.domain.jpa.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
