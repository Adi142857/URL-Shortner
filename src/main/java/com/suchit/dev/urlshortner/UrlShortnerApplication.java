package com.suchit.dev.urlshortner;

import com.suchit.dev.urlshortner.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableScheduling
public class UrlShortnerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlShortnerApplication.class, args);
	}

}
