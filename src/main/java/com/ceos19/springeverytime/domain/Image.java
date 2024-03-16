package com.ceos19.springeverytime.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

public class Image {
    @Id
    private Long id;

    @Column(length = 200, nullable = false)
    private String imageUrl;

    @OneToMany
    private Post post;
}
