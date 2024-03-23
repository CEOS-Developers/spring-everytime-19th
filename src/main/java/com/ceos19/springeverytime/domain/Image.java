package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
