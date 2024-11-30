package ru.andryxx.gitworker.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.andryxx.gitworker.domain.model.GitEntity;

public interface GitEntityRepository extends JpaRepository<GitEntity, Long> {
}
