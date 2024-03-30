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
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql="UPDATE reply set deleted = true where reply_id = ?")
//@Where(clause = "deleted = false")
@Getter
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",nullable = false,updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Reply parent;

    @Column(nullable = false,length = 100)
    private String contents;

    @Column(nullable = false)
    private boolean deleted=false;

    private boolean isHideNickName;


    private int likeCount=0;

    private String writer;
    @Builder
    public Reply(Post post,Member member,Reply parent,String contents,boolean isHideNickName,String writer){
        this.post=post;
        this.member=member;
        this.parent =parent;
        this.contents=contents;
        this.isHideNickName = isHideNickName;
        this.writer = writer;
    }

    public void changeParentByDeleteOnlyHaveChild(String deletedContents){
        this.member=null;
        this.contents = deletedContents;
    }

    public void increaseLikeCount(){
        this.likeCount++;
    }
    public void decreaseLikeCount(){
        if(this.likeCount>0){
            this.likeCount--;
        }
    }

    @OneToMany(mappedBy = "parent")
    List<Reply> childList = new ArrayList<>();





}
