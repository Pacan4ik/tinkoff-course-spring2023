package edu.java.scrapper.domain.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "link")
public class Link {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "link_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "link_id_generator", sequenceName = "link_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "url", nullable = false, length = Integer.MAX_VALUE)
    private String url;

    @Column(name = "created_at", insertable = false)
    private OffsetDateTime createdAt;

    @Column(name = "checked_at", insertable = false)
    private OffsetDateTime checkedAt;

    @ManyToMany(mappedBy = "subscribedLinks", fetch = FetchType.LAZY)
    Set<Chat> subscribedChats;
}
