package com.suchit.dev.urlshortner.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "short_url")
public class ShortUrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "short_code")
    private String shortCode;

    @Column(name = "original_url")
    private String originalUrl;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "access_count")
    private int accessCount = 0;

    @Column(name = "updated_at")
    private Date updatedAt;

}
