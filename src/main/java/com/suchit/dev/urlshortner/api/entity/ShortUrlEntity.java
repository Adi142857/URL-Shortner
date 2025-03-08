package com.suchit.dev.urlshortner.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

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

    @Column(name = "shortCode")
    private String shortCode;

    @Column(name = "originalUrl")
    private String originalUrl;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "accessCount")
    private int accessCount = 0;

    @Column(name = "updatedAt")
    private Date updatedAt;

}
