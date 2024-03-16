package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@ToString(exclude = {"user", "hearts", "comments", "images"})
public class Post extends BaseTimeEntity {  //게시물
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")  //select * from post p join user u on p.user_id = u.id;
    @Setter(value = PROTECTED)
    private User user;

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    /**
     * 조회 메서드
     */
    public List<Comment> getCommentsByContent(String content) {
        return comments.stream()
                .filter(r -> r.getContent().equals(content))
                .collect(Collectors.toList());
    }



    /**
     * 연관관계 편의 메서드
     */
    public void addImage(Attachment attachment) {
        attachment.setPost(this);
        attachments.add(attachment);
    }

    // User, Heart, Post의 연관관계는 Post에서 관리함
    public void addHeartFromUser(User user) {
        Heart heart = Heart.createHeart(user);
        heart.setPost(this);
        hearts.add(heart);
    }

    public void removeHeartFromUserAndPost(Heart heart) {  // 제거 메서드
        hearts.remove(heart);
        heart.getUser().getHearts().remove(heart);
    }

    public void removeHeartFromUserAndPost(User user) {  // 제거 메서드
        Optional<Heart> heartByUser = getHeartByUser(user);
        if (heartByUser.isEmpty()) {
            return;
        }
        Heart heart = heartByUser.get();
        hearts.remove(heart);
        heart.getUser().getHearts().remove(heart);
    }
    private Optional<Heart> getHeartByUser(User user) {
        return hearts.stream()
                .filter(h -> h.getUser().equals(user))
                .findFirst();
    }

    // Post에서 User를 파라미터로 받아 Comment 다는 경우
    public void addCommentFromUser(User user, String commentContent) {
        Comment comment = Comment.createComment(user, commentContent);
        comment.setPost(this);
        comments.add(comment);
    }

    public void removeCommentFromUserAndPost(Comment comment) {  // 제거 메서드
        comments.remove(comment);
        comment.getUser().getComments().remove(comment);
    }
}
