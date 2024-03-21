package edu.java.scrapper.domain.jpa.dao;

import edu.java.scrapper.domain.jpa.model.Link;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUrl(String url);
}
