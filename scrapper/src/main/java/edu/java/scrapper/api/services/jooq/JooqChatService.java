package edu.java.scrapper.api.services.jooq;

import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.exceptions.UserAlreadyExistsException;
import edu.java.scrapper.api.services.ChatService;
import edu.java.scrapper.domain.jooq.tables.Chat;
import edu.java.scrapper.domain.jooq.tables.Link;
import edu.java.scrapper.domain.jooq.tables.LinkChatAssignment;
import org.jooq.DSLContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import static org.jooq.impl.DSL.selectOne;

public class JooqChatService implements ChatService {

    private final DSLContext dslContext;
    private final Chat chat = Chat.CHAT;
    private final Link link = Link.LINK;

    private final LinkChatAssignment linkChatAssignment = LinkChatAssignment.LINK_CHAT_ASSIGNMENT;

    public JooqChatService(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public void registerChat(Long id) {
        try {
            dslContext.insertInto(chat)
                .set(chat.ID, id)
                .execute();
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistsException("User already exists");
        }
    }

    @Override
    @Transactional
    public void deleteChat(Long id) {
        dslContext.deleteFrom(link)
            .whereExists(
                selectOne()
                    .from(linkChatAssignment)
                    .where(linkChatAssignment.LINK_ID.eq(link.ID))
                    .andNotExists(
                        selectOne()
                            .from(linkChatAssignment)
                            .where(linkChatAssignment.LINK_ID.eq(link.ID))
                            .and(linkChatAssignment.CHAT_ID.ne(id))
                    )
            )
            .execute();

        int deleted = dslContext.deleteFrom(chat)
            .where(chat.ID.eq(id))
            .execute();

        if (deleted == 0) {
            throw new ResourceNotFoundException("User not found");
        }
    }
}
