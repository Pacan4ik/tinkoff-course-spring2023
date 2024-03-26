/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.domain.jooq.tables.records;


import edu.java.scrapper.domain.jooq.tables.LinkChatAssignment;

import java.beans.ConstructorProperties;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class LinkChatAssignmentRecord extends UpdatableRecordImpl<LinkChatAssignmentRecord> implements Record2<Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>LINK_CHAT_ASSIGNMENT.LINK_ID</code>.
     */
    public void setLinkId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>LINK_CHAT_ASSIGNMENT.LINK_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getLinkId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>LINK_CHAT_ASSIGNMENT.CHAT_ID</code>.
     */
    public void setChatId(@NotNull Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>LINK_CHAT_ASSIGNMENT.CHAT_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getChatId() {
        return (Long) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record2<Long, Long> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row2<Long, Long> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row2<Long, Long> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return LinkChatAssignment.LINK_CHAT_ASSIGNMENT.LINK_ID;
    }

    @Override
    @NotNull
    public Field<Long> field2() {
        return LinkChatAssignment.LINK_CHAT_ASSIGNMENT.CHAT_ID;
    }

    @Override
    @NotNull
    public Long component1() {
        return getLinkId();
    }

    @Override
    @NotNull
    public Long component2() {
        return getChatId();
    }

    @Override
    @NotNull
    public Long value1() {
        return getLinkId();
    }

    @Override
    @NotNull
    public Long value2() {
        return getChatId();
    }

    @Override
    @NotNull
    public LinkChatAssignmentRecord value1(@NotNull Long value) {
        setLinkId(value);
        return this;
    }

    @Override
    @NotNull
    public LinkChatAssignmentRecord value2(@NotNull Long value) {
        setChatId(value);
        return this;
    }

    @Override
    @NotNull
    public LinkChatAssignmentRecord values(@NotNull Long value1, @NotNull Long value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LinkChatAssignmentRecord
     */
    public LinkChatAssignmentRecord() {
        super(LinkChatAssignment.LINK_CHAT_ASSIGNMENT);
    }

    /**
     * Create a detached, initialised LinkChatAssignmentRecord
     */
    @ConstructorProperties({ "linkId", "chatId" })
    public LinkChatAssignmentRecord(@NotNull Long linkId, @NotNull Long chatId) {
        super(LinkChatAssignment.LINK_CHAT_ASSIGNMENT);

        setLinkId(linkId);
        setChatId(chatId);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised LinkChatAssignmentRecord
     */
    public LinkChatAssignmentRecord(edu.java.scrapper.domain.jooq.tables.pojos.LinkChatAssignment value) {
        super(LinkChatAssignment.LINK_CHAT_ASSIGNMENT);

        if (value != null) {
            setLinkId(value.getLinkId());
            setChatId(value.getChatId());
            resetChangedOnNotNull();
        }
    }
}
