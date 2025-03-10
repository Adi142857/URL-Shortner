package com.suchit.dev.urlshortner.api.cli;

import com.suchit.dev.urlshortner.api.service.ShortUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UrlShortnerCLI implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(UrlShortnerCLI.class);

    @Autowired
    private ShortUrlService shortUrlService;

    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Welcome to CLI-based URL Shortner!");

        while(true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Shorten a URL");
            System.out.println("2. Retrieve original URL");
            System.out.println("3. Update short URL");
            System.out.println("4. Delete a short URL");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> createOrGetShortUrl();
                case 2 -> retrieveShortUrl();
                case 3 -> updateShortUrl();
                case 4 -> deleteShortUrl();
                case 5 -> {
                    System.out.println("Exiting...!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again!");

            }
        }
    }

    private void createOrGetShortUrl() {
        System.out.print("Enter the original URL: ");
        String originalUrl = scanner.nextLine();
        String shortCode = shortUrlService.createOrGetShortenUrl(originalUrl);
        System.out.println("Shortened URL is: " + shortCode);
    }

    private void retrieveShortUrl() {
        System.out.print("Enter the short code: ");
        String shortCode = scanner.nextLine();
        String originalUrl = shortUrlService.getOriginalUrl(shortCode);
        if(originalUrl.isEmpty()) {
            System.out.println("Short URL does not exist!");
            return;
        }
        System.out.println("Original URL is: " + originalUrl);
    }

    private void updateShortUrl() {
        System.out.print("Enter the short code to update: ");
        String shortCode = scanner.nextLine();
        System.out.print("Enter the new original URL: ");
        String newOriginalUrl = scanner.nextLine();
        boolean isUpdated = shortUrlService.updateShortUrl(shortCode, newOriginalUrl);
        if(isUpdated) {
            System.out.println("Short URL updated successfully!");
        } else {
            System.out.println("Short URL does not exist!");
        }
    }

    private void deleteShortUrl() {
        System.out.print("Enter the short code to delete: ");
        String shortCode = scanner.nextLine();
        boolean isDeleted = shortUrlService.deleteShortUrl(shortCode);
        if(isDeleted) {
            System.out.println("Short URL deleted successfully!");
        } else {
            System.out.println("Short URL does not exist!");
        }
    }
}
