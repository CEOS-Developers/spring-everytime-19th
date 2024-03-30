package com.ceos19.everytime.domain.AboutPost;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.domain.AboutUser.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Post extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Builder.Default
    private Long likeNum = 0L;

    @Column(nullable = false)
    @Builder.Default
    private boolean isReported = false;

    @Column(nullable = false)
    @Builder.Default
    private Long commentNum = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long scrapNum = 0L;

    @Column(nullable = false)
    @Builder.Default
    private boolean isAnonymity = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


    @OneToMany(mappedBy = "post")
    @Builder.Default
    List<Comment> comments = new ArrayList<Comment>();

    @OneToMany(mappedBy = "post")
    @Builder.Default
    List<Image> images = new ArrayList<Image>();

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
