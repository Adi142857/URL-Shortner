package com.suchit.dev.urlshortner.api.service;

import java.util.Optional;

public interface ShortUrlService {
    String createOrGetShortenUrl(String originalUrl);
    String getOriginalUrl(String shortCode);
    boolean updateShortUrl(String shortCode, String newOriginalUrl);
    boolean deleteShortUrl(String shortCode);


}
