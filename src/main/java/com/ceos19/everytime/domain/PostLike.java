package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor
@Entity
@ToString
public class PostLike {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    @Setter(value = PRIVATE)
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    @Setter(value = PRIVATE)
    private User user;

    public static PostLike createPostLike(Post post, User user) {
        PostLike postLike = new PostLike();
        postLike.setPost(post);
        postLike.setUser(user);
        return postLike;
    }
}
