package com.ceos19.springeverytime.domain.image.domain;

import com.ceos19.springeverytime.domain.BaseEntity;
import com.ceos19.springeverytime.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@RequiredArgsConstructor
public class Image extends BaseEntity {
    @Id
    @GeneratedValue
    private Long imageId;

    @NonNull
    @Column(length = 200, nullable = false, updatable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Image(String imageUrl, Post post) {
        this.imageUrl = imageUrl;
        this.post = post;
    }
}
