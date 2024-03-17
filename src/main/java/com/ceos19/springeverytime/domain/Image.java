package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Image {
    @Id
    @GeneratedValue
    private Long imageId;

    @NonNull
    @Column(length = 200, nullable = false)
    private String imageUrl;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
