package com.ceos19.springeverytime.domain.like;

import com.ceos19.springeverytime.domain.Comment;
import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Entity
@DiscriminatorValue("C")
@NoArgsConstructor
@Getter
public class CommentLike extends Like {
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public CommentLike(@NonNull User user, Comment comment) {
        super(user);
        this.comment = comment;
    }
}
