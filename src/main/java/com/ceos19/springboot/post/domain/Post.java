package com.ceos19.springboot.post.domain;

import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.post.dto.PostModifyRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Post SET deleted = true WHERE post_id = ?")
@Where(clause = "deleted = false")
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
    private int likes = 0;
    private String imageUrl;
    private final boolean deleted = false;

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

    //Post 엔티티 업데이트 로직
    public void updateFromDto(PostModifyRequestDto dto) {
        if (dto.getTitle() != null) {
            this.title = dto.getTitle();
        }
        if (dto.getContent() != null) {
            this.content = dto.getContent();
        }
    }
}
