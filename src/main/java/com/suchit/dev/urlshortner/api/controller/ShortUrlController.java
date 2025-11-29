package com.suchit.dev.urlshortner.api.controller;

import com.suchit.dev.urlshortner.api.dto.CreateShortUrlRequest;
import com.suchit.dev.urlshortner.api.dto.ShortUrlResponse;
import com.suchit.dev.urlshortner.api.dto.UpdateShortUrlRequest;
import com.suchit.dev.urlshortner.api.mapper.ShortUrlMapper;
import com.suchit.dev.urlshortner.api.service.ShortUrlService;
import com.suchit.dev.urlshortner.config.AppProperties;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/urls")
@Validated
public class ShortUrlController {

    private final ShortUrlService shortUrlService;
    private final AppProperties appProperties;

    public ShortUrlController(ShortUrlService shortUrlService, AppProperties appProperties) {
        this.shortUrlService = shortUrlService;
        this.appProperties = appProperties;
    }

    @PostMapping
    public ResponseEntity<ShortUrlResponse> createShortUrl(@Valid @RequestBody CreateShortUrlRequest request) {
        var entity = shortUrlService.createShortUrl(request.originalUrl(), request.customAlias());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ShortUrlMapper.toResponse(entity, appProperties.getShortBaseUrl()));
    }

    @GetMapping
    public ResponseEntity<List<ShortUrlResponse>> listShortUrls(@RequestParam(defaultValue = "10") int limit) {
        var items = shortUrlService.getRecentShortUrls(Math.min(Math.max(limit, 1), 100))
                .stream()
                .map(entity -> ShortUrlMapper.toResponse(entity, appProperties.getShortBaseUrl()))
                .toList();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<ShortUrlResponse> getShortUrl(@PathVariable String shortCode) {
        var entity = shortUrlService.getShortUrlByCode(shortCode);
        return ResponseEntity.ok(ShortUrlMapper.toResponse(entity, appProperties.getShortBaseUrl()));
    }

    @PutMapping("/{shortCode}")
    public ResponseEntity<ShortUrlResponse> updateShortUrl(@PathVariable String shortCode,
                                                           @Valid @RequestBody UpdateShortUrlRequest request) {
        var entity = shortUrlService.updateShortUrl(shortCode, request.originalUrl());
        return ResponseEntity.ok(ShortUrlMapper.toResponse(entity, appProperties.getShortBaseUrl()));
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortCode) {
        shortUrlService.deleteShortUrl(shortCode);
        return ResponseEntity.noContent().build();
    }
}

