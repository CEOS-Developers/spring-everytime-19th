package com.ceos19.everyTime.post.domain;

import com.ceos19.everyTime.common.BaseEntity;
import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.member.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id",nullable = false,updatable = false)
    private Community community;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,length = 500)
    private String contents;

    @Column(nullable = false)
    private int replyCount;

    @Column(nullable = false)
    boolean isQuestion;

    @Builder
    public Post(Member member,Community community,String writer,String title, String contents,boolean isQuestion){
        this.member=member;
        this.community=community;
        this.writer=writer;
        this.title=title;
        this.contents=contents;
        this.isQuestion=isQuestion;
        this.replyCount=0;
        this.likeCount=0;
    }

    @OneToMany(mappedBy = "post")
    private List<Reply> replyList=new ArrayList<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<PostImage> postImageList = new ArrayList<>();





}
