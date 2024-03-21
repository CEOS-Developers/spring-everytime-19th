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
public class Comment extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;          //댓글

    /*
    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)        //대댓글
    private List<Comment> childrenComment = new ArrayList<>();
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(nullable = false)
    private int likes;

    @Column(nullable = false)
    private boolean isAnonymous;

    @Column(nullable = false)
    private boolean isDeleted;


    @Builder
    public Comment(Post postId, Comment parentComment, Member author, String content, boolean isAnonymous) {
        this.postId = postId;
        this.parentComment = parentComment;
        //this.childrenComment = childrenComment;
        this.author = author;
        this.content = content;
        this.likes = 0;
        this.isAnonymous = isAnonymous;
        this.isDeleted = false;
    }

}