package com.ceos19.springboot.postlike.entity;

import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_like")
public class postLike {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long postLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public postLike(User user, Post post) {
        this.user = user;
        this.post = post;
    }
    public postLike()
    {

    }
}
