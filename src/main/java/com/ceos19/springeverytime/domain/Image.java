package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue
    private Long imageId;

    @Column(length = 200, nullable = false)
    private String imageUrl;

    @ManyToOne
    private Post post;
}
