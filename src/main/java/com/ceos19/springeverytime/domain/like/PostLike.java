package com.ceos19.springeverytime.domain.like;

import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@DiscriminatorValue("P")
public class PostLike extends Like {
    @NonNull
    @ManyToOne
    Post post;

    public PostLike(@NonNull User user, @NonNull Date createDate, @NonNull Post post) {
        super(user, createDate);
        this.post = post;
    }
}
