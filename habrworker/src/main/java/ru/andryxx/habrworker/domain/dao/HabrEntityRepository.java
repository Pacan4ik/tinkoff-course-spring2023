package ru.andryxx.habrworker.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryxx.habrworker.domain.model.HabrEntity;

public interface HabrEntityRepository extends JpaRepository<HabrEntity, Long> {
}
