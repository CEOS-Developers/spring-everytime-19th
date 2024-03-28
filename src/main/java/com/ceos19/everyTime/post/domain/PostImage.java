package com.ceos19.everyTime.post.domain;

import com.ceos19.everyTime.post.domain.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Arrays;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",nullable = false,updatable = false)
    private Post post;

    @Column(nullable = false)
    private String accessUrl;

    @Column(nullable = false)
    private String originalName;




    @Builder
    public PostImage(Post post,String accessUrl,String originalName){
        this.post = post;
        this.accessUrl=accessUrl;
        this.originalName = originalName;
    }








}
