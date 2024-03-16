package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@ToString(exclude = {"user", "post"})
public class Heart {  // 게시물 좋아요
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "heart_id")
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "user_id")
    @Setter(value = PROTECTED)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    @Setter(value = PROTECTED)
    private Post post;

    protected static Heart createHeart(User user) {
        Heart heart = new Heart();
        user.addHearts(heart);

        return heart;
    }
}
