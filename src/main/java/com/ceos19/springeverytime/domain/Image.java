package com.ceos19.springeverytime.domain;

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
    @Column(length = 200, nullable = false)
    private String imageUrl;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
