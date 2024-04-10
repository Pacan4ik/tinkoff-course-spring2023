package edu.java.scrapper.domain.jpa.dao;

import edu.java.scrapper.domain.jpa.model.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUrl(String url);

    @Modifying
    @Query(value = "delete from link where link.id in "
                   + "(select lca.link_id from link_chat_assignment lca "
                   + "where lca.chat_id = :chatId and lca.link_id not in ("
                   + "select lca2.link_id from link_chat_assignment lca2 "
                   + "where lca2.chat_id != :chatId))",
           nativeQuery = true)
    void deleteBySubscribedChatsContainsOnlyChatId(@Param("chatId") Long chatId);

    List<Link> findByCheckedAtLessThan(OffsetDateTime checkedAt);

    @Transactional @Modifying @Query("update Link l set l.checkedAt = ?1 where l.id = ?2")
    void updateCheckedAtById(OffsetDateTime checkedAt, Long id);

    @Transactional @Modifying @Query("update Link l set l.additionalInfo = ?1 where l.id = ?2")
    void updateAdditionalInfoById(Map<String, Object> additionalInfo, Long id);

    @Modifying @Query(value = "select chat_id from link_chat_assignment where link_id = :id", nativeQuery = true)
    Set<Long> getChatIdsById(Long id);
}
