package com.suchit.dev.urlshortner.api.mapper;

import com.suchit.dev.urlshortner.api.dto.ShortUrlResponse;
import com.suchit.dev.urlshortner.api.entity.ShortUrlEntity;

public final class ShortUrlMapper {

    private ShortUrlMapper() {
    }

    public static ShortUrlResponse toResponse(ShortUrlEntity entity, String shortBaseUrl) {
        String shortUrl = shortBaseUrl.endsWith("/")
                ? shortBaseUrl + entity.getShortCode()
                : shortBaseUrl + "/" + entity.getShortCode();
        return new ShortUrlResponse(
                entity.getShortCode(),
                entity.getOriginalUrl(),
                shortUrl,
                entity.getAccessCount(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

