package com.ceos19.everyTime.post.domain;

import com.ceos19.everyTime.common.BaseEntity;
import com.ceos19.everyTime.member.domain.Member;
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
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",nullable = false,updatable = false)
    private Post post;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false,updatable = false)
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",updatable = false)
    private Reply parentId;

    @Column(nullable = false,length = 100)
    private String contents;
    @Builder
    public Reply(Post post,Member member,Reply parentId,String contents){
        this.post=post;
        this.member=member;
        this.parentId=parentId;
        this.contents=contents;
    }





}
