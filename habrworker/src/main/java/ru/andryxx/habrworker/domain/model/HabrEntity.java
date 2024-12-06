package ru.andryxx.habrworker.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "habr")
@Getter
@Setter
public class HabrEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "comment_count", nullable = false)
    private Long commentCount;

    @Column(name = "content_sha1_hash", nullable = true)
    private String contentSha1Hash;
}
