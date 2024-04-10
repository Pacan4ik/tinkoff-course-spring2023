package edu.java.scrapper.domain.adapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.domain.jooq.tables.Chat;
import edu.java.scrapper.domain.jooq.tables.Link;
import edu.java.scrapper.domain.jooq.tables.LinkChatAssignment;
import edu.java.scrapper.domain.jooq.tables.records.LinkRecord;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.jooq.Record1;
import org.jooq.Result;
import org.springframework.transaction.annotation.Transactional;

public class JooqLinkInfoAdapter implements LinkInfoAdapter {
    private final DSLContext dslContext;
    private final ObjectMapper objectMapper;
    private final Chat chat = Chat.CHAT;
    private final Link link = Link.LINK;
    private final LinkChatAssignment linkChatAssignment = LinkChatAssignment.LINK_CHAT_ASSIGNMENT;

    public JooqLinkInfoAdapter(DSLContext dslContext, ObjectMapper objectMapper) {
        this.dslContext = dslContext;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<LinkInfoDto> findAllCheckedAtBefore(OffsetDateTime offsetDateTime) {
        List<LinkRecord> recordList =
            dslContext.select().from(link).where(link.CHECKED_AT.lessThan(offsetDateTime)).fetchInto(LinkRecord.class);
        return recordList.stream()
            .map(linkRecord -> {
                    try {
                        return new LinkInfoDto(
                            linkRecord.getId(),
                            URI.create(linkRecord.getUrl()),
                            linkRecord.getCheckedAt(),
                            objectMapper.readValue(
                                Objects.requireNonNull(linkRecord.getAdditionalInfo()).data(),
                                LinkInfoDto.AdditionalInfo.class
                            )
                        );
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            ).toList();
    }

    @Override
    public void updateCheckedAt(Long id, OffsetDateTime offsetDateTime) {
        dslContext.update(link).set(link.CHECKED_AT, offsetDateTime)
            .where(link.ID.eq(id))
            .execute();
    }

    @Override
    public Collection<Long> getSubscribedChats(Long id) {
        Result<Record1<Long>> result = dslContext.select(linkChatAssignment.CHAT_ID).from(linkChatAssignment)
            .where(linkChatAssignment.LINK_ID.eq(id))
            .fetch();
        return result.stream().map(Record1::component1).toList();
    }

    @Override
    @Transactional
    public void updateAdditionalInfo(Long id, LinkInfoDto.AdditionalInfo additionalInfo) {
        LinkRecord linkRecord = dslContext.selectFrom(link).where(link.ID.eq(id)).fetchOne();
        Objects.requireNonNull(linkRecord);
        JsonNode oldNode = objectMapper.createObjectNode();
        if (linkRecord.getAdditionalInfo() != null) {
            String data = linkRecord.getAdditionalInfo().data();
            try {
                oldNode = objectMapper.readValue(data, JsonNode.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        JsonNode newNode = objectMapper.convertValue(additionalInfo, JsonNode.class);
        linkRecord.setAdditionalInfo(
            JSON.json(mergeJsonNodes(oldNode, newNode).toString())
        );
        linkRecord.update();
    }

}
