package com.ceos19.springboot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;


    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int likes;
    private String imageUrl;

    public Post(Users user,
                String title,
                String content,
                LocalDateTime createdAt,
                int likes,
                String imageUrl){
        this.user = user;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.likes = likes;
        this.imageUrl = imageUrl;
    }
    public void plusLike() {
        this.likes += 1;
    }
    public void minusLike(){ this.likes -= 1; }
}
