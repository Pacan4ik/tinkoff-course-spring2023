/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.domain.jooq.tables.pojos;


import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.JSON;


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
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String url;
    private OffsetDateTime createdAt;
    private OffsetDateTime checkedAt;
    private JSON additionalInfo;

    public Link() {}

    public Link(Link value) {
        this.id = value.id;
        this.url = value.url;
        this.createdAt = value.createdAt;
        this.checkedAt = value.checkedAt;
        this.additionalInfo = value.additionalInfo;
    }

    @ConstructorProperties({ "id", "url", "createdAt", "checkedAt", "additionalInfo" })
    public Link(
        @Nullable Long id,
        @NotNull String url,
        @Nullable OffsetDateTime createdAt,
        @Nullable OffsetDateTime checkedAt,
        @Nullable JSON additionalInfo
    ) {
        this.id = id;
        this.url = url;
        this.createdAt = createdAt;
        this.checkedAt = checkedAt;
        this.additionalInfo = additionalInfo;
    }

    /**
     * Getter for <code>LINK.ID</code>.
     */
    @Nullable
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>LINK.ID</code>.
     */
    public void setId(@Nullable Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>LINK.URL</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getUrl() {
        return this.url;
    }

    /**
     * Setter for <code>LINK.URL</code>.
     */
    public void setUrl(@NotNull String url) {
        this.url = url;
    }

    /**
     * Getter for <code>LINK.CREATED_AT</code>.
     */
    @Nullable
    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>LINK.CREATED_AT</code>.
     */
    public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Getter for <code>LINK.CHECKED_AT</code>.
     */
    @Nullable
    public OffsetDateTime getCheckedAt() {
        return this.checkedAt;
    }

    /**
     * Setter for <code>LINK.CHECKED_AT</code>.
     */
    public void setCheckedAt(@Nullable OffsetDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }

    /**
     * Getter for <code>LINK.ADDITIONAL_INFO</code>.
     */
    @Nullable
    public JSON getAdditionalInfo() {
        return this.additionalInfo;
    }

    /**
     * Setter for <code>LINK.ADDITIONAL_INFO</code>.
     */
    public void setAdditionalInfo(@Nullable JSON additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Link other = (Link) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.url == null) {
            if (other.url != null)
                return false;
        }
        else if (!this.url.equals(other.url))
            return false;
        if (this.createdAt == null) {
            if (other.createdAt != null)
                return false;
        }
        else if (!this.createdAt.equals(other.createdAt))
            return false;
        if (this.checkedAt == null) {
            if (other.checkedAt != null)
                return false;
        }
        else if (!this.checkedAt.equals(other.checkedAt))
            return false;
        if (this.additionalInfo == null) {
            if (other.additionalInfo != null)
                return false;
        }
        else if (!this.additionalInfo.equals(other.additionalInfo))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.url == null) ? 0 : this.url.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.checkedAt == null) ? 0 : this.checkedAt.hashCode());
        result = prime * result + ((this.additionalInfo == null) ? 0 : this.additionalInfo.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Link (");

        sb.append(id);
        sb.append(", ").append(url);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(checkedAt);
        sb.append(", ").append(additionalInfo);

        sb.append(")");
        return sb.toString();
    }
}
