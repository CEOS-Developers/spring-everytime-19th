package com.ceos19.springboot.domain;

import com.ceos19.springboot.repository.UserRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Users {

    @Id
    @GeneratedValue
    private UUID userId;

    @Column(nullable = false)
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Posts> posts = new ArrayList<>();

    @Column(nullable = false)
    private String university;
    private String nickname;
    private String email;
    private String loginId;
    private String password;

    @Builder
    public Users(String username,
                 String university,
                 String nickname,
                 String email,
                 String loginId,
                 String password){
        this.username = username;
        this.university = university;
        this.nickname = nickname;
        this.email = email;
        this.loginId = loginId;
        this.password = password;
    }

    public Posts pressLike(Posts post) {
        post.plusLike();
        return post;
    }

    public Posts unlike(Posts post) {
        post.minusLike();
        return post;
    }

    public void addPosts(Posts post) {
        this.posts.add(post);
    }

    public void removePost(Posts post) {
        this.posts.remove(post);
    }
}
