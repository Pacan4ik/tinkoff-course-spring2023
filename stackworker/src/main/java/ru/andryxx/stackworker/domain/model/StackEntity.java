package ru.andryxx.stackworker.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stack")
public class StackEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "answer_count", nullable = false)
    private Long answerCount;

    @Column(name = "comment_count", nullable = false)
    private Long commentCount;

    @Column(name = "last_activity_date", nullable = true)
    private OffsetDateTime lastActivityDate;
}
