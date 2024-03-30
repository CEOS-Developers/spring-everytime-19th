package com.ceos19.everytime.domain.AboutPost;


import com.ceos19.everytime.domain.AboutUser.User;
import com.ceos19.everytime.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    private Long likeNum = 0L;

    @Column(nullable = false)
    private boolean isReported = false;

    @Column(nullable = false)
    private Long commentNum = 0L;

    @Column(nullable = false)
    private Long scrapNum = 0L;

    @Column(nullable = false)
    private boolean isAnonymity = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


    @OneToMany(mappedBy = "post")
    List<Comment> comments = new ArrayList<Comment>();

    @OneToMany(mappedBy = "post")
    List<Image> images = new ArrayList<Image>();

    @Builder
    public Post(Long postId, String title, String contents, Long likeNum, boolean isReported, Long commentNum, Long scrapNum, boolean isAnonymity, User user, Board board) {
        this.postId = postId;
        this.title = title;
        this.contents = contents;
        this.likeNum = likeNum;
        this.isReported = isReported;
        this.commentNum = commentNum;
        this.scrapNum = scrapNum;
        this.isAnonymity = isAnonymity;
        this.user = user;
        this.board = board;
    }



    public void addLikeNum(Long likeNum) {
        this.likeNum = likeNum+1;
    }

    public void deleteLikeNum(Long likeNum) {
        if(this.likeNum == 0){
            this.likeNum = 0L;
        } else {
            this.likeNum = likeNum-1;
        }
    }



}
