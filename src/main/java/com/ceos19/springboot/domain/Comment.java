package com.ceos19.springboot.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
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
}
