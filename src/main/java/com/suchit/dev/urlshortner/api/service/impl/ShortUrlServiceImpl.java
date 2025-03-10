package com.suchit.dev.urlshortner.api.service.impl;

import com.suchit.dev.urlshortner.api.entity.ShortUrlEntity;
import com.suchit.dev.urlshortner.api.repository.ShortUrlRepository;
import com.suchit.dev.urlshortner.api.service.ShortUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    private static final Logger logger = LoggerFactory.getLogger(ShortUrlServiceImpl.class);

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    private static final int MAX_SHORT_CODE_LENGTH = 7;

    @Override
    public String createOrGetShortenUrl(String originalUrl) {
        logger.info("Entering inside ShortUrlServiceImpl :: createOrGetShortenUrl. Shortening url {}", originalUrl);
        ShortUrlEntity shortUrlEntity;
        String shortCode = "";
        try {
            if(null!=originalUrl) {
                shortUrlEntity = shortUrlRepository.findByOriginalUrl(originalUrl);
                if(null!=shortUrlEntity) {
                    return shortUrlEntity.getShortCode();
                }
                logger.info("Original url does not have short code. Generating new short code");
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(originalUrl.getBytes());
                byte[] digest = md.digest();
                shortCode = Base64.getUrlEncoder().withoutPadding().encodeToString(digest).substring(0, 7);
                shortUrlEntity = new ShortUrlEntity();
                shortUrlEntity.setOriginalUrl(originalUrl);
                shortUrlEntity.setShortCode(shortCode.substring(0, MAX_SHORT_CODE_LENGTH));
                shortUrlEntity.setAccessCount(0);
                shortUrlEntity.setCreatedAt(new Date());
                shortUrlEntity.setUpdatedAt(new Date());
                shortUrlRepository.save(shortUrlEntity);
            }

        } catch (Exception e) {
            logger.error("Error occurred while creating short code for original url {}. Exception is {}", originalUrl, e.getLocalizedMessage());
        }
        logger.info("Exiting from ShortUrlServiceImpl :: createOrGetShortenUrl. Generated short code for original url {} is {}", originalUrl, shortCode);
        return shortCode;
    }

    @Override
    public String getOriginalUrl(String shortCode) {
        logger.info("Entering inside ShortUrlServiceImpl :: getOriginalUrl. Short Code is {}", shortCode);
        ShortUrlEntity shortUrlEntity;
        try{
            if(null!=shortCode) {
                shortUrlEntity = shortUrlRepository.findByShortCode(shortCode);
                logger.info("Retrieved original url for short code {} is {}", shortCode, shortUrlEntity);
                if(null!=shortUrlEntity) {
                    shortUrlEntity.setAccessCount(shortUrlEntity.getAccessCount()+1);
                    shortUrlEntity.setUpdatedAt(new Date());
                    shortUrlRepository.save(shortUrlEntity);
                    return shortUrlEntity.getOriginalUrl();
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while retrieving original url for short code {}. Exception is {}", shortCode, e.getLocalizedMessage());
        }
        return "";
    }

    @Override
    public boolean updateShortUrl(String shortCode, String newOriginalUrl) {
        logger.info("Entering inside ShortUrlServiceImpl :: updateShortUrl. Short Code is {} and new original url is {}", shortCode, newOriginalUrl);
        ShortUrlEntity shortUrlEntity;
        try{
            if(null!=shortCode && null!=newOriginalUrl){
                shortUrlEntity = shortUrlRepository.findByShortCode(shortCode);
                if(null!=shortUrlEntity) {
                    shortUrlEntity.setOriginalUrl(newOriginalUrl);
                    shortUrlEntity.setUpdatedAt(new Date());
                    shortUrlRepository.save(shortUrlEntity);
                    return true;
                }
            }
            logger.info("Short code {} does not exist or new original url is empty", shortCode);
        }catch (Exception e) {
            logger.error("Error occurred while updating new original url {} for short code {}. Exception is {}", newOriginalUrl, shortCode, e.getLocalizedMessage());
        }
        return false;
    }

    @Override
    public boolean deleteShortUrl(String shortCode) {
        logger.info("Entering inside ShortUrlServiceImpl :: deleteShortUrl. Short Code is {}", shortCode);
        ShortUrlEntity shortUrlEntity;
        try{
            if(null!=shortCode){
                shortUrlEntity = shortUrlRepository.findByShortCode(shortCode);
                if(null!=shortUrlEntity) {
                    shortUrlRepository.delete(shortUrlEntity);
                    return true;
                }
            }
            logger.info("Short code {} does not exist", shortCode);
        }catch (Exception e) {
            logger.error("Error occurred while deleting short code {}. Exception is {}", shortCode, e.getLocalizedMessage());
        }
        return false;
    }
}
