package com.suchit.dev.urlshortner.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateShortUrlRequest(
        @NotBlank(message = "originalUrl is required")
        String originalUrl,

        @Size(min = 4, max = 20, message = "customAlias must be between 4 and 20 characters")
        @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "customAlias must be alphanumeric or include -/_")
        String customAlias
) { }

