package com.suchit.dev.urlshortner.api.repository;

import com.suchit.dev.urlshortner.api.entity.ShortUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortUrlEntity, Long> {
}
