package com.suchit.dev.urlshortner.api.repository;

import com.suchit.dev.urlshortner.api.entity.ShortUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrlEntity, Long> {
    ShortUrlEntity findByShortCode(String shortCode);
    ShortUrlEntity findByOriginalUrl(String originalUrl);
    boolean existsByShortCode(String shortCode);
}
