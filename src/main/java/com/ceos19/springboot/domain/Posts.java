package com.ceos19.springboot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Posts {
    @Id
    @GeneratedValue
    private UUID postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int likes;
    private String imageUrl;

    public Posts(Users user,
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

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }
}
