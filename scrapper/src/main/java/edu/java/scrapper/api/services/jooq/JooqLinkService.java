package edu.java.scrapper.api.services.jooq;

import edu.java.scrapper.api.exceptions.LinkAlreadyExistsException;
import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.model.LinkResponse;
import edu.java.scrapper.api.services.LinkRepositoryService;
import edu.java.scrapper.domain.jooq.tables.Chat;
import edu.java.scrapper.domain.jooq.tables.Link;
import edu.java.scrapper.domain.jooq.tables.LinkChatAssignment;
import edu.java.scrapper.domain.jooq.tables.records.ChatRecord;
import edu.java.scrapper.domain.jooq.tables.records.LinkRecord;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import org.jooq.DSLContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("jooqLinkService")
public class JooqLinkService implements LinkRepositoryService {

    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_ALREADY_SUBSCRIBED = "User already subscribed";
    public static final String LINK_NOT_FOUND = "Link not found";
    public static final String USER_HAS_NOT_SUBSCRIBED = "User has not yet subscribed to this link";
    private final DSLContext dslContext;
    private final Chat chat = Chat.CHAT;
    private final Link link = Link.LINK;
    private final LinkChatAssignment linkChatAssignment = LinkChatAssignment.LINK_CHAT_ASSIGNMENT;

    public JooqLinkService(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    @Transactional
    public List<LinkResponse> getUserLinks(Long id) {
        ChatRecord chatRecord = dslContext.selectOne()
            .from(chat)
            .where(chat.ID.eq(id))
            .fetchOneInto(ChatRecord.class);

        if (chatRecord == null) {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }

        List<LinkRecord> recordList = dslContext.select(link.fields())
            .from(link)
            .join(linkChatAssignment).on(link.ID.eq(linkChatAssignment.LINK_ID))
            .where(linkChatAssignment.CHAT_ID.eq(id))
            .fetchInto(LinkRecord.class);

        return recordList.stream()
            .map(r -> new LinkResponse(r.getId(), URI.create(r.get(link.URL))))
            .toList();
    }

    @Override
    @Transactional
    public LinkResponse addLink(Long id, URI link) {
        LinkRecord linkRecord = Objects.requireNonNullElseGet(
            dslContext.selectFrom(this.link)
                .where(this.link.URL.eq(link.toString()))
                .fetchOne(),
            () -> dslContext.insertInto(this.link)
                .set(this.link.URL, link.toString())
                .returning()
                .fetchOne()
        );
        try {
            dslContext.insertInto(linkChatAssignment)
                .set(linkChatAssignment.LINK_ID, linkRecord.getId())
                .set(linkChatAssignment.CHAT_ID, id)
                .execute();
        } catch (DuplicateKeyException e) {
            throw new LinkAlreadyExistsException(USER_ALREADY_SUBSCRIBED);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceNotFoundException(USER_NOT_FOUND, e);
        }

        return new LinkResponse(linkRecord.getId(), URI.create(linkRecord.get(this.link.URL)));
    }

    @Override
    @Transactional
    public LinkResponse removeLink(Long id, URI link) {
        LinkRecord linkRecord = dslContext.selectFrom(this.link)
            .where(this.link.URL.eq(link.toString()))
            .fetchOne();
        if (linkRecord == null) {
            throw new ResourceNotFoundException(LINK_NOT_FOUND);
        }

        int deleted = dslContext.deleteFrom(linkChatAssignment)
            .where(linkChatAssignment.CHAT_ID.eq(id))
            .and(linkChatAssignment.LINK_ID.eq(linkRecord.getId()))
            .execute();
        if (deleted == 0) {
            throw new ResourceNotFoundException(USER_HAS_NOT_SUBSCRIBED);
        }

        dslContext.deleteFrom(this.link)
            .where(this.link.ID.eq(linkRecord.getId()))
            .andNotExists(
                dslContext.selectOne()
                    .from(linkChatAssignment)
                    .where(linkChatAssignment.LINK_ID.eq(linkRecord.getId()))
            )
            .execute();
        return new LinkResponse(linkRecord.getId(), URI.create(linkRecord.getUrl()));
    }
}
