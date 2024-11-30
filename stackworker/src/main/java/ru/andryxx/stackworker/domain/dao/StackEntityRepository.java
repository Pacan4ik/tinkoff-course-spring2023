package ru.andryxx.stackworker.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryxx.stackworker.domain.model.StackEntity;

public interface StackEntityRepository extends JpaRepository<StackEntity, Long> {
}
