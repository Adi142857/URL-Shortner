package com.suchit.dev.urlshortner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    /**
     * Base URL exposed for short links, e.g. https://sho.rt/r
     */
    private String shortBaseUrl = "http://localhost:8082/r";

    /**
     * Comma separated list of allowed origins for CORS.
     */
    private String corsAllowedOrigins = "http://localhost:3000";

    public String getShortBaseUrl() {
        return shortBaseUrl;
    }

    public void setShortBaseUrl(String shortBaseUrl) {
        this.shortBaseUrl = shortBaseUrl;
    }

    public String getCorsAllowedOrigins() {
        return corsAllowedOrigins;
    }

    public void setCorsAllowedOrigins(String corsAllowedOrigins) {
        this.corsAllowedOrigins = corsAllowedOrigins;
    }
}

