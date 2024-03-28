package com.ceos19.everytime.post.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.ceos19.everytime.board.domain.Board;
import com.ceos19.everytime.global.BaseEntity;
import com.ceos19.everytime.user.domain.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
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

    @Column(nullable = false)
    private int likeNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Post(final String title, final String content, final boolean isAnonymous, final User writer,
                final Board board) {
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.writer = writer;
        this.board = board;
    }

    public String getWriterNickname() {
        if (isAnonymous) {
            return "익명";
        }
        return writer.getNickname();
    }

    public void increaseLikeNumber() {
        likeNumber++;
    }

    public void decreaseLikeNumber() {
        if (likeNumber <= 0) {
            throw new IllegalArgumentException();
        }
        likeNumber--;
    }
}
