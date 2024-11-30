package ru.andryxx.gitworker.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryxx.gitworker.domain.model.GitEntity;

public interface GitEntityRepository extends JpaRepository<GitEntity, Long> {
}
