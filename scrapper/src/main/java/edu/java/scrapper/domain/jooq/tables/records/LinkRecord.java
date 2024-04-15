/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.domain.jooq.tables.records;


import edu.java.scrapper.domain.jooq.tables.Link;

import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.time.OffsetDateTime;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
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
public class LinkRecord extends UpdatableRecordImpl<LinkRecord> implements Record5<Long, String, OffsetDateTime, OffsetDateTime, JSON> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>LINK.ID</code>.
     */
    public void setId(@Nullable Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>LINK.ID</code>.
     */
    @Nullable
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>LINK.URL</code>.
     */
    public void setUrl(@NotNull String value) {
        set(1, value);
    }

    /**
     * Getter for <code>LINK.URL</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getUrl() {
        return (String) get(1);
    }

    /**
     * Setter for <code>LINK.CREATED_AT</code>.
     */
    public void setCreatedAt(@Nullable OffsetDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>LINK.CREATED_AT</code>.
     */
    @Nullable
    public OffsetDateTime getCreatedAt() {
        return (OffsetDateTime) get(2);
    }

    /**
     * Setter for <code>LINK.CHECKED_AT</code>.
     */
    public void setCheckedAt(@Nullable OffsetDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>LINK.CHECKED_AT</code>.
     */
    @Nullable
    public OffsetDateTime getCheckedAt() {
        return (OffsetDateTime) get(3);
    }

    /**
     * Setter for <code>LINK.ADDITIONAL_INFO</code>.
     */
    public void setAdditionalInfo(@Nullable JSON value) {
        set(4, value);
    }

    /**
     * Getter for <code>LINK.ADDITIONAL_INFO</code>.
     */
    @Nullable
    public JSON getAdditionalInfo() {
        return (JSON) get(4);
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
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row5<Long, String, OffsetDateTime, OffsetDateTime, JSON> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row5<Long, String, OffsetDateTime, OffsetDateTime, JSON> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return Link.LINK.ID;
    }

    @Override
    @NotNull
    public Field<String> field2() {
        return Link.LINK.URL;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field3() {
        return Link.LINK.CREATED_AT;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field4() {
        return Link.LINK.CHECKED_AT;
    }

    @Override
    @NotNull
    public Field<JSON> field5() {
        return Link.LINK.ADDITIONAL_INFO;
    }

    @Override
    @Nullable
    public Long component1() {
        return getId();
    }

    @Override
    @NotNull
    public String component2() {
        return getUrl();
    }

    @Override
    @Nullable
    public OffsetDateTime component3() {
        return getCreatedAt();
    }

    @Override
    @Nullable
    public OffsetDateTime component4() {
        return getCheckedAt();
    }

    @Override
    @Nullable
    public JSON component5() {
        return getAdditionalInfo();
    }

    @Override
    @Nullable
    public Long value1() {
        return getId();
    }

    @Override
    @NotNull
    public String value2() {
        return getUrl();
    }

    @Override
    @Nullable
    public OffsetDateTime value3() {
        return getCreatedAt();
    }

    @Override
    @Nullable
    public OffsetDateTime value4() {
        return getCheckedAt();
    }

    @Override
    @Nullable
    public JSON value5() {
        return getAdditionalInfo();
    }

    @Override
    @NotNull
    public LinkRecord value1(@Nullable Long value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value2(@NotNull String value) {
        setUrl(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value3(@Nullable OffsetDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value4(@Nullable OffsetDateTime value) {
        setCheckedAt(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value5(@Nullable JSON value) {
        setAdditionalInfo(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord values(@Nullable Long value1, @NotNull String value2, @Nullable OffsetDateTime value3, @Nullable OffsetDateTime value4, @Nullable JSON value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LinkRecord
     */
    public LinkRecord() {
        super(Link.LINK);
    }

    /**
     * Create a detached, initialised LinkRecord
     */
    @ConstructorProperties({ "id", "url", "createdAt", "checkedAt", "additionalInfo" })
    public LinkRecord(@Nullable Long id, @NotNull String url, @Nullable OffsetDateTime createdAt, @Nullable OffsetDateTime checkedAt, @Nullable JSON additionalInfo) {
        super(Link.LINK);

        setId(id);
        setUrl(url);
        setCreatedAt(createdAt);
        setCheckedAt(checkedAt);
        setAdditionalInfo(additionalInfo);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised LinkRecord
     */
    public LinkRecord(edu.java.scrapper.domain.jooq.tables.pojos.Link value) {
        super(Link.LINK);

        if (value != null) {
            setId(value.getId());
            setUrl(value.getUrl());
            setCreatedAt(value.getCreatedAt());
            setCheckedAt(value.getCheckedAt());
            setAdditionalInfo(value.getAdditionalInfo());
            resetChangedOnNotNull();
        }
    }
}
