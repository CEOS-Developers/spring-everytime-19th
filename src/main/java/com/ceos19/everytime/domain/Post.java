package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity{

    public static final int MAX_TITLE_LENGTH = 100;
    public static final int MAX_CONTENT_LENGTH = 2000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    private boolean isAnonymous;

    @Column(nullable = false)
    private int likes;

    @Builder
    public Post(String title, String content, Member author, Board board, boolean isAnonymous){
        this.title = title;
        this.content = content;
        this.author = author;
        this.board = board;
        this.isAnonymous = isAnonymous;
        this.likes = 0;
    }

    public void addLike(){
        likes++;
    }

    public void changeTitle(final String title) {
        validateTitle(title);
        this.title = title;
    }

    public void changeContent(final String content) {
        validateContent(content);
        this.content = content;
    }

    private boolean validateTitle(String title){
        if(title.isEmpty() || title.length()> MAX_TITLE_LENGTH)
            return false;
        return true;
    }

    private boolean validateContent(String content){
        if(content.isEmpty() || content.length()> MAX_CONTENT_LENGTH)
            return false;
        return true;
    }

    private boolean validatePost(String title, String content){
        if(!validateTitle(title) || !validateContent(content))
            return false;
        return true;
    }


}
