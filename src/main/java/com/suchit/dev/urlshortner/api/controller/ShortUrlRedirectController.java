package com.suchit.dev.urlshortner.api.controller;

import com.suchit.dev.urlshortner.api.service.ShortUrlService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortUrlRedirectController {

    private final ShortUrlService shortUrlService;

    public ShortUrlRedirectController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @GetMapping("/r/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String targetUrl = shortUrlService.resolveShortCode(shortCode);
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, targetUrl)
                .build();
    }
}

