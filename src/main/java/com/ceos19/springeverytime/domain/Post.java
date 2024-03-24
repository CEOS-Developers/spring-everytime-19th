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
public class Post {
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    @NonNull
    @ManyToOne
    private User author;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
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
}
