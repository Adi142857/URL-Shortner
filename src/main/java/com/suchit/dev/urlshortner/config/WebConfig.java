package com.suchit.dev.urlshortner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AppProperties appProperties;

    public WebConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String configuredOrigins = appProperties.getCorsAllowedOrigins();
        String[] origins = Arrays.stream(configuredOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isBlank())
                .toArray(String[]::new);

        registry.addMapping("/**")
                .allowedOrigins(origins.length == 0 ? new String[]{"http://localhost:3000"} : origins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

