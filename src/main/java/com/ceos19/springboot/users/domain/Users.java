package com.ceos19.springboot.users.domain;

import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.school.domain.School;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE Users SET deleted = true WHERE user_id = ?")
@Where(clause = "deleted = false")
public class Users {

    @Id
    @GeneratedValue
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;
    private String email;
    private String loginId;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private School university;
    private final boolean deleted = false; // 기본값을 false로 설정

    public Users(String username,
                 String nickname,
                 School university,
                 String email,
                 String loginId,
                 String password){
        this.username = username;
        this.nickname = nickname;
        this.university = university;
        this.email = email;
        this.loginId = loginId;
        this.password = password;
    }

    public Post pressLike(Post post) {
        post.plusLike();
        return post;
    }

    public Post unlike(Post post) {
        post.minusLike();
        return post;
    }
}
