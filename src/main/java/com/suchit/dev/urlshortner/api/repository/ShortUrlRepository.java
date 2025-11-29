package com.suchit.dev.urlshortner.api.repository;

import com.suchit.dev.urlshortner.api.entity.ShortUrlEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrlEntity, Long> {
    Optional<ShortUrlEntity> findByShortCode(String shortCode);
    Optional<ShortUrlEntity> findByOriginalUrl(String originalUrl);
    boolean existsByShortCode(String shortCode);
    Page<ShortUrlEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
