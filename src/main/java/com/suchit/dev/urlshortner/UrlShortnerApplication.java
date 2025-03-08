package com.suchit.dev.urlshortner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(value = "com.*")
@EnableJpaRepositories(value = "com.*")
@EnableScheduling
public class UrlShortnerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlShortnerApplication.class, args);
	}

}
