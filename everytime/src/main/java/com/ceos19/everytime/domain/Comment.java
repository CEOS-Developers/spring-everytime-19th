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
import static java.util.Arrays.stream;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@ToString(exclude = {"user", "post", "replies"})
public class Comment extends BaseTimeEntity {  // 댓글
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    private Comment(String content) {
        this.content = content;
    }

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "user_id")
    @Setter(value = PROTECTED)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    @Setter(value = PROTECTED)
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    public List<Reply> getRepliesByContent(String content) {
        return replies.stream()
                .filter(r -> r.getContent().equals(content))
                .collect(Collectors.toList());
    }

    /**
     * 연관관계 편의 메서드
     */
    public void addReplyFromUser(User user, String replyContent) {
        Reply reply = Reply.createReply(user, replyContent);
        reply.setComment(this);
        replies.add(reply);
    }

    public void removeReplyFromUserAndComment(Reply reply) {
        replies.remove(reply);
        reply.getUser().getReplies().remove(reply);
    }

    protected static Comment createComment(User user, String content) {
        Comment comment = new Comment(content);
        user.addComment(comment);

        return comment;
    }
}
