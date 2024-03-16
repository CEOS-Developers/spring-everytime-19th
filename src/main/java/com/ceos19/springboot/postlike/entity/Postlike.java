package com.ceos19.springboot.postlike.entity;

import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "post_like")
public class Postlike {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long postLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

}
