package com.ceos19.springboot.comment.domain;

import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.users.domain.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Comment SET deleted = true WHERE comment_id = ?")
@Where(clause = "deleted = false")
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Users user;


    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @Builder.Default
    private List<Comment> children = new ArrayList<>();

    private String content;

    private int likes = 0;

    private final boolean deleted = false;

    public Comment(Post post,
                   Users user,
                   Comment parent,
                   List<Comment> children) {
        this.post = post;
        this.user = user;
        this.parent = parent;
        this.children = children;
    }

    public void addChildComment(Comment childComment) {
        this.children.add(childComment);
    }
    public void pressLike() {
        this.likes ++;
    }
}
