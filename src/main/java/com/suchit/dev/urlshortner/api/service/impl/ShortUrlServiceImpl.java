package com.suchit.dev.urlshortner.api.service.impl;

import com.suchit.dev.urlshortner.api.entity.ShortUrlEntity;
import com.suchit.dev.urlshortner.api.exception.BadRequestException;
import com.suchit.dev.urlshortner.api.exception.ResourceNotFoundException;
import com.suchit.dev.urlshortner.api.repository.ShortUrlRepository;
import com.suchit.dev.urlshortner.api.service.ShortUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@Transactional
public class ShortUrlServiceImpl implements ShortUrlService {

    private static final Logger logger = LoggerFactory.getLogger(ShortUrlServiceImpl.class);
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int GENERATED_CODE_LENGTH = 7;
    private static final int MAX_GENERATION_ATTEMPTS = 5;
    private static final Pattern SHORT_CODE_PATTERN = Pattern.compile("^[A-Za-z0-9_-]{4,20}$");

    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    @Override
    public ShortUrlEntity createShortUrl(String originalUrl, String customAlias) {
        validateOriginalUrl(originalUrl);
        String cleanedUrl = originalUrl.trim();

        if (!StringUtils.hasText(customAlias)) {
            return shortUrlRepository.findByOriginalUrl(cleanedUrl)
                    .orElseGet(() -> persistShortUrl(cleanedUrl, null));
        }

        String normalizedAlias = customAlias.trim();
        validateShortCode(normalizedAlias);

        if (shortUrlRepository.existsByShortCode(normalizedAlias)) {
            throw new BadRequestException("Custom alias already exists. Please choose another one.");
        }
        return persistShortUrl(cleanedUrl, normalizedAlias);
    }

    @Override
    @Transactional(readOnly = true)
    public ShortUrlEntity getShortUrlByCode(String shortCode) {
        validateShortCode(shortCode);
        return shortUrlRepository.findByShortCode(shortCode.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Short code %s not found".formatted(shortCode)));
    }

    @Override
    public ShortUrlEntity updateShortUrl(String shortCode, String newOriginalUrl) {
        validateShortCode(shortCode);
        validateOriginalUrl(newOriginalUrl);
        ShortUrlEntity entity = getShortUrlByCode(shortCode.trim());
        entity.setOriginalUrl(newOriginalUrl.trim());
        entity.setUpdatedAt(new Date());
        return shortUrlRepository.save(entity);
    }

    @Override
    public void deleteShortUrl(String shortCode) {
        ShortUrlEntity entity = getShortUrlByCode(shortCode);
        shortUrlRepository.delete(entity);
    }

    @Override
    public String resolveShortCode(String shortCode) {
        ShortUrlEntity entity = getShortUrlByCode(shortCode);
        entity.setAccessCount(entity.getAccessCount() + 1);
        entity.setUpdatedAt(new Date());
        shortUrlRepository.save(entity);
        return entity.getOriginalUrl();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortUrlEntity> getRecentShortUrls(int limit) {
        int pageSize = Math.max(limit, 1);
        return shortUrlRepository
                .findAllByOrderByCreatedAtDesc(PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")))
                .getContent();
    }

    private ShortUrlEntity persistShortUrl(String originalUrl, String providedShortCode) {
        String shortCode = StringUtils.hasText(providedShortCode)
                ? providedShortCode
                : generateUniqueShortCode();
        ShortUrlEntity entity = new ShortUrlEntity();
        entity.setOriginalUrl(originalUrl);
        entity.setShortCode(shortCode);
        entity.setAccessCount(0);
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        ShortUrlEntity saved = shortUrlRepository.save(entity);
        logger.info("Created short code {} for url {}", shortCode, originalUrl);
        return saved;
    }

    private String generateUniqueShortCode() {
        for (int attempt = 0; attempt < MAX_GENERATION_ATTEMPTS; attempt++) {
            String candidate = randomBase62Code();
            if (!shortUrlRepository.existsByShortCode(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("Unable to generate a unique short code after several attempts");
    }

    private String randomBase62Code() {
        StringBuilder builder = new StringBuilder(GENERATED_CODE_LENGTH);
        for (int i = 0; i < GENERATED_CODE_LENGTH; i++) {
            int index = RANDOM.nextInt(BASE62.length());
            builder.append(BASE62.charAt(index));
        }
        return builder.toString();
    }

    private void validateShortCode(String shortCode) {
        if (!StringUtils.hasText(shortCode) || !SHORT_CODE_PATTERN.matcher(shortCode.trim()).matches()) {
            throw new BadRequestException("Short code must be 4-20 characters of letters, numbers, _ or -");
        }
    }

    private void validateOriginalUrl(String originalUrl) {
        if (!StringUtils.hasText(originalUrl)) {
            throw new BadRequestException("originalUrl must not be blank");
        }
        String trimmed = originalUrl.trim().toLowerCase(Locale.ENGLISH);
        if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            throw new BadRequestException("originalUrl must start with http:// or https://");
        }
    }
}
