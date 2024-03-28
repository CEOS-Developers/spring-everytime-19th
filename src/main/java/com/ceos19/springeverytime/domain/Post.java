package com.ceos19.springeverytime.domain;

import com.ceos19.springeverytime.domain.like.Like;
import com.ceos19.springeverytime.domain.like.PostLike;
import jakarta.persistence.*;
import lombok.*;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue
    private Long postId;

    @NonNull
    @Column(length = 100, nullable = false)
    private String title;

    @NonNull
    @Column(length = 2000, nullable = false)
    private String content;

    @NonNull
    private boolean isAnonymous;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    /**
     * 비즈니스 로직
     * */
    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Comment addComment(User author, String content, boolean isAnonymous) {
        Comment comment = Comment.builder().post(this).author(author).content(content).isAnonymous(isAnonymous).build();
        this.comments.add(comment);
        return comment;
    }

    public PostLike like(User user) {
        return new PostLike(user, this);
    }
}
