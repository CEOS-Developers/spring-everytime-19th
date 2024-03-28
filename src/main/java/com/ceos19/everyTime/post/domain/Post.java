package com.ceos19.everyTime.post.domain;

import com.ceos19.everyTime.common.BaseEntity;
import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.post.dto.editor.PostEditor;
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
    private String title;

    @Column(nullable = false,length = 500)
    private String contents;

    @Column(nullable = false)
    private int replyCount;

    @Column(nullable = false)
    boolean isQuestion;

    @Column(nullable = false)
    boolean isHideNickName;

    @Column(nullable = false)
    private int hideNameSequence;

    @Builder
    public Post(Member member,Community community,String title, String contents,boolean isQuestion, boolean isHideNickName){
        this.member=member;
        this.community=community;
        this.title=title;
        this.contents=contents;
        this.isQuestion=isQuestion;
        this.replyCount=0;
        this.likeCount=0;
        this.isHideNickName = isHideNickName;
        this.hideNameSequence = 0;
    }


    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<PostImage> postImageList = new ArrayList<>();

   public void changeTitleAndContentAndIsQuestionAndIsHideNickName(String title,String contents,boolean isQuestion,boolean isHideNickName){
       this.title = title;
       this.contents = contents;
       this.isQuestion = isQuestion;
       this.isHideNickName = isHideNickName;
   }

   public PostEditor.PostEditorBuilder toEditor(){
       return PostEditor.builder()
           .title(this.title)
           .content(this.contents)
           .isQuestion(this.isQuestion)
           .hideNickName(this.isHideNickName);
   }

   public void edit(PostEditor postEditor){
       this.title= postEditor.getTitle();
       this.contents = postEditor.getContent();
       this.isQuestion = postEditor.isQuestion();
       this.isHideNickName = postEditor.isHideNickName();
   }

   public void increaseReplyCount(){
       this.replyCount++;
   }

   public void decreaseReplyCount(){
       if(this.replyCount>0){
           this.replyCount--;
       }
   }

    public void increaseLikeCount(){
        this.likeCount++;
    }

    public void decreaseLikeCount(){
        if(this.likeCount>0){
            this.likeCount--;
        }
    }

    public void saveImage(String originalName,String accessUrl){
        PostImage postImage =PostImage.builder()
            .originalName(originalName)
            .accessUrl(accessUrl)
            .post(this)
            .build();

        postImageList.add(postImage);
    }

    public void changeImage(String originalName,String accessUrl){
        PostImage postImage =PostImage.builder()
            .originalName(originalName)
            .accessUrl(accessUrl)
            .post(this)
            .build();

        postImageList.add(postImage);
    }

    public int getIncreaseHideNameSequence(){
       return ++hideNameSequence;
    }








}
