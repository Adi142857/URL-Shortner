package com.suchit.dev.urlshortner.api.dto;

import java.util.Date;

public record ShortUrlResponse(
        String shortCode,
        String originalUrl,
        String shortUrl,
        int accessCount,
        Date createdAt,
        Date updatedAt
) { }

