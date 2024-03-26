package edu.java.scrapper.domain.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "registered_at", insertable = false)
    private OffsetDateTime registeredAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "link_chat_assignment",
        joinColumns = @JoinColumn(name = "chat_id"),
        inverseJoinColumns = @JoinColumn(name = "link_id"))
    Set<Link> subscribedLinks;

}
