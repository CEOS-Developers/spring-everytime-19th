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
    @Column(name="post_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(name="like_num")
    private Long likeNum=0L;

    @Column(name="is_reported", nullable = false)
    private boolean isReported=false;

    @Column(name="comment_num", nullable = false)
    private Long commentNum=0L;

    @Column(name="scrap_num", nullable = false)
    private Long scrapNum=0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Board board;

    @OneToMany(mappedBy = "post")
    List<Scrap> scraps = new ArrayList<Scrap>();

    @OneToMany(mappedBy = "post")
    List<Comment> comments = new ArrayList<Comment>();

    @OneToMany(mappedBy="post")
    List<Image> images = new ArrayList<Image>();
}
