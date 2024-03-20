package com.ceos19.everytime.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 65535)
    private String content;

    @Column(nullable = false)
    private boolean isAnonymous;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Post(final Long id, final String title, final String content, final boolean isAnonymous, final User user,
                final Board board) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.user = user;
        this.board = board;
    }

    public String getWriterNickname() {
        if (isAnonymous) {
            return "익명";
        }
        return user.getNickname();
    }
}
