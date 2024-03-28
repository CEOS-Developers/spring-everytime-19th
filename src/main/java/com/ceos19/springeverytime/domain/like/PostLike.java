package com.ceos19.springeverytime.domain.like;

import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.user.domain.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@DiscriminatorValue("P")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostLike extends Like {
    @NonNull
    @ManyToOne
    @JoinColumn(name = "post_id", updatable = false)
    private Post post;

    public PostLike(@NonNull User user, @NonNull Post post) {
        super(user);
        this.post = post;
    }
}
