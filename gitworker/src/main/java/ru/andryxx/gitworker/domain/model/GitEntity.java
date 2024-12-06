package ru.andryxx.gitworker.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "git")
@Getter
@Setter
public class GitEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "last_event_id", nullable = true)
    private Long lastEventId;
}
