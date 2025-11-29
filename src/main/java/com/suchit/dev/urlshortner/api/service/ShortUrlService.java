package com.suchit.dev.urlshortner.api.service;

import com.suchit.dev.urlshortner.api.entity.ShortUrlEntity;

import java.util.List;

public interface ShortUrlService {
    ShortUrlEntity createShortUrl(String originalUrl, String customAlias);
    ShortUrlEntity getShortUrlByCode(String shortCode);
    ShortUrlEntity updateShortUrl(String shortCode, String newOriginalUrl);
    void deleteShortUrl(String shortCode);
    String resolveShortCode(String shortCode);
    List<ShortUrlEntity> getRecentShortUrls(int limit);
}
