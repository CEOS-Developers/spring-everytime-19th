package com.ceos19.springeverytime.post.domain;

import com.ceos19.springeverytime.global.BaseTimeEntity;
import com.ceos19.springeverytime.comment.domain.Comment;
import com.ceos19.springeverytime.Image.domain.Image;
import com.ceos19.springeverytime.postcategory.domain.PostCategory;
import com.ceos19.springeverytime.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToOne
    @JoinColumn(name = "category_id")
    private PostCategory category;

    @OneToMany(mappedBy = "post")
    private List<Comment> Comments;

    @OneToMany(mappedBy = "post")
    private List<Image> image;

    private int likeCount;
    private String title;
    private String content;

    public void increaseLikeCount(){
        likeCount++;
    }

    public void decreaseLikeCount() {
        likeCount--;
    }
}
