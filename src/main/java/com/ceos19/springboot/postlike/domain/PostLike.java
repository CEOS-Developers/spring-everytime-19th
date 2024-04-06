package com.ceos19.springboot.postlike.domain;

import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.users.domain.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE PostLike SET deleted = true WHERE like_id = ?")
@Where(clause = "deleted = false")
public class PostLike {
    @Id
    @GeneratedValue
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    private final boolean deleted = false;

    public PostLike(Post post,
                    Users user) {
        this.post = post;
        this.user = user;
    }
}
