package com.suchit.dev.urlshortner.api.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateShortUrlRequest(
        @NotBlank(message = "originalUrl is required")
        String originalUrl
) { }

