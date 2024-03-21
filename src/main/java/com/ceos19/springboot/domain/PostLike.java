package com.ceos19.springboot.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class PostLike {
    @Id
    @GeneratedValue
    private UUID likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Posts post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;
}
