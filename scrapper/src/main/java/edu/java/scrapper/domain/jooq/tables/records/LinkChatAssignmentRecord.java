/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.domain.jooq.tables.records;


import edu.java.scrapper.domain.jooq.tables.LinkChatAssignment;

import java.beans.ConstructorProperties;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
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
public class LinkChatAssignmentRecord extends UpdatableRecordImpl<LinkChatAssignmentRecord> implements Record3<Long, Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>LINK_CHAT_ASSIGNMENT.ID</code>.
     */
    public void setId(@Nullable Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>LINK_CHAT_ASSIGNMENT.ID</code>.
     */
    @Nullable
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>LINK_CHAT_ASSIGNMENT.LINK_ID</code>.
     */
    public void setLinkId(@NotNull Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>LINK_CHAT_ASSIGNMENT.LINK_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getLinkId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>LINK_CHAT_ASSIGNMENT.CHAT_ID</code>.
     */
    public void setChatId(@NotNull Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>LINK_CHAT_ASSIGNMENT.CHAT_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getChatId() {
        return (Long) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row3<Long, Long, Long> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row3<Long, Long, Long> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return LinkChatAssignment.LINK_CHAT_ASSIGNMENT.ID;
    }

    @Override
    @NotNull
    public Field<Long> field2() {
        return LinkChatAssignment.LINK_CHAT_ASSIGNMENT.LINK_ID;
    }

    @Override
    @NotNull
    public Field<Long> field3() {
        return LinkChatAssignment.LINK_CHAT_ASSIGNMENT.CHAT_ID;
    }

    @Override
    @Nullable
    public Long component1() {
        return getId();
    }

    @Override
    @NotNull
    public Long component2() {
        return getLinkId();
    }

    @Override
    @NotNull
    public Long component3() {
        return getChatId();
    }

    @Override
    @Nullable
    public Long value1() {
        return getId();
    }

    @Override
    @NotNull
    public Long value2() {
        return getLinkId();
    }

    @Override
    @NotNull
    public Long value3() {
        return getChatId();
    }

    @Override
    @NotNull
    public LinkChatAssignmentRecord value1(@Nullable Long value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public LinkChatAssignmentRecord value2(@NotNull Long value) {
        setLinkId(value);
        return this;
    }

    @Override
    @NotNull
    public LinkChatAssignmentRecord value3(@NotNull Long value) {
        setChatId(value);
        return this;
    }

    @Override
    @NotNull
    public LinkChatAssignmentRecord values(@Nullable Long value1, @NotNull Long value2, @NotNull Long value3) {
        value1(value1);
        value2(value2);
        value3(value3);
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
    @ConstructorProperties({ "id", "linkId", "chatId" })
    public LinkChatAssignmentRecord(@Nullable Long id, @NotNull Long linkId, @NotNull Long chatId) {
        super(LinkChatAssignment.LINK_CHAT_ASSIGNMENT);

        setId(id);
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
            setId(value.getId());
            setLinkId(value.getLinkId());
            setChatId(value.getChatId());
            resetChangedOnNotNull();
        }
    }
}
