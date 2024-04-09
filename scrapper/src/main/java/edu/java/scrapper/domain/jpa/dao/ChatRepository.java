package edu.java.scrapper.domain.jpa.dao;

import edu.java.scrapper.domain.jpa.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Modifying
    @Query(value = "delete from link_chat_assignment lca "
                   + "where chat_id = :chatId "
                   + "and link_id in "
                   + "(select id from link where url = :linkUrl); "
                   + "delete from link l "
                   + "where url = :linkUrl "
                   + "and not exists(select 1 from link_chat_assignment where link_id = l.id)",
           nativeQuery = true)
    void removeLinkByUrl(Long chatId, String linkUrl);
}
