package com.ceos19.springeverytime.domain.post.domain;

import com.ceos19.springeverytime.domain.BaseEntity;
import com.ceos19.springeverytime.domain.comment.domain.Comment;
import com.ceos19.springeverytime.domain.image.domain.Image;
import com.ceos19.springeverytime.domain.category.domain.Category;
import com.ceos19.springeverytime.domain.like.domain.PostLike;
import com.ceos19.springeverytime.domain.post.dto.request.PostUpdateRequest;
import com.ceos19.springeverytime.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    /**
     * 비즈니스 로직
     * */
    public void update(PostUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public Comment addComment(User author, String content, boolean isAnonymous) {
        Comment comment = Comment.builder().post(this).author(author).content(content).isAnonymous(isAnonymous).build();
        this.comments.add(comment);
        return comment;
    }
}
