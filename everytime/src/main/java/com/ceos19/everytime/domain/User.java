package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@ToString(exclude = {"school", "posts", "hearts", "sendMessages", "receiveMessages", "comments", "replies"})
public class User {  // 유저
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String studentNo;

    @Email
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "school_id")
    @Setter(value = PROTECTED)
    private School school;

    public User(String name, String studentNo, String username, String password) {
        this.name = name;
        this.studentNo = studentNo;
        this.username = username;
        this.password = password;
    }

    public User(String name, String studentNo, String username, String password, School school) {
        this.name = name;
        this.studentNo = studentNo;
        this.username = username;
        this.password = password;
        this.school = school;
    }

    @OneToMany(mappedBy = "user", cascade = PERSIST, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true) //유저가 제거되도 게시물의 좋아요는 유지됨.
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = ALL, orphanRemoval = true)
    private List<Message> sendMessages = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = ALL, orphanRemoval = true)
    private List<Message> receiveMessages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    /**
     * 연관관계 편의 메서드
     */
    public void addPost(Post post) {
        post.setUser(this);
        posts.add(post);
    }

    public Post createPost(String title, String content) {
        Post post = new Post(title, content);
        addPost(post);
        return post;
    }

    // User + 중간 테이블 + Message간의 연관관계를 매핑해주는 메서드

    protected void addSendMessage(Message message) {
        message.setSender(this);
        sendMessages.add(message);
    }
    // User + 중간 테이블 + Message간의 연관관계를 매핑해주는 메서드
    protected void addReceiveMessage(Message message) {
        message.setReceiver(this);
        receiveMessages.add(message);
    }

    // Post에서 User를 파라미터로 받아 comment 다는 경우
    protected void addComment(Comment comment) {
        comment.setUser(this);
        comments.add(comment);
    }

    // Comment에서 User를 파라미터로 받아 Reply를 다는 경우
    protected void addReply(Reply reply) {
        reply.setUser(this);
        replies.add(reply);
    }

    protected void addHearts(Heart heart) {
        heart.setUser(this);
        hearts.add(heart);
    }
}
